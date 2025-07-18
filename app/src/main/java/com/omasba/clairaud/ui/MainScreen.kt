package com.omasba.clairaud.ui

import UserViewModel
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
import com.omasba.clairaud.repos.StoreRepo
import com.omasba.clairaud.state.BottomNavItem
import com.omasba.clairaud.state.EqPreset
import com.omasba.clairaud.ui.models.AddPresetViewModel
import com.omasba.clairaud.ui.models.AuthViewModel
import com.omasba.clairaud.ui.models.EqualizerViewModel
import com.omasba.clairaud.ui.models.PresetComparisonViewModel
import com.omasba.clairaud.ui.models.StoreViewModel


/**
 * Main screen composable function: it renders main components of the screen, such as the navigation bar
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val storeViewModel = StoreViewModel()
    val equalizerViewModel = EqualizerViewModel()
    val pcViewModel = PresetComparisonViewModel()
    val bands = arrayListOf(
        Pair<Int,Short>(0,0),
        Pair<Int,Short>(1,0),
        Pair<Int,Short>(2,0),
        Pair<Int,Short>(3,0),
        Pair<Int,Short>(4,0))
    equalizerViewModel.newBands(bands)
    val addPresetViewModel = AddPresetViewModel()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Store,
        BottomNavItem.Profile
    )
    val presets by StoreRepo.presets.collectAsState()
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
//            val presets by StoreRepo.presets.collectAsState()
            composable(BottomNavItem.Home.route) { EqScreen(eqViewModel = equalizerViewModel, storeViewModel, navController = navController) }
            composable(BottomNavItem.Store.route) { StoreScreen(viewModel = storeViewModel, navController = navController) }
//            composable(BottomNavItem.Profile.route) { ProfileScreen(viewModel = UserViewModel(), navController = navController) }
            composable(BottomNavItem.Profile.route) { LoginScreen(viewModel = AuthViewModel()) }

//            composable(BottomNavItem.Profile.route) { Text("Profilo") }
            //add preset
            composable("addPreset"){ AddPresetScreen(viewModel = addPresetViewModel, navController = navController) }
            composable("editPreset/{presetId}"){backStackEntry ->
                //nel caso in cui sto editando un preset, devo capire attraverso l'id che preset devo modificare
                //e aggiornare il viewModel di conseguenza
                val presetId = backStackEntry.arguments?.getString("presetId")?.toInt()
                Log.d("route", "id passato alla route: ${presetId}")
                val preset = presets.find { it.id == presetId }

                Log.d("route","Preset trovato: $preset")
                val editPresetViewModel = AddPresetViewModel()
                editPresetViewModel.changePreset(preset ?: EqPreset())
                AddPresetScreen(viewModel = editPresetViewModel, navController = navController)
            }
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