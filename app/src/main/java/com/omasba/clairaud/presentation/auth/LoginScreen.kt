package com.omasba.clairaud.presentation.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.omasba.clairaud.data.repository.AuthRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.navigation.BottomNavItem
import com.omasba.clairaud.presentation.auth.components.CancelButton
import com.omasba.clairaud.presentation.auth.components.LogoutButton
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.auth.model.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavHostController){

    val uiState by viewModel.uiState.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }


    //controllo lo stato di autenticazione, se sono autenticato, mi sposto alla schermata di profilo
    LaunchedEffect(uiState.isLoggedIn) {
        Log.d("auth", "Changed")
        if(uiState.isLoggedIn)
            navController.navigate("profile") {
                popUpTo("login"){inclusive = true} // fa in modo che non si possa tornare a login
            }
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //testo
        Text("Login", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)

        //casella per mail
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email", color = MaterialTheme.colorScheme.secondary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
        )

        //casella per password
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password", color = MaterialTheme.colorScheme.secondary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                }
            },
        )

        //Riga con i pulsanti
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            //bottone per tornare indietro
            CancelButton {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.padding(start = 6.dp))

            //bottone login
            Button(
                onClick = {
                    viewModel.login()
                },
                enabled = !uiState.isLoading
            ) {
                Text("Login")
            }
        }
        // per per registrarsi
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
            //collegamento con la registrazione
            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text("Register", color = MaterialTheme.colorScheme.primary)
            }
        }

        //animazione di caricamento
        if(uiState.isLoading){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.error?.let {
            Text("Error: $it", color = Color.Red)
        }


    }
}