// Copyright 2018 Sourcerer Inc. All Rights Reserved.
// Author: Liubov Yaronskaya (lyaronskaya@sourcerer.io)

package app.model

import app.BuildConfig
import app.Logger
import app.utils.FileHelper
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import java.io.FileOutputStream

class ClassifiersPull(val language: String) {
    var classifiersCaсhe = hashMapOf<String, Classifier>()

    private val modelsDir = "models"
    private val pbExt = ".pb"
    /**
     * Returns libraries used in a line.
     */
    fun estimate(line: List<String>, libraries: List<String>): List<String> {
        // TODO(lyaronskaya): case with no imports
        val extended_libraries = libraries.toMutableList()
        if (language == "ruby") {
            extended_libraries.add("rb.rails")
        }
        val output = extended_libraries.filter { library ->
            if (!classifiersCaсhe.containsKey(library)) {
                if (FileHelper.notExists(library + pbExt, "$modelsDir/$language")) {
                    Logger.info { "Downloading " + library }
                    downloadClassifier(library)
                    Logger.info { "Downloaded " + library }
                }
                Logger.info { "Loading $library evaluator" }
                addClassifier(library)
                Logger.info { "$library evaluator ready" }
            }
            val prediction = classifiersCaсhe[library]!!.evaluate(line)
            prediction[0] > prediction[1]
        }
        return output
    }

    fun downloadClassifier(id: String) {
        val url = BuildConfig.LIBRARY_MODELS_URL + "$language/$id.pb"

        val file = FileHelper.getFile(id + pbExt, "$modelsDir/$language")
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
        }
        catch (e: Exception) {
            Logger.error(e, "Failed to download $id model")
        }
    }

    fun addClassifier(id: String) {
        val bytesArray = FileHelper.getFile(id + pbExt, "$modelsDir/$language").readBytes()
        val classifier = Classifier(bytesArray)
        classifiersCaсhe.put(id, classifier)
    }
}
