package org.company.app

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.youtube.clone.db.YoutubeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.awt.Desktop
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.file.Paths
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.swing.JOptionPane

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}


@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    val videoId = splitLinkForVideoId(url.toString())
    DesktopWebView(modifier, "https://www.youtube.com/embed/$videoId")
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

@OptIn(ExperimentalResourceApi::class)
@Composable
internal actual fun Notify(message: String) {
    if (SystemTray.isSupported()) {
        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().createImage("logo.webp")
        val trayIcon = TrayIcon(image, "Desktop Notification")
        tray.add(trayIcon)
        trayIcon.displayMessage("Desktop Notification", message, TrayIcon.MessageType.INFO)
    } else {
        // Fallback for systems that don't support SystemTray
        JOptionPane.showMessageDialog(
            null,
            message,
            "Desktop Notification",
            JOptionPane.INFORMATION_MESSAGE
        )
    }
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(videoUrl))
    }
}

@Composable
internal actual fun ShortsVideoPlayer(url: String?, modifier: Modifier) {
    DesktopWebView(
        modifier = Modifier.width(640.dp).height(360.dp),
        url = "https://www.youtube.com/embed/$url/"
    )
}

fun splitLinkForShotsVideoId(url: String?): String {
    return url!!.split("v=").get(1)
}

internal actual fun UserRegion(): String {
    val currentLocale: Locale = Locale.getDefault()
    return currentLocale.country
}

@Composable
actual fun isConnected(): Flow<Boolean> {
    return flow {
        while (true) {
            val isConnected = try {
                val url = URL("https://www.youtube.com")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 30000
                connection.connect()
                connection.responseCode == 200
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
            emit(isConnected)
            delay(5000)
        }
    }
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
                val downloadDir = Paths.get(userHome, "Downloads").toString()
                val destination = "$downloadDir/%(title)s.%(ext)s"
                val command = listOf("C:\\Program Files\\yt-dlp\\yt-dlp.exe", "-o", destination, url)
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