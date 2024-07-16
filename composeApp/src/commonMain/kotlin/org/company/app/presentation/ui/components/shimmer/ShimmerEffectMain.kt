package org.company.app.presentation.ui.components.shimmer

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.company.app.theme.LocalThemeIsDark
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ShimmerEffectMain() {
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
    ShimmerItem(brush)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimmerItem(
    brush: Brush
) {
    val isDark by LocalThemeIsDark.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBar(
            title = {
                Spacer(modifier = Modifier.width(6.dp))
                Spacer(
                    modifier = Modifier
                        .size(width = 120.dp, height = 40.dp)
                        .clip(
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(brush = brush)
                )
            },
            actions = {
                Spacer(modifier = Modifier.width(6.dp))
                Spacer(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(brush = brush)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Spacer(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(brush = brush)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Spacer(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(brush = brush)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Spacer(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(14.dp))
                        .background(brush = brush)
                )
                Spacer(modifier = Modifier.width(6.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarColors(
                containerColor = if (isDark) Color(0xFF202020) else Color.White,
                titleContentColor = if (isDark) Color.White else Color.Black,
                navigationIconContentColor = if (isDark) Color.White else Color.Black,
                actionIconContentColor = if (isDark) Color.White else Color.Black,
                scrolledContainerColor = if (isDark) Color.White else Color.Black,
            )
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            item {
                repeat(20) {
                    Spacer(
                        modifier = Modifier.width(45.dp)
                            .height(45.dp)
                            .clip(shape = RoundedCornerShape(14.dp))
                            .background(brush = brush)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp)
        ) {
            repeat(20) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =if (isDark) Color(0xFF202020) else Color.White
                        )
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {

                                Spacer(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(200.dp)
                                        .clip(
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(brush = brush)
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .background(MaterialTheme.colorScheme.primary)
                                        .clip(RoundedCornerShape(4.dp))
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .background(brush = brush)
                                    )
                                }
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(brush = brush)
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Spacer(
                                        modifier = Modifier.fillMaxWidth(0.8f)
                                            .height(18.dp)
                                            .clip(shape = RoundedCornerShape(12.dp))
                                            .background(brush = brush)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Spacer(
                                                modifier = Modifier
                                                    .width(35.dp)
                                                    .height(20.dp)
                                                    .clip(shape = RoundedCornerShape(12.dp))
                                                    .background(brush = brush)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Spacer(
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .padding(start = 4.dp, top = 4.dp)
                                                    .clip(shape = RoundedCornerShape(12.dp))
                                                    .background(brush = brush)
                                            )
                                        }
                                        Spacer(
                                            modifier = Modifier
                                                .size(15.dp)
                                                .clip(shape = CircleShape)
                                                .background(brush = brush)
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .size(25.dp)
                                                .clip(shape = CircleShape)
                                                .background(brush = brush)
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .size(15.dp)
                                                .clip(shape = CircleShape)
                                                .background(brush = brush)
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .width(25.dp)
                                                .height(10.dp)
                                                .clip(shape = CircleShape)
                                                .background(brush = brush)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Spacer(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}