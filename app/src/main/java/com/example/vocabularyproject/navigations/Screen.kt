package com.example.vocabularyproject.navigations

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object InputKata : Screen("input_kata")

    object DaftarKata : Screen("daftar_kata")
    object Permainan:Screen("permainan")
}
