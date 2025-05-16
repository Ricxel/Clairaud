package com.omasba.clairaud.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.ui.components.BottomNavItem
import com.omasba.clairaud.ui.components.store.FloatingButton
import com.omasba.clairaud.ui.components.store.SearchablePresetList
import com.omasba.clairaud.ui.models.PresetListViewModel

@Composable
fun StoreScreen(viewModel: PresetListViewModel, navController: NavHostController){
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchablePresetList(viewModel = viewModel)
        }
        FloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            navController.navigate("addPreset")
        }
    }

}

@Composable
@Preview(
    showBackground = true
)
fun StoreScreenPreview(){
    val viewModel = PresetListViewModel()
    StoreScreen(viewModel = viewModel, navController = rememberNavController())
}