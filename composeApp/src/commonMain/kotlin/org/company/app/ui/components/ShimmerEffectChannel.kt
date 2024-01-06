package org.company.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ShimmerEffectChannel() {
    val shimmerColors = listOf(
        Color.LightGray.copy(0.6f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.6f),
    )

    val transition = rememberInfiniteTransition(label = "Transition For Shimmer Effect")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        ), label = "Animation"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    ShimmerItemChannel(brush)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ShimmerItemChannel(
    brush: Brush
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navigator = LocalNavigator.current
        // Custom Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Icon
            Spacer(
                modifier = Modifier
                    .size(35.dp)
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(brush = brush)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Spacer(
                modifier = Modifier
                    .width(25.dp)
                    .height(14.dp)
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(brush = brush)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Search Icon
            Spacer(
                modifier = Modifier
                    .size(35.dp)
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(brush = brush)
            )

            // More Vert Icon
            Spacer(
                modifier = Modifier
                    .size(35.dp)
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(brush = brush)
            )
        }


        // Channel Poster Image
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(start = 20.dp, end = 20.dp)
                .clip(shape = RoundedCornerShape(14.dp))
                .background(brush = brush)
        )

        // Channel Details
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Channel Image
            Spacer(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(brush = brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Channel Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .width(25.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush = brush)
                )
                Spacer(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(brush = brush)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Channel Subscribers and Videos
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(brush = brush)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Channel Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Description
                Spacer(
                    modifier = Modifier.weight(1f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush = brush)
                )

                // Arrow Icon
                Spacer(
                    modifier = Modifier.weight(1f)
                        .height(35.dp)
                        .clip(CircleShape)
                        .background(brush = brush)
                )
            }
        }


        // Channel Links
        Spacer(
            modifier = Modifier
                .width(25.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush = brush)
        )

        // Subscribe Button
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush = brush)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            LazyRow {
                repeat(20) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .width(25.dp)
                                .height(15.dp)
                                .padding(horizontal = 8.dp)
                                .background(brush = brush)
                        )
                    }
                }
            }
            //Data of Row Tabs
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier
                    .height(900.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                repeat(40) {
                    item {
                        Spacer(
                            modifier = Modifier.fillMaxWidth()
                                .width(140.dp)
                                .height(80.dp)
                                .clip(
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(brush = brush)
                        )
                    }
                }
            }
        }
    }
}