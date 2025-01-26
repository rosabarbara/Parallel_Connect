package com.example.parallel_connect.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parallel_connect.viewModels.JeffChatViewModel
import com.example.parallel_connect.viewModels.MartinChatViewModel
import com.example.parallel_connect.viewModels.SarahChatViewModel


@Composable
fun NavGraph(navController: NavHostController, viewModelJeff: JeffChatViewModel, viewModelSarah: SarahChatViewModel, viewModelMartin: MartinChatViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(route = Screens.Map.route) {
            mapScreen(navController = navController)
        }
        composable(route = Screens.MainMenu.route) {
            mainMenu(navController = navController)
        }
        composable(route = Screens.Registration.route) {
            RegistrationScreen(navController = navController)
        }
        composable(route = Screens.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screens.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screens.ConversationsList.route) {
            ConversationsListScreen(
                navController = navController
            )
        }
        composable(
            route = Screens.Jeff.route,
        ) {
            JeffChatScreen(
                navController = navController,
                viewModel = viewModelJeff
            )
        }
        composable(
            route = Screens.Sarah.route,
        ) {
            SarahChatScreen(
                navController = navController,
                viewModel = viewModelSarah
            )
        }
        composable(
            route = Screens.Martin.route,
        ) {
            MartinChatScreen(
                navController = navController,
                viewModel = viewModelMartin
            )
        }
        composable(route = Screens.ForumShoes.route) {
            ForumShoesScreen(navController = navController)
        }
        // Define the new route for the SpiritFoundScreen
        composable("spirit_found/{spiritName}") { backStackEntry ->
            val spiritName = backStackEntry.arguments?.getString("spiritName")
            SpiritFoundScreen(spiritName = spiritName, navController = navController)
        }
        composable(route = Screens.PhotoGallery.route) {
            PhotoGalleryScreen(navController = navController)
        }
    }
}



