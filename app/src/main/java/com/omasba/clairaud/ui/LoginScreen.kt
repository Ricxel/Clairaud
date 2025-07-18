package com.omasba.clairaud.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.ui.models.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel){

    val uiState by viewModel.uiState.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        //casella per username
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = {Text("Email")}
        )

        //casella per password
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = {Text("Password")}
        )

        //bottone login
        Button(
            onClick = {
                viewModel.register()
            },
            enabled = !uiState.isLoading
        ) {
            Text("Login")
        }

        if(uiState.isLoggedIn) {
            Text("Loggato!")
        }
        uiState.error?.let {
            Text("Errore: $it", color = Color.Red)
        }


    }
}