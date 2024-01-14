package org.company.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javafx.application.Platform
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


        // Set the user agent to simulate a browser for YouTube
        webEngine.userAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
        val script = """
    setTimeout(function() {
        var element = document.querySelector('.ytp-chrome-top.ytp-show-cards-title');
        if (element !== null) {
            element.style.display = "none";
        }
        // Repeat the above pattern for other elements...
    }, 1000); // Adjust the timeout value as needed
""".trimIndent()


        webEngine.executeScript(script)

        // Enable JavaScript support for YouTube embed player
        webEngine.isJavaScriptEnabled = true

        // Enable full-screen mode support
        webEngine.executeScript("document.webkitExitFullscreen = function() {};")
        // Load the YouTube video using the embed URL
        webEngine.load(url)
        val scene = Scene(webView)
        setScene(scene)
    }
}