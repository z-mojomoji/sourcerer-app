// Copyright 2018 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)
// Author: Anatoly Kislov (anatoly@sourcerer.io)

package app.extractors

import app.BuildConfig
import app.Logger
import app.model.Classifier
import app.utils.FileHelper
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import java.io.FileOutputStream

class ClassifierManager {
    companion object {
        const val MODELS_DIR = "models"
        const val DATA_EXT = ".pb"
    }

    val cache = hashMapOf<String, Classifier>()

    /**
     * Returns libraries used in a line.
     */
    fun estimate(line: List<String>, libraries: List<String>): List<String> {
        return libraries.filter { libraryId ->
            if (!cache.containsKey(libraryId)) {
                // Library not downloaded from cloud storage.
                if (FileHelper.notExists(libraryId + DATA_EXT, MODELS_DIR)) {
                    Logger.info { "Downloading $libraryId" }
                    downloadClassifier(libraryId)
                    Logger.info { "Downloaded $libraryId" }
                }

                // Library not loaded from local storage.
                Logger.info { "Loading $libraryId evaluator" }
                loadClassifier(libraryId)
                Logger.info { "$libraryId evaluator ready" }
            }

            // Check line for usage of a library.
            val prediction = cache[libraryId]!!.evaluate(line)
            prediction[0] > prediction[1]
        }
    }

    /**
     * Downloads libraries from cloud.
     */
    private fun downloadClassifier(libraryId: String) {
        val url = getDownloadUrl(libraryId)

        val file = FileHelper.getFile(libraryId + DATA_EXT, MODELS_DIR)
        val builder = HttpClientBuilder.create()
        val client = builder.build()
        try {
            client.execute(HttpGet(url)).use { response ->
                val entity = response.entity
                if (entity != null) {
                    FileOutputStream(file).use { outstream ->
                        entity.writeTo(outstream)
                        outstream.flush()
                        outstream.close()
                    }
                }

            }
        } catch (e: Exception) {
            Logger.error(e, "Failed to download $libraryId model")
        }
    }

    private fun getDownloadUrl(libraryId: String): String {
        // TODO(anatoly): Get language from libraries list or lib id.
        val language = ""

        return "${BuildConfig.LIBRARY_MODELS_URL}$language/$libraryId$DATA_EXT"
    }

    /**
     * Loads libraries from local storage to cache.
     */
    private fun loadClassifier(libraryId: String) {
        val bytesArray = FileHelper.getFile(libraryId + DATA_EXT, MODELS_DIR)
            .readBytes()
        cache[libraryId] = Classifier(bytesArray)
    }
}
