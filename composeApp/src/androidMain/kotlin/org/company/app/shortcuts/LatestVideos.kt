package org.company.app.shortcuts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import org.company.app.R

fun latestVideos(context: Context){
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        return
    }
    val shortcutInfoCompat = ShortcutInfoCompat.Builder(context,"latestVideos")
        .setIcon(IconCompat.createWithResource(context, R.drawable.video))
        .setShortLabel("Latest Videos")
        .setLongLabel("Latest Uploaded Videos")
        .setIntent(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://youtube.com/latest/")
            )
        )
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(context,shortcutInfoCompat)
}