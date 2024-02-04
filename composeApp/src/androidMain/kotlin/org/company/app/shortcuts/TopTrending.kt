package org.company.app.shortcuts

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.core.content.getSystemService
import org.company.app.R

fun TopTrending(
    context: Context,
) {
    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (shortcutManager.isRequestPinShortcutSupported) {
        val shortcutInfo = ShortcutInfo.Builder(context, "topTrending")
            .setIcon(Icon.createWithResource(context, R.drawable.news_app))
            .setShortLabel("Trending Videos")
            .setLongLabel("Top Trending Videos")
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://youtube.com/trending")
                )
            )
            .setRank(2)
            .build()
        val shortcutCallback = shortcutManager.createShortcutResultIntent(shortcutInfo)
        val shortCutSuccessCallBack =
            PendingIntent.getBroadcast(context, 1, shortcutCallback, PendingIntent.FLAG_IMMUTABLE)
        shortcutManager.requestPinShortcut(shortcutInfo,shortCutSuccessCallBack.intentSender)
    }

}