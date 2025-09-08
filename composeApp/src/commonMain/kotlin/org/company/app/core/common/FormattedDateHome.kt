package org.company.app.core.common

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun getFormattedDateHome(publishedAt: String): String {
    return try {
        val instant = Instant.parse(publishedAt)
        val currentInstant = Clock.System.now()

        val duration: Duration = currentInstant - instant
        when {
            duration < 1.minutes -> "${duration.inWholeSeconds} seconds ago"
            duration < 1.hours -> "${duration.inWholeMinutes} minutes ago"
            duration < 1.days -> "${duration.inWholeHours} hours ago"
            else -> {
                val days = duration.inWholeDays
                when {
                    days < 7 -> "$days days ago"
                    days < 30 -> {
                        val weeks = (days / 7).toInt()
                        "$weeks weeks ago"
                    }

                    days < 365 -> {
                        val months = (days / 30).toInt()
                        "$months months ago"
                    }

                    else -> {
                        val years = (days / 365).toInt()
                        "$years years ago"
                    }
                }
            }
        }
    } catch (e: Throwable) {
        "Unknown date"
    }
}