package com.omasba.clairaud.ui

import UserViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.ui.components.BottomNavItem
import com.omasba.clairaud.ui.models.AddPresetViewModel
import com.omasba.clairaud.ui.models.StoreViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val storeViewModel = StoreViewModel()
    val addPresetViewModel = AddPresetViewModel()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Store,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        colors = NavigationBarItemColors(
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { EqScreen(navController = navController) }
            composable(BottomNavItem.Store.route) { StoreScreen(viewModel = storeViewModel, navController = navController) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(viewModel = UserViewModel(), navController = navController) }
            composable(BottomNavItem.Profile.route) { Text("Profilo") }
            //add preset
            composable("addPreset"){ AddPresetScreen(viewModel = addPresetViewModel, navController = navController) }
        }
    }
}
@Composable
@Preview(
    showBackground = true
)
fun MainScreenPreview(){
    MainScreen()
}