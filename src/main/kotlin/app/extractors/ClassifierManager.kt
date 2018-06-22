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
        val getDownloadUrl: (String, String) -> String = {lang, libId ->
            "${BuildConfig.LIBRARY_MODELS_URL}$lang/$libId$DATA_EXT"
        }
    }

    val cache = hashMapOf<String, Classifier>()

    /**
     * Returns libraries used in a line.
     */
    fun estimate(line: List<String>, libraries: List<String>): List<String> {
        return libraries.filter { libId ->
            if (!cache.containsKey(libId)) {
                // Library not downloaded from cloud storage.
                if (FileHelper.notExists(libId + DATA_EXT, MODELS_DIR)) {
                    Logger.info { "Downloading $libId classifier" }
                    downloadClassifier(libId)
                    Logger.info { "Finished downloading $libId classifier" }
                }

                // Library not loaded from local storage.
                Logger.info { "Loading $libId evaluator" }
                loadClassifier(libId)
                Logger.info { "$libId evaluator ready" }
            }

            // Check line for usage of a library.
            val prediction = cache[libId]!!.evaluate(line)
            prediction[0] > prediction[1]
        }
    }

    /**
     * Downloads libraries from cloud.
     */
    private fun downloadClassifier(libId: String) {
        val file = FileHelper.getFile(libId + DATA_EXT, MODELS_DIR)
        // TODO(anatoly): Set language.
        val url = getDownloadUrl("", libId)
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
            Logger.error(e, "Failed to download $libId classifier")
        }
    }

    /**
     * Loads libraries from local storage to cache.
     */
    private fun loadClassifier(libId: String) {
        val bytesArray = FileHelper.getFile(libId + DATA_EXT, MODELS_DIR)
            .readBytes()
        cache[libId] = Classifier(bytesArray)
    }
}
