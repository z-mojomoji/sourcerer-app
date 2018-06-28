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
        const val CLASSIFIERS_DIR = "classifiers"
        const val DATA_EXT = ".pb"
    }

    val cache = hashMapOf<String, Classifier>()

    /**
     * Returns libraries used in a line.
     */
    fun estimate(line: List<String>, libraries: List<String>): List<String> {
        return libraries.filter { libId ->
            if (!cache.containsKey(libId)) {
                // Library not downloaded from cloud storage.
                if (FileHelper.notExists(libId + DATA_EXT, CLASSIFIERS_DIR)) {
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
            // Prediction based on two classes.
            val prob = prediction[cache[libId]!!.libraries.indexOf(libId)]
            // Libraries with no imports.
            if (libId == "rb.rails") {
                prob > 0.8
            } else {
                prob > 0.5
            }
        }
    }

    /**
     * Downloads libraries from cloud.
     */
    private fun downloadClassifier(libId: String) {
        val file = FileHelper.getFile(libId + DATA_EXT, CLASSIFIERS_DIR)
        val langId = libId.split('.')[0]
        val url = "${BuildConfig.LIBRARY_MODELS_URL}$langId/$libId$DATA_EXT"
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
        val bytesArray = FileHelper.getFile(libId + DATA_EXT, CLASSIFIERS_DIR)
            .readBytes()
        cache[libId] = Classifier(bytesArray)
    }
}
