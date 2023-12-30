package org.company.app

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.company.app.ui.YoutubeVideoPlayer

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
/*@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            VideoView(context).apply {
                setVideoPath(url)
                val mediaController = MediaController(context)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)
                start()
            }
        },
        update = {})
}*/
//@RequiresApi(Build.VERSION_CODES.S)
//@Composable
//internal actual fun VideoPlayer(modifier: Modifier,url: String?, thumbnail:String?){
//    var isPlaying by remember { mutableStateOf(false) }
//    CustomVideoPlayer(
//        modifier = modifier,
//        videoUrl = url,
//        thumbnailResId = thumbnail.toString(),
//        isPlaying = isPlaying,
//        onClickPlay = {
//            // Toggle the play state
//            isPlaying = !isPlaying
//        }
//    )
//}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    YoutubeVideoPlayer(youtubeURL = url)
}
@Composable
internal actual fun Notify(message: String) {
    val coroutineContext = LocalContext.current
    Toast.makeText(coroutineContext, message, Toast.LENGTH_LONG).show()
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String){
   val shareIntent = Intent().apply {
       action = Intent.ACTION_SEND
       putExtra(Intent.EXTRA_SUBJECT, title)
       putExtra(Intent.EXTRA_TEXT, "$title: $videoUrl")
       type = "text/plain"
   }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share Video"))
}