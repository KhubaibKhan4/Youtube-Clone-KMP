package org.company.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.browser.window

internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}
@Composable
internal actual fun VideoPlayer(modifier: Modifier,url: String?,thumbnail:String?){

}