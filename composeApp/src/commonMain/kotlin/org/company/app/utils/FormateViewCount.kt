package org.company.app.utils

import androidx.compose.runtime.Composable

@Composable
fun formatViewCount(viewCount: String): String {
    val count = viewCount.toLong()

    return when {
        count < 1_000 -> "$count"
        count < 1_000_000 -> "${count / 1_000}k"
        count < 1_000_000_000 -> "${count / 1_000_000}M"
        else -> "${count / 1_000_000_000}B"
    }
}