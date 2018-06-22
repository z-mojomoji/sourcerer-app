// Copyright 2017 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)
// Author: Anatoly Kislov (anatoly@sourcerer.io)

package app.extractors

import app.model.CommitStats
import app.model.DiffFile

class RubyExtractor : ExtractorInterface {
    companion object {
        const val LANGUAGE_NAME = Lang.RUBY
        val importRegex = Regex("""(require\s+'(\w+)'|load\s+'(\w+)\.\w+')""")
        val commentRegex = Regex("""^([^\n]*#)[^\n]*""")
        val extractImportRegex = Regex("""(require\s+'(\w+)'|load\s+'(\w+)\.\w+')""")
    }

    override fun extract(files: List<DiffFile>): List<CommitStats> {
        files.map { file -> file.lang = LANGUAGE_NAME }
        return super.extract(files)
    }

    override fun extractImports(fileContent: List<String>): List<String> {
        val imports = mutableSetOf<String>()

        fileContent.forEach {
            val res = extractImportRegex.find(it)
            if (res != null) {
                val lineLib = res.groupValues.last { it != "" }
                imports.add(lineLib)
            }
        }

        return imports.toList()
    }

    override fun tokenize(line: String): List<String> {
        var newLine = importRegex.replace(line, "")
        newLine = commentRegex.replace(newLine, "")
        return super.tokenize(newLine)
    }

    override fun determineLibs(line: String,
                               fileLibraries: List<String>): List<String> {
        // TODO(lyaronskaya): Case with no imports.
        val libraries = fileLibraries + "rb.rails"

        return super.determineLibs(line, libraries)
    }

    override fun getLanguageName(): String? {
        return LANGUAGE_NAME
    }
}
