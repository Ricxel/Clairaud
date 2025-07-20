package com.omasba.clairaud.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.auth.components.LogoutButton
import com.omasba.clairaud.presentation.auth.model.AuthViewModel
import com.omasba.clairaud.presentation.auth.state.UserProfile
import com.omasba.clairaud.presentation.component.NotAuthenticated

@Composable
fun ProfileScreen(viewModel: AuthViewModel, navController: NavHostController) {

    //controllo, se dovesse sloggarsi, lo rimando alla schermata di login
    var isAuthenticated by remember { mutableStateOf<Boolean?>(null) } //per capire quando si è autenticati

    LaunchedEffect(Unit) {
        isAuthenticated =
            UserRepo.isLogged() //si verifica se l'utente è loggato e anche la validità del token
        if (isAuthenticated == false) {
            navController.navigate("login") {
                popUpTo("store") { inclusive = true }
            }
        }
    }

    when(isAuthenticated){
        true -> {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileHeader(UserRepo.currentUserProfile)
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = {
                            navController.navigate("editProfile") {
                                // Passa i dati correnti come argomenti
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(end = 6.dp),
                    ) {
                        Text("Modifica Profilo")
                    }
                    LogoutButton {
                        viewModel.logout()
                        navController.navigate("login")
                    }
                }
            }
        }
        null -> {
            // aspetto il risultato
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        false -> {
            // niente
        }
    }


}

@Composable
fun ProfileHeader(userProfile: UserProfile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // immagine del profilo con iniziale
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userProfile.username.take(1).uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userProfile.username,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = userProfile.mail,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
