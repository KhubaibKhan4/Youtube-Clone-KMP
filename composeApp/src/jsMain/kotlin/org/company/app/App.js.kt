package org.company.app

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.youtube.clone.db.YoutubeDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import org.w3c.dom.url.URL

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@Composable
internal actual fun provideShortCuts() {
    return
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    window.open(url = videoUrl, "_blank")
}

internal actual fun UserRegion(): String {
    return js("window.navigator.language.slice(-2)")
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val workerScriptUrl =
            js("import.meta.url.replace('kotlin', 'node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js')")
        val driver = WebWorkerDriver(workerScriptUrl).also { YoutubeDatabase.Schema.create(it) }
        return driver
    }
}

actual class VideoDownloader {
    actual suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String {
        return withContext(Dispatchers.Main) {
            try {
                console.log("Downloading video from: $url")
                val response = window.fetch(url).await()

                if (response.ok) {
                    val blob = response.blob().await()
                    console.log("Blob received:", blob)

                    val downloadUrl = URL.createObjectURL(blob)
                    onProgress(1.0f, "Download complete")

                    downloadUrl
                } else {
                    throw Exception("Failed to download video: ${response.statusText}")
                }
            } catch (e: Exception) {
                console.error("Error downloading video:", e.message)
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

actual fun HttpClientConfig<*>.setupHttpCache() {
    install(HttpCache) {
        // Implement web-specific storage, using IndexedDB or localStorage
        // Placeholder implementation
        // publicStorage(WebStorage())
    }
}

@Composable
internal actual fun showDialog(title: String, message: String) {
}