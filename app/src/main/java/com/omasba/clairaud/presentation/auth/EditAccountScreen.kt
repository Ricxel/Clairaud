package com.omasba.clairaud.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.presentation.auth.components.CancelButton
import com.omasba.clairaud.presentation.auth.model.EditViewModel

@Composable
fun EditAccountScreen(viewModel: EditViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material.Text(
            "Edit profile",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        //username
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            label = { Text("Username", color = MaterialTheme.colorScheme.secondary) },
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp),
        ) {
            //bottone per tornare indietro
            CancelButton {
                navController.popBackStack()
            }

            //bottone per salvare
            Button(
                onClick = {
                    //va salvato il profilo
                    viewModel.updateUserProfile()
                },
                modifier = Modifier
                    .padding(start = 6.dp),
                enabled = !uiState.isLoading
            ) {
                Text("Save")
            }
        }

        //errore
        if (uiState.error != null)
            Text("Error: ${uiState.error}", color = Color.Red)
        if (uiState.isChanged)
            Text("Profile updated successfully!", color = Color.Green)

    }
}