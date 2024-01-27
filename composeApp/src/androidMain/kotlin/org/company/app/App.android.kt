package org.company.app

import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import org.company.app.ui.YoutubeShortsPlayer
import org.company.app.ui.YoutubeVideoPlayer
import java.util.Locale

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

private fun topNewsPinnedShortcut(context: Context) {
    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (shortcutManager.isRequestPinShortcutSupported) {
        val shortcutInfo = ShortcutInfo.Builder(context, "top_News")
            .setIcon(Icon.createWithResource(context, R.drawable.music_icon))
            .setShortLabel("Trending News")
            .setLongLabel("Top Trending News Around the World")
            .setIntent(
                Intent(
                    Intent.ACTION_SEND
                ).setComponent(ComponentName("org.company.app.androidApp","org.company.app.AppActivity"))
            )
            .build()
        val pinnedCallBack = shortcutManager.createShortcutResultIntent(shortcutInfo)
        val successCallBack =
            PendingIntent.getBroadcast(context, 0, pinnedCallBack, PendingIntent.FLAG_IMMUTABLE)
        shortcutManager.requestPinShortcut(shortcutInfo, successCallBack.intentSender)
    }
}

private fun topSportsPinnedShortcut(context: Context) {
    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (shortcutManager.isRequestPinShortcutSupported) {
        val shortcutInfo = ShortcutInfo.Builder(context, "top_sports")
            .setShortLabel("Trending Sports")
            .setLongLabel("Trending Sports around the World")
            .setIcon(Icon.createWithResource(context, R.drawable.music_icon))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://linkedin.com/in/khubaibkhandev/")
                )
            )
            .build()
        val pinnedCallBack = shortcutManager.createShortcutResultIntent(shortcutInfo)
        val successCallBack = PendingIntent.getBroadcast(
            context,
            0,
            pinnedCallBack,
            PendingIntent.FLAG_IMMUTABLE
        )
        shortcutManager.requestPinShortcut(shortcutInfo, successCallBack.intentSender)
    }

}

//PinnedShortcut
private fun topMusicPinnedShortcut(context: Context) {
    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (shortcutManager.isRequestPinShortcutSupported) {
        val pinShortCutInfo = ShortcutInfo.Builder(context, "pin")
            .setShortLabel("Top Music")
            .setLongLabel("Top Music Videos from The Trending")
            .setIcon(Icon.createWithResource(context, R.drawable.music_icon))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/khubaibkhan4/")
                )
            )
            .build()
        val pinnedShortCallback = shortcutManager.createShortcutResultIntent(pinShortCutInfo)
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            pinnedShortCallback,
            PendingIntent.FLAG_IMMUTABLE
        )
        shortcutManager.requestPinShortcut(pinShortCutInfo, successCallback.intentSender)
    }
}

//Dynamic Shortcut
private fun topTrendingVideosDynamicShortcut(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    val shortcut = ShortcutInfoCompat.Builder(context, "topVideos")
        .setIcon(IconCompat.createWithResource(context, R.drawable.music_icon))
        .setShortLabel("Top Trending News")
        .setLongLabel("Top Trending Videos From Different Regions")
        .setIntent(
          Intent(Intent.ACTION_VIEW)
              .setPackage("org.company.app.androidApp")
        )
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}

@Composable
internal actual fun provideShortCuts() {
    val context = LocalContext.current
    topTrendingVideosDynamicShortcut(context)
    topMusicPinnedShortcut(context)
    topSportsPinnedShortcut(context)
    topNewsPinnedShortcut(context)

}

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String?, thumbnail: String?) {
    YoutubeVideoPlayer(youtubeURL = url)
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
internal actual fun ShortsVideoPlayer(url: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-100).dp),
        contentAlignment = Alignment.Center,
    ) {
        YoutubeShortsPlayer(youtubeURL = url)
    }
}

@Composable
internal actual fun Notify(message: String) {
    val coroutineContext = LocalContext.current
    Toast.makeText(coroutineContext, message, Toast.LENGTH_LONG).show()
}

@Composable
internal actual fun ShareManager(title: String, videoUrl: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, "$title: $videoUrl")
        type = "text/plain"
    }
    LocalContext.current.startActivity(Intent.createChooser(shareIntent, "Share Video"))
}

internal actual fun UserRegion(): String {
    val currentLocale: Locale = Locale.getDefault()
    return currentLocale.country
}

@Composable
actual fun isConnected(): Boolean {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected == true
    }
}