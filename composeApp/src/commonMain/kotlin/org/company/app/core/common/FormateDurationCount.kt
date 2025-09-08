package org.company.app.core.common

fun formatVideoDuration(duration: String?): String {
    if (duration == null) return "00:00"

    val regex = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
    val matchResult = regex.find(duration)

    val hours = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: 0
    val minutes = matchResult?.groups?.get(2)?.value?.toIntOrNull() ?: 0
    val seconds = matchResult?.groups?.get(3)?.value?.toIntOrNull() ?: 0

    return buildString {
        if (hours > 0) {
            append("${if (hours < 10) "0" else ""}$hours:")
        }
        append("${if (minutes < 10) "0" else ""}$minutes:")
        append("${if (seconds < 10) "0" else ""}$seconds")
    }
}