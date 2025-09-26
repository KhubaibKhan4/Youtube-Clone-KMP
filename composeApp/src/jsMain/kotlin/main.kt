import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.browser.document
import kotlinx.browser.window
import org.company.app.App
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId = "1:77959177776:web:3ec6ad19a2e3888f7c2ff5",
            apiKey = "AIzaSyCNt3M8kkTgxs6xWC9LGExe1_bLeQNuFQs",
            projectId = "clone-ef3ed"
        )
    )
    if (js("navigator.serviceWorker")) {
        js("navigator.serviceWorker.register('/service-worker.js')")
            .then { console.log("Service Worker Registered") }
            .catch { console.log("Service Worker Registration Failed") }
    }

    onWasmReady {
        ComposeViewport(viewportContainerId = "ComposeTarget") {
            App()
        }
    }
}