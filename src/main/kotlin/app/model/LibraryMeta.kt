// Copyright 2018 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)

package app.model

import app.Logger
import java.io.FileReader

data class LibraryMeta(val id: String,
                       val imports: List<String>,
                       val repo: String,
                       val name: String)

class LibraryMetaStorage {
    private var libraries = hashMapOf<String, ArrayList<LibraryMeta>>()
    private var importToIndex = hashMapOf<String, Map<String, String>>()
    private val languages = listOf("cpp", "csharp", "fsharp", "go",
                            "java", "javascript", "kotlin", "objectivec", "php",
                            "python", "ruby", "swift")

    constructor(directory: String) {
        val klaxon = Klaxon()
        for (language in languages) {
            libraries[language] = arrayListOf()
            val librariesFileName = "$directory/$language.json"
            JsonReader(FileReader(librariesFileName)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val library = klaxon.parse<LibraryMeta>(reader)
                        if (library == null) {
                            Logger.info {"Failed to parse $library information."}
                        }
                        else libraries[language]!!.add(library!!)
                    }
                }
            }
        }
    }

    fun getImportToIndexMap(language: String): Map<String, String> {
        if (!importToIndex.containsKey(language)) {
            val result = hashMapOf<String, String>()
            for (library in libraries[language]!!) {
                library.imports.forEach { import -> result[import] = library.id }
            }
            importToIndex[language] = result
        }
        return importToIndex[language]!!
    }

    fun getImports(language: String): List<String> {
        if (language !in languages) {
            return emptyList()
        }
        val result = libraries[language]!!.fold(mutableSetOf<String>()) { acc, lib ->
            acc.addAll(lib.imports)
            acc
        }
        return result.toList()
    }

    /**
     *  Maps import to library id.
     *  If no such import in libraries, returns null.
     */
    fun mapImportToIndex(import: String, language: String): String? {
        if (language !in languages) {
            return null
        }
        if (language in listOf("java", "javascript", "csharp", "cpp", "kotlin")) {
            val languageMap = getImportToIndexMap(language)
            return languageMap.keys.find{ import.startsWith(it) }
        }
        if (language == "fsharp") {
            return getImportToIndexMap("csharp")[import]
        }
        return getImportToIndexMap(language)[import]
    }
}
