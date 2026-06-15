package com.drnkgn.juicejuicejuice.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA0
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA30

enum class BottomBarMenus(
    val icon: ImageVector,
    val path: String,
    val onClick: (NavController) -> Unit
) {
    Home(
        Icons.Default.Home,
        "main",
        { nav ->
            nav.navigate("main") {
                popUpTo("main") { inclusive = true }
                launchSingleTop = true
            }
        }
    ),
    Analytics(
        Icons.Default.BarChart,
        "analytics",
        { nav ->
            nav.navigate("analytics") {
                popUpTo("main")
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    var isVisible by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(BottomBarMenus.Home) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    selected = when (currentRoute) {
        BottomBarMenus.Home.path -> {
            BottomBarMenus.Home
        }
        BottomBarMenus.Analytics.path -> {
            BottomBarMenus.Analytics
        }

        else -> { selected }
    }

    isVisible = when (currentRoute) {
        BottomBarMenus.Home.path,
        BottomBarMenus.Analytics.path -> { true }
        else -> { false }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceTonalA0
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        val modifier = Modifier
                            .background(
                                color = SurfaceTonalA30,
                                shape = RoundedCornerShape(12.dp)
                            )

                        BottomBarMenus.entries.map {
                            IconButton(
                                modifier = if (selected == it) modifier else Modifier,
                                onClick = {
                                    if (selected != it) {
                                        selected = it
                                        it.onClick(navController)
                                    }
                                }
                            ) {
                                Icon(it.icon, contentDescription = null)
                            }
                        }
                    }
                }
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { navController.navigate("transactions/new") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AppBottomBarPreview() {
    JuiceJuiceJuiceTheme {
        AppBottomBar(rememberNavController())
    }
}
