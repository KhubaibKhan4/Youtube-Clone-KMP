package org.company.app.presentation.ui.components.relevance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.company.app.domain.model.videos.Youtube
import org.company.app.domain.usecases.ResultState
import org.company.app.presentation.ui.components.common.ErrorBox
import org.company.app.presentation.ui.components.shimmer.ShimmerEffectSingleVideo
import org.company.app.presentation.ui.components.video_list.VideoItemCard
import org.company.app.theme.LocalThemeIsDark

@Composable
fun RelevanceList(
    stateRelevance: ResultState<Youtube>,
) {
    val isDark by LocalThemeIsDark.current
    var relevanceData by remember { mutableStateOf<Youtube?>(null) }

    when (stateRelevance) {
        is ResultState.LOADING -> {
           ShimmerEffectSingleVideo()
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
        color = if (isDark) Color(0xFF202020) else Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier.height(700.dp)
            ) {
                relevanceData?.items?.let { items ->
                    items(items) { videos ->
                        VideoItemCard(videos)
                    }
                }
            }
        }
    }
}