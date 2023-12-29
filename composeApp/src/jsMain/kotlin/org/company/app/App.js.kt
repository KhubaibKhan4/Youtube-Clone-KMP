package org.company.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.dom.Video
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLVideoElement

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {

}

@Composable
internal actual fun Notify(message: String) {
    window.alert(message)
}