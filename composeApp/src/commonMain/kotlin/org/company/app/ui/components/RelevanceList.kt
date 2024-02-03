package org.company.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.company.app.Res
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState

@Composable
fun RelevanceList(stateRelevance: ResultState<org.company.app.domain.model.videos.Youtube>) {
    var relevanceData by remember { mutableStateOf<org.company.app.domain.model.videos.Youtube?>(null) }

    when (stateRelevance) {
        is ResultState.LOADING -> {
            LoadingBox()
        }

        is ResultState.SUCCESS -> {
            val data = (stateRelevance as ResultState.SUCCESS).response
            relevanceData = data

        }

        is ResultState.ERROR -> {
            val error = (stateRelevance as ResultState.ERROR).error
            ErrorBox(error)
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(columns = GridCells.Adaptive(300.dp), modifier = Modifier.height(700.dp)) {
                relevanceData?.items?.let {items ->
                    items(items) { videos ->
                        VideoItemCard(videos)
                    }
                }
            }
        }
    }
}