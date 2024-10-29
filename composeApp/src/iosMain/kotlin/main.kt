import androidx.compose.ui.window.ComposeUIViewController
import org.company.app.App
import org.company.app.di.appModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController{ App() }
