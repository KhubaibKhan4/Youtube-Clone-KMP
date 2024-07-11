package org.company.app.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.browser.document
import org.company.app.html.HtmlView
import org.w3c.dom.HTMLElement

@Composable
fun HTMLVideoPlayer(
    videoId: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HtmlView(
            modifier = modifier.fillMaxWidth(),
            factory = {
                val iframe = createElement("iframe") as HTMLElement
                iframe.setAttribute("width", "100%")
                iframe.setAttribute("height", "100%")
                iframe.setAttribute(
                    "src",
                    "https://www.youtube.com/embed/$videoId?autoplay=1&mute=1&showinfo=0"
                )
                iframe.setAttribute("frameborder", "0")
                iframe.setAttribute(
                    "allow",
                    "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                )
                iframe.setAttribute("allowfullscreen", "true")
                iframe.setAttribute("referrerpolicy", "no-referrer-when-downgrade")

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
                    }, 1000);
                """.trimIndent()
                injectJavaScript(iframe, script)
                iframe
            },
            update = {
                it.setAttribute("width", "100%")
                it.setAttribute("height", "100%")
            }
        )
    }
}
private fun injectJavaScript(iframe: HTMLElement, script: String) {
    val scriptElement = document.createElement("script") as HTMLElement
    scriptElement.textContent = script
    iframe.appendChild(scriptElement)
}
