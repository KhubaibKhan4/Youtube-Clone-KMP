package org.company.app.shortcuts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import org.company.app.R

fun dynamicShortcut(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        return
    }
    val shortcutInfoCompat = ShortcutInfoCompat.Builder(context, "dynamicShortcut")
        .setShortLabel("Text Search")
        .setLongLabel("Text Search using Handset Mic")
        .setIcon(IconCompat.createWithResource(context, R.drawable.news_app))
        .setIntent(
            Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://linkedin.com/in/KhubaibKhanDev/"))
        )
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfoCompat)
}