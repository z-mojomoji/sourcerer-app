// Copyright 2017 Sourcerer Inc. All Rights Reserved.
// Author: Anatoly Kislov (anatoly@sourcerer.io)
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)

package app.extractors

import app.model.*

interface ExtractorInterface {
    companion object {
        private val classifierManager = ClassifierManager()
        // TODO (anatoly): Download libraries. val librariesMeta =
        val stringRegex = Regex("""(".+?"|'.+?')""")
        val splitRegex = Regex("""\s|,|;|\*|\n|\(|\)|\[|]|\{|}|\+|=|&|\$|!=|\.|>|<|#|@|:|\?|!""")
    }

    fun extract(files: List<DiffFile>): List<CommitStats> {
        return extractLangStats(files) + extractTechStats(files)
    }

    fun extractLangStats(files: List<DiffFile>): List<CommitStats> {
        val langStats = files.filter { file -> file.language.isNotBlank() }
            .groupBy { file -> file.language }
            .map { (language, files) -> CommitStats(
                numLinesAdded = files.fold(0) { total, file ->
                    total + file.getAllAdded().size },
                numLinesDeleted = files.fold(0) { total, file ->
                    total + file.getAllDeleted().size },
                type = Extractor.TYPE_LANGUAGE,
                tech = language)
            }
    }

    fun extractTechStats(files: List<DiffFile>): List<CommitStats> {
        files.forEach { file ->
            file.old.imports = extractImports(file.old.content)
            file.new.imports = extractImports(file.new.content)
        }


        val oldLibraryToCount = mutableMapOf<String, Int>()
        val newLibraryToCount = mutableMapOf<String, Int>()
        val oldFilesImports = files.fold(mutableSetOf<String>()) { acc, file ->
            acc.addAll(file.old.imports)
            acc
        }
        val newFilesImports = files.fold(mutableSetOf<String>()) { acc, file ->
            acc.addAll(file.new.imports)
            acc
        }

        // Skip library stats calculation if no imports found.
        if (oldFilesImports.isEmpty() && newFilesImports.isEmpty()) {
            return langStats
        }

        files.filter { file -> file.language.isNotBlank() }
            .forEach { file ->
                val oldFileImportedLibs = file.old.imports.map { import ->
                    librariesMetaStorage.mapImportToIndex(import, file.language)
                }.filterNotNull()
                val newFileImportedLibs = file.new.imports.map { import ->
                    librariesMetaStorage.mapImportToIndex(import, file.language)
                }.filterNotNull()

                val oldFileLibraries = mutableListOf<String>()
                file.getAllDeleted().forEach {
                    val lineLibs = getLineLibraries(it, oldFileImportedLibs)
                    oldFileLibraries.addAll(lineLibs)
                }
                oldFileImportedLibs.forEach { id ->
                    val numLines = oldFileLibraries.count { it == id }
                    oldLibraryToCount[id] =
                        oldLibraryToCount.getOrDefault(id, 0) + numLines
                }

                val newFileLibraries = mutableListOf<String>()
                file.getAllAdded().forEach {
                    val lineLibs = getLineLibraries(it, newFileImportedLibs)
                    newFileLibraries.addAll(lineLibs)
                }
                newFileImportedLibs.forEach { id ->
                    val numLines = newFileLibraries.count { it == id }
                    newLibraryToCount[id] =
                        newLibraryToCount.getOrDefault(id, 0) + numLines
                }
            }

        val allExtractedLibraries = oldLibraryToCount.keys +
            newLibraryToCount.keys

        return allExtractedLibraries.map { CommitStats(
            numLinesAdded = newLibraryToCount.getOrDefault(it, 0),
            numLinesDeleted = oldLibraryToCount.getOrDefault(it, 0),
            type = Extractor.TYPE_LIBRARY,
            tech = it
        ) }.filter { it.numLinesAdded > 0 || it.numLinesDeleted > 0 }
    }

    fun extractTechStats(diffs: List<Pair<String, DiffContent>>):
        Map<String, Int> {
        val libsCount = mutableMapOf<String, Int>()

        // Extract imports from files.
        diffs.forEach { (_, diff) ->
            diff.imports = extractImports(diff.content)
        }

        // Skip library stats calculation if no imports found.
        if (!diffs.any({ (_, diff) -> diff.imports.isNotEmpty() })) {
            return mapOf()
        }

        // Determine libraries used in each line.
        diffs.filter { (lang, _) -> lang.isNotBlank() }
            .forEach { (lang, diff) ->
                val importedLibs = diff.imports.mapNotNull { import ->
                    // TODO librariesMetaStorage.mapImportToIndex(import, lang)
                    import
                }

                diff.getAllDiffs().forEach { line ->
                    getLineLibraries(line, importedLibs).forEach { libId ->
                        libsCount[libId] = libsCount.getOrDefault(libId, 0) + 1
                    }
                }
            }

        return libsCount
    }

    fun extractImports(fileContent: List<String>): List<String> {
        return listOf()
    }

    fun tokenize(line: String): List<String> {
        val newLine = stringRegex.replace(line, "")
        //TODO(lyaronskaya): multiline comment regex
        val tokens = splitRegex.split(newLine)
            .filter { it.isNotBlank() && !it.contains('"') && !it.contains('\'')
                && it != "-" && it != "@"}
        return tokens
    }

    fun getLineLibraries(line: String,
                         fileLibraries: List<String>): List<String> {
        val language = getLanguageName()
        if (language != null) {
            return classifierManager.estimate(tokenize(line), fileLibraries)
        }

        return listOf()
    }

    fun getLanguageName(): String? {
        return null
    }
}
