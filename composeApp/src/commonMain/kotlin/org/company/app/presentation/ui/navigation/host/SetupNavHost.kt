package org.company.app.presentation.ui.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            ScreenItems.DetailScreen.title + "/{videoId}/{videoTitle}/{videoDescription}/{videoThumbnail}/{videoChannelTitle}/{videoChannelThumbnail}/{videoDuration}/{videoPublishedAt}/{videoViewCount}/{videoLikeCount}/{videoCommentCount}/{isLinked}/{videoChannelSubs}/{customUrl}/{channelDes}/{channelId}",
            arguments = listOf<NamedNavArgument>(
                navArgument("videoId") {
                    type = NavType.StringType
                },
                navArgument("videoTitle") {
                    type = NavType.StringType
                },
                navArgument("videoDescription") {
                    type = NavType.StringType
                },
                navArgument("videoThumbnail") {
                    type = NavType.StringType
                },
                navArgument("videoChannelTitle") {
                    type = NavType.StringType
                },
                navArgument("videoChannelThumbnail") {
                    type = NavType.StringType
                },
                navArgument("videoDuration") {
                    type = NavType.StringType
                },
                navArgument("videoPublishedAt") {
                    type = NavType.StringType
                },
                navArgument("videoViewCount") {
                    type = NavType.StringType
                },
                navArgument("videoLikeCount") {
                    type = NavType.StringType
                },
                navArgument("videoCommentCount") {
                    type = NavType.StringType
                },
                navArgument("isLinked") {
                    type = NavType.BoolType
                },
                navArgument("videoChannelSubs") {
                    type = NavType.StringType
                },
                navArgument("customUrl") {
                    type = NavType.StringType
                },
                navArgument("channelDes") {
                    type = NavType.StringType
                },
                navArgument("channelId") {
                    type = NavType.StringType
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
                navController
            )
        }
        composable(
            ScreenItems.ChannelScreen.title + "/{channelId}/{channelTitle}/{channelLogo}/{isVerified}/{subscribers}/{videoCount}/{customUrl}/{channelDes}",
            arguments = listOf<NamedNavArgument>(
                navArgument("channelId") {
                    type = NavType.StringType
                },
                navArgument("channelTitle") {
                    type = NavType.StringType
                },
                navArgument("channelLogo") {
                    type = NavType.StringType
                },
                navArgument("isVerified") {
                    type = NavType.BoolType
                },
                navArgument("subscribers") {
                    type = NavType.StringType
                },
                navArgument("videoCount") {
                    type = NavType.StringType
                },
                navArgument("customUrl") {
                    type = NavType.StringType
                },
                navArgument("channelDes") {
                    type = NavType.StringType
                },

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

            ChannelScreen(channelId,channelTitle,channelLogo,isVerified,subscribers,videoCount,customUrl,channelDes,navController)
        }
    }
}