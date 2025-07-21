package com.omasba.clairaud.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NotAuthenticated(navController: NavHostController) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = "You are not autheticated",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //bottone per il login
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("notAuth") {
                        inclusive = true
                    } //tolgo dallo stack per eveitare di tornare indietro
                }
            }
        ) {
            Text("Login")
        }
        Spacer(
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Text("or")

        //bottone per la registrazione
        TextButton(
            onClick = {
                navController.navigate("register") {
                    popUpTo("notAuth") {
                        inclusive = true
                    } //tolgo dallo stack per eveitare di tornare indietro
                }
            }
        ) {
            Text(text = "register here", color = MaterialTheme.colorScheme.secondary)
        }
    }
}