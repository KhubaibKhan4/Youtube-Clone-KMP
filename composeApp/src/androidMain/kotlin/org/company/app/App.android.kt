package org.company.app

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
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
internal actual fun VideoPlayer(modifier: Modifier, url: String?) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            VideoView(context).apply {
                try {
                    setVideoURI(Uri.parse(url)) // Use setVideoURI instead of setVideoPath
                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)

                    // Introduce a delay before calling start
                    Handler(Looper.getMainLooper()).postDelayed({
                        start()
                    }, 1000) // 1000 milliseconds delay
                } catch (e: Exception) {
                    Log.e("VideoPlayer", "Error setting up video playback: ${e.message}")
                }

                setOnErrorListener { mp, what, extra ->
                    Log.e("VideoPlayer", "Error during playback - what: $what, extra: $extra")
                    true // Return true to indicate that the error is handled
                }

                setOnPreparedListener {
                    Log.d("VideoPlayer", "Video prepared successfully")
                }

                setOnCompletionListener {
                    Log.d("VideoPlayer", "Video playback completed")
                }
            }
        },
        update = {}
    )
}
