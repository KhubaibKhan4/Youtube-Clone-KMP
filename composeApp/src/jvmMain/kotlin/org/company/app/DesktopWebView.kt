package org.company.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebView
import javax.swing.JPanel

@Composable
fun DesktopWebView(
    modifier: Modifier,
    url: String,
) {
    val jPanel: JPanel = remember { JPanel() }
    val jfxPanel = JFXPanel()

    SwingPanel(
        factory = {
            jfxPanel.apply { buildWebView(url) }
            jPanel.add(jfxPanel)
        },
        modifier = modifier,
    )

    DisposableEffect(url) { onDispose { jPanel.remove(jfxPanel) } }
}

private fun JFXPanel.buildWebView(url: String) {
    Platform.runLater {
        val webView = WebView()
        val webEngine = webView.engine

        webEngine.userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"

        webEngine.isJavaScriptEnabled = true

        webEngine.load(url)

        val scene = Scene(webView)
        setScene(scene)

        webEngine.loadWorker.stateProperty().addListener { _, _, newState ->
            if (newState == Worker.State.SUCCEEDED) {
                val script = """
                    setTimeout(function() {
                        var overlaySelectors = [
                            '.ytp-gradient-top',
                            '.ytp-gradient-bottom'
                        ];
                        overlaySelectors.forEach(function(selector) {
                            var element = document.querySelector(selector);
                            if (element !== null) {
                                element.style.display = 'none';
                            }
                        });
                    }, 1000); // Adjust the timeout value as needed
                """.trimIndent()
                webEngine.executeScript(script)
            }
        }
    }
}
