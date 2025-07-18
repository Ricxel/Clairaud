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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.auth.model.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavHostController){

    val uiState by viewModel.uiState.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        //casella per username
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = {Text("Email", color = MaterialTheme.colorScheme.secondary)},
            textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
        )

        //casella per password
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = {Text("Password", color = MaterialTheme.colorScheme.secondary)},
        )

        Row {
            //bottone login
            Button(
                onClick = {
                    viewModel.login()
                },
                enabled = !uiState.isLoading
            ) {
                Text("Login")
            }

            //collegamento con la registrazione
            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text("Registrati")
            }
        }


        if(uiState.isLoggedIn) {
            Text("Loggato!", color = Color.Green)

            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid ?: ""

            LaunchedEffect(uid) {
                val userProfile = AuthRepo.getUserProfile(uid).getOrNull() ?: UserProfile(uid = uid, username = "prova", mail = "skibidi")
                UserRepo.currentUserProfile = userProfile
            }
            Text("Profilo: \n${UserRepo.currentUserProfile}", color = MaterialTheme.colorScheme.secondary)
        }


        uiState.error?.let {
            Text("Errore: $it", color = Color.Red)
        }


    }
}