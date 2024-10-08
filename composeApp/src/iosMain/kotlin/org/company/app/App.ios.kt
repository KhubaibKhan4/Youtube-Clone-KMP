package org.company.app

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.youtube.clone.db.YoutubeDatabase
import io.kamel.core.utils.File
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.HttpCacheEntry
import io.ktor.client.plugins.cache.storage.HttpCacheStorage
import io.ktor.http.Url
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGRect
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSLocale
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.NSUserDomainMask
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.dataTaskWithRequest
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIView
import platform.WebKit.WKWebView
import kotlin.collections.set

internal actual fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}



@Composable
internal actual fun provideShortCuts(){
    return
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val activityItems = listOf("$title: $videoUrl")
    val activityViewController = UIActivityViewController(activityItems, null)
    viewController?.presentViewController(activityViewController, true, null)
}

internal actual fun UserRegion(): String {
    return NSLocale.currentLocale.countryCode ?: "us"
}

actual class DriverFactory actual constructor(){
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(YoutubeDatabase.Schema,"YouTubeDatabase.db")
    }
}

actual class VideoDownloader {
    actual suspend fun downloadVideo(url: String, onProgress: (Float, String) -> Unit): String {
        return withContext(Dispatchers.Main) {
            try {
                val nsUrl = NSURL.URLWithString(url)
                val request = NSURLRequest.requestWithURL(nsUrl!!)
                val config = NSURLSessionConfiguration.defaultSessionConfiguration()
                val session = NSURLSession.sessionWithConfiguration(config, null, null)
                var downloadOutput = ""

                session.dataTaskWithRequest(request) { data, response, error ->
                    if (error != null) {
                        onProgress(1.0f, "Error: ${error.localizedDescription}")
                        downloadOutput = "Error: ${error.localizedDescription}"
                    } else {
                        val downloadDir = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).first() as String
                        val filePath = downloadDir + "/downloaded_video.mp4"
                        data?.writeToFile(filePath, true)
                        onProgress(1.0f, "Download complete: $filePath")
                        downloadOutput = filePath
                    }
                }.resume()

                downloadOutput
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}

actual fun HttpClientConfig<*>.setupHttpCache() {
    install(HttpCache) {
        val cacheDir = File("cache_directory") // Set your desired cache directory
        val cacheStorage = object : HttpCacheStorage() {
            private val cache = HashMap<String, HttpCacheEntry>()

            override fun find(url: Url, varyKeys: Map<String, String>): HttpCacheEntry? {
                return cache[url.toString()]
            }

            override fun findByUrl(url: Url): Set<HttpCacheEntry> {
                val entry = cache[url.toString()] ?: return emptySet()
                return setOf(entry)
            }

            override fun store(url: Url, value: HttpCacheEntry) {
                if (cache.size < 10 * 1024 * 1024) { // Check if cache size is less than 10MB
                    cache[url.toString()] = value
                } else {
                    // Handle cache size limit, e.g., evict oldest entries or similar
                }
            }
        }
        cacheStorage
    }
}

@Composable
internal actual fun showDialog(title: String, message: String) {
}