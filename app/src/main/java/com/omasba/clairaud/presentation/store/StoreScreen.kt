package com.omasba.clairaud.presentation.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.store.component.Store
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.theme.ClairaudTheme


/**
 * Store screen view, it renders all available presets and filters
 * @param viewModel Store screen view model
 * @param navController Nav host controller to be able to navigate around the screens
 */
@Composable
fun StoreScreen(viewModel: StoreViewModel, navController: NavHostController) {
    val presets by viewModel.presets.collectAsState()
    val presetsLoaded by viewModel.presetsLoaded.collectAsState()

    var isAuthenticated by remember { mutableStateOf<Boolean?>(null) } //per capire quando si è autenticati

    LaunchedEffect(Unit) {
        isAuthenticated =
            UserRepo.isLogged() //si verifica se l'utente è loggato e anche la validità del token
        if (isAuthenticated == false) {
            navController.navigate("notAuth") {
                popUpTo("store") { inclusive = true }
            }
        }
        else if (presets.isEmpty())
            viewModel.fetchPresets()
    }


    when (isAuthenticated) {
        null -> {
            // aspetto il risultato
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        true -> {
            // autenticazione verificata
            if(presetsLoaded){
                Store(viewModel, navController)
            }
            else{
                // aspetto il fetch
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        else -> {//niente, in questo caso viene fatto il redirect, ci pensa già in primo LaunchedEffect
        }

    }
}
