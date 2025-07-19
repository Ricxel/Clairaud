package com.omasba.clairaud.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.NotAuthenticatedScreen
import com.omasba.clairaud.presentation.auth.EditAccountScreen
import com.omasba.clairaud.presentation.auth.LoginScreen
import com.omasba.clairaud.presentation.auth.ProfileScreen
import com.omasba.clairaud.presentation.auth.RegisterScreen
import com.omasba.clairaud.presentation.auth.model.AuthViewModel
import com.omasba.clairaud.presentation.auth.model.EditViewModel
import com.omasba.clairaud.presentation.home.AddPresetScreen
import com.omasba.clairaud.presentation.home.EqScreen
import com.omasba.clairaud.presentation.home.model.AddPresetViewModel
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import com.omasba.clairaud.presentation.home.model.PresetComparisonViewModel
import com.omasba.clairaud.presentation.store.StoreScreen
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.store.state.EqPreset

@Composable
fun AppNavHost(navController: NavHostController, equalizerViewModel: EqualizerViewModel, modifier: Modifier = Modifier) {

    val pcViewModel = PresetComparisonViewModel()

    val authViewModel = AuthViewModel()
    val storeViewModel = StoreViewModel()
    val addPresetViewModel = AddPresetViewModel()
    val presets by StoreRepo.presets.collectAsState()



    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = Modifier
            .padding(top = 32.dp),
        enterTransition = {
            fadeIn(tween(100))
        },
        exitTransition = {
            fadeOut(tween(100))
        }
    ) {
//            val presets by StoreRepo.presets.collectAsState()
        composable(BottomNavItem.Home.route) {
            EqScreen(eqViewModel = equalizerViewModel, storeViewModel, navController = navController)
        }
        composable(BottomNavItem.Store.route) {
            //per accedere allo store, è necessario essere loggati, controllo in viewmodel auth
            StoreScreen(viewModel = storeViewModel, navController = navController)
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(viewModel = authViewModel, navController = navController)
//            else {
//                //rimando al login
//                LaunchedEffect(Unit){
//                    navController.navigate("login")
//                }
//            }
        }
//        composable(BottomNavItem.Profile.route) { LoginScreen(viewModel = authViewModelLogin, navController) }

//            composable(BottomNavItem.Profile.route) { Text("Profilo") }
        //add preset
        composable("notAuth"){ NotAuthenticatedScreen(navController) }
        composable("addPreset"){ AddPresetScreen(viewModel = addPresetViewModel, navController = navController) }
        composable("register"){ RegisterScreen(viewModel = authViewModel, navController = navController) }
        composable("login"){ LoginScreen(viewModel = authViewModel, navController = navController) }
        composable("editProfile"){ EditAccountScreen(viewModel = EditViewModel(UserRepo.currentUserProfile.username, UserRepo.currentUserProfile.mail), navController = navController)}

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
