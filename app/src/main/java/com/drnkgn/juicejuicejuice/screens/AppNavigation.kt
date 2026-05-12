package com.drnkgn.juicejuicejuice.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.drnkgn.juicejuicejuice.screens.transactions.newTransaction.NewTransactionScreen
import com.drnkgn.juicejuicejuice.screens.overview.OverviewScreen
import com.drnkgn.juicejuicejuice.screens.settings.SettingsScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.TagSettingsScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.editTag.EditTagScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.newTag.NewTagScreen
import com.drnkgn.juicejuicejuice.screens.transactions.editTransaction.EditTransactionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "main",
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it / 4 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(tween(400))
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { -it / 4 },
                animationSpec = tween(400)
            ) + fadeOut(tween(200))
        },
        popEnterTransition = {
            slideInVertically(
                initialOffsetY = { -it / 4 },
                animationSpec = tween(400)
            ) + fadeIn(tween(400))
        },
        popExitTransition = {
            slideOutVertically(
                targetOffsetY = { it / 4 },
                animationSpec = tween(400)
            ) + fadeOut(tween(400))
        }
    ) {
        composable("main") { OverviewScreen(navController) }

        composable("transactions/new") { NewTransactionScreen(navController) }
        composable(
            route = "transactions/edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            EditTransactionScreen(navController, transactionId = backStackEntry.arguments?.getInt("id"))
        }

        composable("settings") { SettingsScreen(navController) }
        composable("settings/tags") { TagSettingsScreen(navController) }
        composable("settings/tags/new") {
            NewTagScreen(navController)
        }
        composable(
            route = "settings/tags/edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            EditTagScreen(navController, tagId = backStackEntry.arguments?.getInt("id"))
        }
    }
}