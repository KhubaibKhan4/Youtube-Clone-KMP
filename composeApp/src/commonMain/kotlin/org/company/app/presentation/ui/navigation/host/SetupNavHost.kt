package org.company.app.presentation.ui.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.company.app.presentation.ui.screens.channel_detail.ChannelDetail
import org.company.app.presentation.ui.screens.channel_screen.ChannelScreen
import org.company.app.presentation.ui.screens.detail.Detail
import org.company.app.presentation.ui.screens.home.HomeScreen
import org.company.app.presentation.ui.screens.library.LibraryScreen
import org.company.app.presentation.ui.screens.shorts.ShortContent
import org.company.app.presentation.ui.screens.subscriptions.SubscriptionScreen

@Composable
fun SetupNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = ScreenItems.Home.title) {
        composable(ScreenItems.Home.title) {
            HomeScreen(navController)
        }
        composable(ScreenItems.Shorts.title) {
            ShortContent()
        }
        composable(ScreenItems.Subscription.title) {
            SubscriptionScreen(navController)
        }
        composable(ScreenItems.Library.title) {
            LibraryScreen(navController)
        }
        composable(
            ScreenItems.DetailScreen.title + "/{videoId}/{videoTitle}/{videoDescription}/{videoThumbnail}/{videoChannelTitle}/{videoChannelThumbnail}/{videoDuration}/{videoPublishedAt}/{videoViewCount}/{videoLikeCount}/{videoCommentCount}/{isLinked}/{videoChannelSubs}/{customUrl}/{channelDes}/{channelId}/{channelCountry}/{topicDetails}/{channelViewCount}",
            arguments = listOf<NamedNavArgument>(
                navArgument("videoId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoTitle") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoDescription") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoThumbnail") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoChannelTitle") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoChannelThumbnail") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoDuration") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoPublishedAt") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoViewCount") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoLikeCount") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoCommentCount") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isLinked") {
                    type = NavType.BoolType
                },
                navArgument("videoChannelSubs") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("customUrl") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelDes") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelCountry") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("topicDetails") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelViewCount"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val videoId = it.arguments?.getString("videoId")
            val videoTitle = it.arguments?.getString("videoTitle")
            val videoDescription = it.arguments?.getString("videoDescription")
            val videoThumbnail = it.arguments?.getString("videoThumbnail")
            val videoChannelTitle = it.arguments?.getString("videoChannelTitle")
            val videoChannelThumbnail = it.arguments?.getString("videoChannelThumbnail")
            val videoDuration = it.arguments?.getString("videoDuration")
            val videoPublishedAt = it.arguments?.getString("videoPublishedAt")
            val videoViewCount = it.arguments?.getString("videoViewCount")
            val videoLikeCount = it.arguments?.getString("videoLikeCount")
            val videoCommentCount = it.arguments?.getString("videoCommentCount")
            val isLinked = it.arguments?.getBoolean("isLinked")
            val videoChannelSubs = it.arguments?.getString("videoChannelSubs")
            val customUrl = it.arguments?.getString("customUrl")
            val channelDes = it.arguments?.getString("channelDes")
            val channelId = it.arguments?.getString("channelId")
            val channelCountry = it.arguments?.getString("channelCountry")
            val topicDetails = it.arguments?.getString("topicDetails")
            val channelViewCount = it.arguments?.getString("channelViewCount")

            Detail(
                videoId,
                videoTitle,
                videoDescription,
                videoThumbnail,
                videoChannelTitle,
                videoChannelThumbnail,
                videoDuration,
                videoPublishedAt,
                videoViewCount,
                videoLikeCount,
                videoCommentCount,
                isLinked,
                videoChannelSubs,
                customUrl,
                channelDes,
                channelId,
                channelCountry,
                topicDetails,
                channelViewCount,
                navController
            )
        }
        composable(
            ScreenItems.ChannelScreen.title + "/{channelId}/{channelTitle}/{channelLogo}/{isVerified}/{subscribers}/{videoCount}/{customUrl}/{channelDes}/{channelCountry}/{topicDetails}/{channelViewCount}",
            arguments = listOf<NamedNavArgument>(
                navArgument("channelId") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelTitle") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelLogo") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isVerified") {
                    type = NavType.BoolType
                },
                navArgument("subscribers") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("videoCount") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("customUrl") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelDes") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelCountry") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("topicDetails") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelViewCount") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val channelId = it.arguments?.getString("channelId")
            val channelTitle = it.arguments?.getString("channelTitle")
            val channelLogo = it.arguments?.getString("channelLogo")
            val isVerified = it.arguments?.getBoolean("isVerified")
            val subscribers = it.arguments?.getString("subscribers")
            val videoCount = it.arguments?.getString("videoCount")
            val customUrl = it.arguments?.getString("customUrl")
            val channelDes = it.arguments?.getString("channelDes")
            val channelCountry = it.arguments?.getString("channelCountry")
            val topicDetails = it.arguments?.getString("topicDetails")
            val channelViewCount = it.arguments?.getString("channelViewCount")

            ChannelScreen(channelId,channelTitle,channelLogo,isVerified,subscribers,videoCount,customUrl,channelDes,channelCountry,topicDetails, channelViewCount,navController)
        }
        composable(
            ScreenItems.ChannelDetail.title + "/{channelTitle}/{channelDescription}/{customUrl}/{country}/{viewCount}/{isLinked}/{topicDetails}",
            arguments = listOf<NamedNavArgument>(
                navArgument("channelTitle") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("channelDescription") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("customUrl") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("country") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("viewCount") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("isLinked") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("topicDetails") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val channelTitle = it.arguments?.getString("channelTitle")
            val channelDescription = it.arguments?.getString("channelDescription")
            val customUrl = it.arguments?.getString("customUrl")
            val country = it.arguments?.getString("country")
            val viewCount = it.arguments?.getString("viewCount")
            val isLinked = it.arguments?.getBoolean("isLinked")
            val topicDetails = it.arguments?.getString("topicDetails")

            ChannelDetail(channelTitle,channelDescription,customUrl,country,viewCount,isLinked,topicDetails,navController)
        }
    }
}