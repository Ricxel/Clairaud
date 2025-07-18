package com.omasba.clairaud.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.navigation.AppNavHost
import com.omasba.clairaud.presentation.auth.LoginScreen
import com.omasba.clairaud.presentation.auth.RegisterScreen
import com.omasba.clairaud.presentation.store.StoreScreen
import com.omasba.clairaud.navigation.BottomNavItem
import com.omasba.clairaud.presentation.store.state.EqPreset
import com.omasba.clairaud.presentation.NotAuthenticatedScreen
import com.omasba.clairaud.presentation.home.model.AddPresetViewModel
import com.omasba.clairaud.presentation.auth.model.AuthViewModel
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import com.omasba.clairaud.presentation.home.model.PresetComparisonViewModel
import com.omasba.clairaud.presentation.store.model.StoreViewModel


/**
 * Main screen composable function: it renders main components of the screen, such as the navigation bar
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val equalizerViewModel = EqualizerViewModel()

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
        AppNavHost(
            navController,
            equalizerViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
@Composable
@Preview(
    showBackground = true
)
fun MainScreenPreview(){
    MainScreen()
}