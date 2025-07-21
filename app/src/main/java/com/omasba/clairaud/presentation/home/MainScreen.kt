package com.omasba.clairaud.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.navigation.AppNavHost
import com.omasba.clairaud.presentation.home.component.AppNavigationBar
import com.omasba.clairaud.presentation.home.component.TopBar
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val equalizerViewModel = EqualizerViewModel()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), // aggiunge padding per la status bar
        bottomBar = {
            AppNavigationBar(navController)
        },
        topBar = {
            TopBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavHost(
                navController,
                equalizerViewModel,
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun MainScreenPreview() {
    MainScreen()
}