package com.example.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    data object CheckIn : Screen("check_in")
    data object Companion : Screen("companion")
    data object Journal : Screen("journal")
    data object Insights : Screen("insights")
    data object Regulation : Screen("regulation")
    data object Settings : Screen("settings")

    companion object Router {
        fun fromRoute(route: String?): Screen = when (route) {
            Auth.route -> Auth
            CheckIn.route -> CheckIn
            Companion.route -> Companion
            Journal.route -> Journal
            Insights.route -> Insights
            Regulation.route -> Regulation
            Settings.route -> Settings
            else -> Home
        }
    }
}
