package com.omasba.clairaud.presentation.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.omasba.clairaud.presentation.store.component.Store
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.theme.ClairaudTheme


/**
 * Store screen view, it renders all available presets and filters
 * @param viewModel Store screen view model
 * @param navController Nav host controller to be able to navigate around the screens
 */
@Composable
fun StoreScreen(viewModel: StoreViewModel, navController: NavHostController){
    val presets by viewModel.presets.collectAsState()
    LaunchedEffect(Unit) {
        //if(presets.isEmpty())
        viewModel.fetchPresets()
    }

    if(presets.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Store(viewModel, navController)
            }
        }
    }

}

@Composable
@Preview(
    showBackground = true
)
fun StoreScreenPreview(){
    ClairaudTheme {
        val viewModel = StoreViewModel()
        StoreScreen(viewModel = viewModel, navController = NavHostController(context = LocalContext.current))
    }
}