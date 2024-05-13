package org.company.app.presentation.ui.screens.detail

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.company.app.domain.model.channel.Item
import org.company.app.presentation.ui.components.detail.DetailContent

class DetailScreen(
    private val video: org.company.app.domain.model.videos.Item? = null,
    private val search: org.company.app.domain.model.search.Item? = null,
    private val channelData: Item? = null,
    private val logo: String? = null,
) : Screen {

    @Composable
    override fun Content() {
        DetailContent(video, search, channelData,logo)
    }
}


fun formatViewCount(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatViewComments(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatLikes(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun formatSubscribers(count: String?): String {
    return count?.toDoubleOrNull()?.let {
        when {
            it >= 1_000_000_000 -> "${(it / 1_000_000_000).toInt()}B"
            it >= 1_000_000 -> "${(it / 1_000_000).toInt()}M"
            it >= 1_000 -> "${(it / 1_000).toInt()}K"
            else -> "$it"
        }
    } ?: "0"
}

fun getFormattedDate(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentInstant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())

        val seconds = currentInstant.epochSeconds - instant.epochSeconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    } catch (e: Throwable) {
        "Unknown date"
    }
}

fun getFormattedDateLikeMonthDay(videoPublishedAt: String): Triple<String, Int, Int> {
    return try {
        val instant = Instant.parse(videoPublishedAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        val formattedMonth = months[localDateTime.monthNumber - 1]
        val dayOfMonth = localDateTime.dayOfMonth
        val year = localDateTime.year

        Triple(formattedMonth, dayOfMonth, year)
    } catch (e: Throwable) {
        Triple("Unknown", 0, 0)
    }
}

