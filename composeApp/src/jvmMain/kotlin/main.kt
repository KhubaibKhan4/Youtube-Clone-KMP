import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.core.lifecycle.DefaultScreenLifecycleOwner.onDispose
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Dimension
import org.company.app.App
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.io.File
import kotlin.math.max

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        title = "Youtube Clone",
        icon = painterResource("youtube_music.png"),
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        // Necessary for WebView :( for youtube embed
        // It usually happens once
        // https://github.com/KevinnZou/compose-webview-multiplatform/blob/main/README.desktop.md
        ConfigWebView {
            App()

        }
        //App()
    }
}


@Composable
fun ConfigWebView(onFinish: @Composable () -> Unit) {
    var restartRequired by remember { mutableStateOf(false) }
    var downloading by remember { mutableStateOf(0F) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {
                        downloading = max(it, 0F)
                    }
                    onInitialized {
                        initialized = true
                    }
                }
                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {
                restartRequired = true
            })
        }
    }

    if (restartRequired) {
        Text(
            text = "Please\nRestart the application",
            color = MaterialTheme.colorScheme.error
        )
    } else {
        if (initialized) {
            onFinish()
        } else {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "One moment...\nSetting up ${"%.2f".format(downloading)}%",
                    color = MaterialTheme.colorScheme.primary
                )

            }

        }
    }

    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }

}