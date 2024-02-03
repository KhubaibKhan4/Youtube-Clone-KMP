package org.company.app.widget

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

fun pinnedShortCut(context: Context){
    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        return
    }
    if (shortcutManager.isRequestPinShortcutSupported){
        val shortcutInfo = ShortcutInfo.Builder(context,"pinned")
            .setShortLabel("Voice Search")
            .setLongLabel("Voice Search using Handset Mic")
            .setIcon(Icon.createWithResource(context, R.drawable.news_app))
            .setIntent(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://linkedin.com/in/KhubaibKhanDev/"))
            )
            .build()
        val shortcutCallBack = shortcutManager.createShortcutResultIntent(shortcutInfo)
        val shortCutSuccessCallBack = PendingIntent.getBroadcast(
            context,
            0,
            shortcutCallBack,
            PendingIntent.FLAG_IMMUTABLE
        )
        shortcutManager.requestPinShortcut(shortcutInfo,shortCutSuccessCallBack.intentSender)
    }
}