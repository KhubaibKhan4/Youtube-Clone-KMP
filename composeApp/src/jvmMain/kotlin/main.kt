import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.company.app.App
import org.company.app.di.appModule
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin
import java.awt.Dimension

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(
        title = "Youtube Clone",
        icon = painterResource(DrawableResource("youtube_music.png")),
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}