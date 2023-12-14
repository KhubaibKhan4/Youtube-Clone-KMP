package org.company.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.company.app.data.model.videos.Youtube

@Composable
fun RelevanceList(youtube: Youtube) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                items(youtube.items) { videos ->
                    VideoItemCard(videos)
                }
            }
        }
    }
}