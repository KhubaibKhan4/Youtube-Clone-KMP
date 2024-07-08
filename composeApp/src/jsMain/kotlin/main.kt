import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.company.app.App
import org.company.app.di.appModule
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId = "1:77959177776:web:3ec6ad19a2e3888f7c2ff5",
            apiKey = "AIzaSyCNt3M8kkTgxs6xWC9LGExe1_bLeQNuFQs",
            projectId = "clone-ef3ed"
        )
    )
    onWasmReady {
        CanvasBasedWindow("Youtube Clone") {
            App()
        }
    }
}