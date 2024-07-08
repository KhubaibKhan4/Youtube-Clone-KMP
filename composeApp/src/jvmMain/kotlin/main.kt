import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
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
        projectId = "clone-ef3ed",
        applicationId = "1:77959177776:web:f37fa391d0b5385c7c2ff5",
        apiKey = "AIzaSyCNt3M8kkTgxs6xWC9LGExe1_bLeQNuFQs"
    )
    Firebase.initialize(Application(),option)


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