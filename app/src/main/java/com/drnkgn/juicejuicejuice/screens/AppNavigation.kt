package com.drnkgn.juicejuicejuice.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.drnkgn.juicejuicejuice.BuildConfig
import com.drnkgn.juicejuicejuice.components.AppBottomBar
import com.drnkgn.juicejuicejuice.db.dto.OverviewStatsDTO
import com.drnkgn.juicejuicejuice.fakes.FakeTransactions
import com.drnkgn.juicejuicejuice.screens.analytics.AnalyticScreen
import com.drnkgn.juicejuicejuice.screens.overview.OverviewContent
import com.drnkgn.juicejuicejuice.screens.overview.OverviewContentMock
import com.drnkgn.juicejuicejuice.screens.overview.OverviewScreen
import com.drnkgn.juicejuicejuice.screens.settings.SettingsScreen
import com.drnkgn.juicejuicejuice.screens.settings.dbSettings.DBSettingsContent
import com.drnkgn.juicejuicejuice.screens.settings.dbSettings.DBSettingsScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.TagSettingsScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.editTag.EditTagScreen
import com.drnkgn.juicejuicejuice.screens.settings.tagSettings.newTag.NewTagScreen
import com.drnkgn.juicejuicejuice.screens.transactions.editTransaction.EditTransactionScreen
import com.drnkgn.juicejuicejuice.screens.transactions.newTransaction.NewTransactionScreen
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiState
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    AppNavigationContent(navController) {
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

            composable("analytics") { AnalyticScreen(navController) }

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
            
            composable("settings/database") { DBSettingsScreen() }
        }
    }
}

@Composable
fun AppNavigationContent(
    navController: NavController,
    children: @Composable () -> Unit
) {
    val isDebug = BuildConfig.DEBUG

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        children()
        if (isDebug) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(color = MaterialTheme.colorScheme.error),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Development Build", fontSize = 12.sp)
                }
            }
        }
        AppBottomBar(navController)
    }
}

@Preview
@Composable
fun AppNavigationPreview() {
    val navController = rememberNavController()

    JuiceJuiceJuiceTheme {
        AppNavigationContent(navController) {
            OverviewContentMock()
        }
    }
}
