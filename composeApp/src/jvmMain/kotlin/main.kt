import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.FirebaseOptions
import org.company.app.App
import org.jetbrains.compose.resources.painterResource
import youtube_clone.composeapp.generated.resources.Res
import youtube_clone.composeapp.generated.resources.youtube_music
import java.awt.Dimension

fun main() = application {

    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform(){
        val storage = mutableMapOf<String,String>()
        override fun clear(key: String) {
            storage.remove(key)
        }

        override fun log(msg: String) {
            println(msg)
        }

        override fun retrieve(key: String): String? {
            return storage[key]
        }

        override fun store(key: String, value: String) {
            storage.set(key,value)
        }
    })

    val option = FirebaseOptions(
        projectId = "",
        applicationId = "",
        apiKey = ""
    )


    Window(
        title = "Youtube Clone",
        icon = painterResource(Res.drawable.youtube_music),
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}