package cz.ash.mobilniapplikace.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.ash.mobilniapplikace.ui.screens.DashboardScreen
import cz.ash.mobilniapplikace.ui.screens.DetailScreen
import cz.ash.mobilniapplikace.ui.screens.ExploreScreen
import cz.ash.mobilniapplikace.ui.screens.ProfileScreen
import cz.ash.mobilniapplikace.ui.viewmodel.AuthViewModel

@Composable
fun MainScaffold(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    androidx.compose.material3.Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.Dashboard } == true,
                    onClick = { navController.navigate(Routes.Dashboard) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { androidx.compose.material3.Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.Explore } == true,
                    onClick = { navController.navigate(Routes.Explore) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                    label = { androidx.compose.material3.Text("Explore") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.Profile } == true,
                    onClick = { navController.navigate(Routes.Profile) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { androidx.compose.material3.Text("Profile") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Explore,
        ) {
            composable(Routes.Dashboard) {
                DashboardScreen(onOpenDetail = { id -> navController.navigate(Routes.detail(id)) })
            }
            composable(Routes.Explore) {
                ExploreScreen(
                    onOpenDetail = { id -> navController.navigate(Routes.detail(id)) },
                    onOpenFavorites = { navController.navigate(Routes.Dashboard) }
                )
            }
            composable(Routes.Profile) {
                ProfileScreen(authViewModel = authViewModel)
            }
            composable(Routes.Detail) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                if (!id.isNullOrBlank()) {
                    DetailScreen(coinId = id, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

