package com.example.vocabularyproject.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.vocabularyproject.navigations.Screen
import com.example.vocabularyproject.viewmodels.GameViewModel


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onInputKataClick = { navController.navigate(Screen.InputKata.route) },
                onDaftarKataClick = { navController.navigate(Screen.DaftarKata.route) },
                onPermainanClick = { navController.navigate("permainan_graph") },
                onSettingClick = { navController.navigate(Screen.Setting.route) }
            )
        }

        composable(Screen.InputKata.route) {
            InputKataScreen(
                onDaftarKataClick = { navController.navigate(Screen.DaftarKata.route) }
            )
        }

        composable(Screen.DaftarKata.route) {
            DaftarKataScreen()
        }

        composable(Screen.Setting.route) {
            SettingScreen()
        }

        // Nested graph — GameViewModel is scoped here and shared between both screens
        navigation(
            startDestination = Screen.PermainanSetting.route,
            route = "permainan_graph"
        ) {
            composable(Screen.PermainanSetting.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("permainan_graph")
                }
                val gameViewModel: GameViewModel = hiltViewModel(parentEntry)

                PermainanSettingScreen(
                    onMulaiPermainanClick = { navController.navigate(Screen.Permainan.route) },
                    gViewModel = gameViewModel
                )
            }

            composable(Screen.Permainan.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("permainan_graph")
                }
                val gameViewModel: GameViewModel = hiltViewModel(parentEntry)

                PermainanScreen(
                    gViewModel = gameViewModel
                )
            }
        }
    }
}