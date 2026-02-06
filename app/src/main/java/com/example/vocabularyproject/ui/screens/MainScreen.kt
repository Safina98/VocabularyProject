package com.example.vocabularyproject.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vocabularyproject.navigations.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onInputKataClick = {
                    navController.navigate(Screen.InputKata.route)
                },
                onDaftarKataClick = {
                    navController.navigate(Screen.DaftarKata.route)
                },
                onPermainanClick = {
                    navController.navigate(Screen.Permainan.route)
                }
            )
        }

        composable(Screen.InputKata.route) {
            InputKataScreen(
                onDaftarKataClick = {
                    navController.navigate(Screen.DaftarKata.route)
                }
            )
        }
        composable(Screen.DaftarKata.route) {
            DaftarKataScreen(

            )
        }
        composable (Screen.Permainan.route){
            PermainanScreen()
        }
        // Add more composables for other screens if needed)

    }
}
