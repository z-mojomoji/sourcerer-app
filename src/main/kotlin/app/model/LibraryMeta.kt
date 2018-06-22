// Copyright 2018 Sourcerer Inc. All Rights Reserved.
// Author: Anatoly Kislov (anatoly@sourcerer.io)
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)

package app.model

import app.ClassifierProtos
import com.google.protobuf.InvalidProtocolBufferException
import java.security.InvalidParameterException

data class Library (
    val id: String,
    val imports: List<String>,
    val version: Int
)

class LibraryMeta (
    var langLibraries: HashMap<String, List<Library>> = hashMapOf()
){
    val importToIndexMap = buildImportToIndexMap(langLibraries)

    fun buildImportToIndexMap(langLibraries: HashMap<String, List<Library>>):
        HashMap<String, Map<String, String>> {
        val importToIndexMap = hashMapOf<String, Map<String, String>>()

        for ((lang, libs) in langLibraries) {
            val map = hashMapOf<String, String>()
            libs.forEach { lib -> lib.imports.forEach { import ->
                map[import] = lib.id
            }}
            importToIndexMap[lang] = map
        }

        return importToIndexMap
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

    @Throws(InvalidParameterException::class)
    constructor(proto: ClassifierProtos.LibrariesMeta) : this() {
        val tempMap = proto.languagesList.associateBy({ lang -> lang.id },
            { lang ->
                lang.librariesList.map { lib ->
                    Library(lib.id, lib.importsList, lib.version)
                }
            })
        langLibraries = HashMap(tempMap)
    }

    @Throws(InvalidProtocolBufferException::class)
    constructor(bytes: ByteArray) : this(ClassifierProtos.LibrariesMeta.parseFrom(bytes))

    constructor(serialized: String) : this(serialized.toByteArray())

    fun getProto(): ClassifierProtos.LibrariesMeta {
        return ClassifierProtos.LibrariesMeta.newBuilder()
            .addAllLanguages(langLibraries.map({ (langId, libs) ->
                ClassifierProtos.Language.newBuilder()
                    .setId(langId)
                    .addAllLibraries(libs.map { lib ->
                        ClassifierProtos.Library.newBuilder()
                            .setId(lib.id)
                            .setVersion(lib.version)
                            .addAllImports(lib.imports)
                            .build()
                    })
                    .build()
            }))
            .build()
    }

    fun serialize(): ByteArray {
        return getProto().toByteArray()
    }
}
