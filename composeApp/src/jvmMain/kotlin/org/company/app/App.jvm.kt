package org.company.app

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.youtube.clone.db.YoutubeDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.file.Paths
import java.util.Locale
import java.util.concurrent.TimeUnit

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}


@Composable
internal actual fun provideShortCuts() {
    return
}

fun splitLinkForVideoId(
    url: String?,
): String {
    return url?.substringAfter("v=").toString()
}

private fun openYouTubeVideo(videoUrl: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(videoUrl))
    }
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(videoUrl))
    }
}

fun splitLinkForShotsVideoId(url: String?): String {
    return url!!.split("v=").get(1)
}

internal actual fun UserRegion(): String {
    val currentLocale: Locale = Locale.getDefault()
    return currentLocale.country
}

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        if (!File("YouTubeDatabase.db").exists()) {
            YoutubeDatabase.Schema.create(driver)
        }
        return driver
    }
}
actual class VideoDownloader {
    actual suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String {
        return withContext(Dispatchers.IO) {
            try {
                val userHome = System.getProperty("user.home")
                val downloadDir = Paths.get(userHome, "Desktop").toString()
                val destination = "$downloadDir/%(title)s.%(ext)s"
                val command =
                    listOf("C:\\Program Files\\yt-dlp\\yt-dlp.exe", "-o", destination, url)
                val processBuilder = ProcessBuilder(command)
                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    output.append(line).append("\n")
                    onProgress(0.5f, line ?: "")
                }
                process.waitFor(10, TimeUnit.MINUTES)
                if (process.exitValue() != 0) {
                    throw Exception("Error downloading video: ${output.toString()}")
                }
                output.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

actual fun HttpClientConfig<*>.setupHttpCache() {
    install(HttpCache) {
        val cacheDir = Paths.get(System.getProperty("user.home"), ".cache", "myAppCache").toFile()
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        publicStorage(FileStorage(cacheDir))
    }
}

@Composable
internal actual fun showDialog(title: String, message: String) {
}