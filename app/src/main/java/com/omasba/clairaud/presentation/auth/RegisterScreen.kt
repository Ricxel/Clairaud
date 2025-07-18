package com.omasba.clairaud.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.presentation.auth.model.AuthViewModel

@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavHostController){

    val uiState by viewModel.uiState.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        //casella per email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = {Text("Email", color = MaterialTheme.colorScheme.secondary)},
        )


        //casella per username
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = {Text("Username", color = MaterialTheme.colorScheme.secondary)},
        )

        //casella per password
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = {Text("Password", color = MaterialTheme.colorScheme.secondary)},
        )



        Row {
            //bottone registrazione
            Button(
                onClick = {
                    viewModel.register()
                },
                enabled = !uiState.isLoading
            ) {
                Text("Register")
            }

            //collegamento con il login
            TextButton(
                onClick = {
                    navController.navigate("login")
                }
            ) {
                Text("Esegui il login")
            }
        }
        if(uiState.isLoggedIn) {
            Text("Loggato!", color = Color.Green)
        }
        uiState.error?.let {
            Text("Errore: $it", color = Color.Red)
        }


    }
}