package org.company.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.FirebaseApp
import com.youtube.clone.db.YoutubeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.company.app.di.appModule
import org.company.app.shortcuts.TopTrending
import org.company.app.shortcuts.dynamicShortcut
import org.company.app.shortcuts.latestVideos
import org.company.app.shortcuts.pinnedShortCut
import org.company.app.ui.YoutubeShortsPlayer
import org.company.app.ui.YoutubeVideoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
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
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
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
                Toast.makeText(this, "Permissions are required to proceed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun proceedWithTask() {
        // Proceed with video download logic
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    YoutubeVideoPlayer(youtubeURL = url)
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun ShortsVideoPlayer(url: String?, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        YoutubeShortsPlayer(youtubeURL = url)
    }
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

@Composable
actual fun isConnected(): Flow<Boolean> {
    val context = LocalContext.current
    return callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
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