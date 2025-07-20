package com.omasba.clairaud.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.R
import com.omasba.clairaud.navigation.AppNavHost
import com.omasba.clairaud.navigation.BottomNavItem
import com.omasba.clairaud.presentation.home.component.AppNavigationBar
import com.omasba.clairaud.presentation.home.component.TopBar
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel


@OptIn(ExperimentalMaterial3Api::class)

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
        ){
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
fun MainScreenPreview(){
    MainScreen()
}