package com.omasba.clairaud.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.ui.components.store.Store
import com.omasba.clairaud.ui.models.StoreViewModel

@Composable
fun StoreScreen(viewModel: StoreViewModel, navController: NavHostController){
    val presets by viewModel.presets.collectAsState()
    LaunchedEffect(Unit) {
        if(presets.isEmpty())
            StoreRepo.fetchPresets()
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
                Store(viewModel = viewModel)
            }
        }
    }

}

@Composable
@Preview(
    showBackground = true
)
fun StoreScreenPreview(){
    val viewModel = StoreViewModel()
    StoreScreen(viewModel = viewModel, navController = rememberNavController())
}