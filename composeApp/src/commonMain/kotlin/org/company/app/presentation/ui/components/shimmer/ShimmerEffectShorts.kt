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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffectShorts() {
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
    ShimmerItemShorts(brush)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShimmerItemShorts(
    brush: Brush
) {
    LazyColumn(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)){
        repeat(20){
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().background(color = Color.Black)
                        .padding(top = 10.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Spacer(
                        modifier =Modifier.fillMaxWidth()
                            .height(300.dp)
                            .padding(8.dp)
                            .align(alignment = Alignment.Center)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .background(brush = brush)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* IconButton(
                                 onClick = {

                                 },
                             ) {
                                 Icon(
                                     imageVector = Icons.Default.ArrowBack,
                                     contentDescription = "Arrow Back",
                                     tint = Color.White
                                 )
                             }*/

                            Spacer(modifier = Modifier.weight(1f))

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
                        }
                        Column(
                            modifier = Modifier.fillMaxHeight()
                                .padding(end = 6.dp, top = 335.dp, bottom = 49.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End
                        ) {
                            //Like Button
                            Column(
                                modifier = Modifier.wrapContentWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(10.dp)
                                        .clip(shape = CircleShape)
                                        .background(brush= brush)
                                )
                            }

                            //DisLike Button
                            Column(
                                modifier = Modifier.wrapContentWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(10.dp)
                                        .clip(shape = CircleShape)
                                        .background(brush= brush)
                                )
                            }

                            //Comments Button
                            Column(
                                modifier = Modifier.wrapContentWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(10.dp)
                                        .clip(shape = CircleShape)
                                        .background(brush= brush)
                                )
                            }

                            //Share Button
                            Column(
                                modifier = Modifier.wrapContentWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(10.dp)
                                        .clip(shape = CircleShape)
                                        .background(brush= brush)
                                )
                            }

                            //Remix Button
                            Column(
                                modifier = Modifier.wrapContentWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(10.dp)
                                        .height(10.dp)
                                        .clip(shape = CircleShape)
                                        .background(brush= brush)
                                )
                            }

                            Column(
                                modifier = Modifier.wrapContentWidth()
                                    .padding(top = 12.dp, end = 2.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .background(brush = brush)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 4.dp)
                                .offset(y = 35.dp)
                            ,
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Circle
                            Spacer(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(shape = CircleShape)
                                    .background(brush = brush)
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            // Nested Row with three components
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // First Box
                                Spacer(
                                    modifier = Modifier
                                        .width(35.dp)
                                        .height(20.dp)
                                        .clip(shape = RoundedCornerShape(12.dp))
                                        .background(brush = brush)
                                )
                                Spacer(modifier = Modifier.size(15.dp))

                                // Second Box
                                Spacer(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .height(25.dp)
                                        .clip(shape = RoundedCornerShape(6.dp))
                                        .background(brush = brush)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 18.dp, end = 8.dp, top = 55.dp)
                                .offset(y = (-20).dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // FlowRow
                            Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .width(80.dp)
                                        .height(18.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(brush = brush)
                            )
                            Spacer(modifier = Modifier.size(15.dp))
                            Spacer(
                                modifier = Modifier
                                    .size(35.dp)
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