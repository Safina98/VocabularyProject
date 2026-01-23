package com.example.vocabularyproject.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vocabularyproject.navigations.Home
import com.example.vocabularyproject.navigations.InputKata
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
                }
            )
        }

        composable(Screen.InputKata.route) {
            InputKataScreen()
        }

//        composable(Screen.TebakKata.route) {
//            TebakKataScreen()
//        }
//
//        composable(Screen.DaftarKata.route) {
//            DaftarKataScreen()
//        }
    }
}
