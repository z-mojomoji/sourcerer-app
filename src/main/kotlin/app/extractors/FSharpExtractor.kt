// Copyright 2018 Sourcerer Inc. All Rights Reserved.
// Author: Tuomas Hietanen
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)

package app.extractors

import app.model.CommitStats
import app.model.DiffFile

class FSharpExtractor : ExtractorInterface {
    companion object {
        const val LANGUAGE_NAME = Lang.FSHARP
        val importRegex = Regex("""^.*open\s+(\w+[.\w+]*)""")
        val commentRegex = Regex("""^([^\n]*//)[^\n]*""")
        val extractImportRegex = Regex("""open\s+(\w+[.\w+]*)""")
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
                imports.add(res.groupValues[1])
            }
        }

        return imports.toList()
    }

    override fun tokenize(line: String): List<String> {
        val importRegex = Regex("""^.*open\s+(\w+[.\w+]*)""")
        val commentRegex = Regex("""^([^\n]*//)[^\n]*""")
        var newLine = importRegex.replace(line, "")
        newLine = commentRegex.replace(newLine, "")
        return super.tokenize(newLine)
    }

    override fun mapImportToIndex(import: String, lang: String): String {
        // The behaviour of fsharp library classifier is the same as for csharp.
        // TODO(anatoly): Use "csharp" classifier.
        return import
    }

    override fun getLanguageName(): String? {
        return LANGUAGE_NAME
    }
}
