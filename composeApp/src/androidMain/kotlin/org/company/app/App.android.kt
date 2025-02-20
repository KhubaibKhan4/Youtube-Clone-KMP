package org.company.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.FirebaseApp
import com.youtube.clone.db.YoutubeDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.company.app.shortcuts.TopTrending
import org.company.app.shortcuts.dynamicShortcut
import org.company.app.shortcuts.latestVideos
import org.company.app.shortcuts.pinnedShortCut
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale
import java.util.concurrent.TimeUnit

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            App()
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            proceedWithTask()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                proceedWithTask()
            } else {
                Toast.makeText(this, "Permissions are required to proceed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun proceedWithTask() {

    }
}


@Composable
internal actual fun provideShortCuts() {
    val context = LocalContext.current
    pinnedShortCut(context)
    dynamicShortcut(context)
    latestVideos(context)
    TopTrending(context)
}

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, "$title: $videoUrl")
        type = "text/plain"
    }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share Video"))
}

internal actual fun UserRegion(): String {
    val currentLocale: Locale = Locale.getDefault()
    return currentLocale.country
}
actual class DriverFactory actual constructor() {
    private var context: Context = AndroidApp.INSTANCE.applicationContext
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(YoutubeDatabase.Schema, context, "YouTubeDatabase.db")
    }
}

actual class VideoDownloader {
    actual suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String {
        return withContext(Dispatchers.IO) {
            try {
                val context = AndroidApp.INSTANCE.applicationContext
                val ytDlpPath = copyExecutableToInternalStorage(context)

                val downloadDir = context.getExternalFilesDir(null)?.absolutePath
                    ?: throw IOException("Failed to get external files directory")
                val destination = "$downloadDir/%(title)s.%(ext)s"
                val command = listOf(ytDlpPath, "-o", destination, url)

                Log.d("VideoDownloader", "Command to run: $command")

                val processBuilder = ProcessBuilder(command)
                processBuilder.directory(context.filesDir)
                processBuilder.redirectErrorStream(true)

                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    output.append(line).append("\n")
                    onProgress(0.5f, line ?: "")
                    Log.d("VideoDownloader", "Output: $line")
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    process.waitFor(10, TimeUnit.MINUTES)
                }

                val exitValue = process.exitValue()
                Log.d("VideoDownloader", "Process exit value: $exitValue")

                if (exitValue != 0) {
                    throw Exception("Error downloading video. Exit code: $exitValue")
                }

                output.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("VideoDownloader", "Error: ${e.message}")
                "Error: ${e.message}"
            }
        }
    }
}

fun copyExecutableToInternalStorage(context: Context): String {
    val externalFilePath = "/storage/emulated/0/Download/yt-dlp.exe"
    val externalFile = File(externalFilePath)
    if (!externalFile.exists()) {
        throw IOException("yt-dlp.exe not found in external storage")
    }

    val internalFile = File(context.filesDir, "yt-dlp.exe")
    Log.d("VideoDownloader", "Copying yt-dlp.exe to ${internalFile.absolutePath}")

    externalFile.inputStream().use { input ->
        internalFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    try {
        Log.d("VideoDownloader", "Setting executable permissions")
        Runtime.getRuntime().exec("chmod 755 ${internalFile.absolutePath}").waitFor()
    } catch (e: IOException) {
        Log.e("VideoDownloader", "Failed to set executable permission", e)
        throw IOException("Failed to set executable permission", e)
    }

    Log.d("VideoDownloader", "yt-dlp.exe copied and set as executable")
    return internalFile.absolutePath
}

actual fun HttpClientConfig<*>.setupHttpCache() {
    install(HttpCache) {
        val cacheDir = File(AndroidApp.INSTANCE.cacheDir, "myAppCache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        publicStorage(FileStorage(cacheDir))
    }
}