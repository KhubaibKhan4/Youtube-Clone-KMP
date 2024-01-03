package org.company.app.YoutubeWebView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState


@Composable
internal fun YoutubeWebViewScreen(modifier: Modifier, url: String) {


    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        val webViewState = rememberWebViewState(url)
        val navigator = rememberWebViewNavigator()


        /*
        webViewState.webSettings.apply {
            isJavaScriptEnabled = true
            androidWebSettings.apply {
                //isAlgorithmicDarkeningAllowed = true
                //safeBrowsingEnabled = true
            }
        }
         */

        Column {

            val loadingState = webViewState.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = modifier.fillMaxWidth()
                )
            } else if(loadingState is LoadingState.Finished){
                // Hide Button View in Yt
                val script = """
    document.querySelector('.ytp-chrome-top.ytp-show-cards-title').style.display = "none";
    document.querySelector('.ytp-youtube-button.ytp-button.yt-uix-sessionlink').style.display = "none";
    document.querySelector('.ytp-impression-link').style.display = "none";
    document.querySelector('.ytp-pause-overlay-container').style.display = "none";
""".trimIndent()

                navigator.evaluateJavaScript(script)
                //navigator.evaluateJavaScript("document.getElementById('my-element').style.display = 'none'")

            }
            com.multiplatform.webview.web.WebView(
                state = webViewState,
                navigator = navigator,
                modifier = modifier.fillMaxSize()
            )

        }


    }

}