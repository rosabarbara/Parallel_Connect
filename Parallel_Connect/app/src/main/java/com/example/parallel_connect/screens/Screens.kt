package com.example.parallel_connect.screens


import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class Screens(val route :String) {
    object MainMenu : Screens("main_menu_screen")
    object Map : Screens("map_screen")
    object Login: Screens("login_screen")
    object Registration: Screens("registration_screen")
    object Profile: Screens("profile_screen")
    object ConversationsList : Screens("conversations_list")
    object Jeff : Screens("Jeff/{conversationName}") {
        val arguments = listOf(
            navArgument("conversationName") { type = NavType.StringType }
        )
    }
    object Martin : Screens("Martin/{conversationName}") {
        val arguments = listOf(
            navArgument("conversationName") { type = NavType.StringType }
        )
    }
    object Sarah : Screens("sarah/{conversationName}") {
        val arguments = listOf(
            navArgument("conversationName") { type = NavType.StringType }
        )
    }
    object ForumShoes : Screens("forum_shoes_screen")
    object PhotoGallery : Screens("photo_gallery")
}