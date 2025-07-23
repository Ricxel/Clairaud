package com.omasba.clairaud.presentation.home.component

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.home.AutoEq
import com.omasba.clairaud.presentation.home.TAG
import com.omasba.clairaud.presentation.home.model.AutoEqViewModel
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel


@Composable
fun EqCard(viewModel: EqualizerViewModel, navController: NavHostController) {

    val eqState by viewModel.eqState.collectAsState()
    val isOn = eqState.isOn
    val eq by EqRepo.eq.collectAsState()
    val bands = eqState.bands
    Log.d(TAG, "bands: $bands.toString()")
    val autoEqModel = AutoEqViewModel()

    var isEnabled by remember { mutableStateOf(false) } // per capire se l'utente è autenticato
    LaunchedEffect(Unit) {
        isEnabled = UserRepo.isLogged() // per l'aggiunta di un preset
    }

    Log.d("EqScreen", "eq card $eq")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        val cardWidth = this.maxWidth - 32.dp

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // titolo e switch
                EqTitle(isEnabled, isOn, viewModel, navController)

                Spacer(modifier = Modifier.height(16.dp))

                EqSliderRow(eqState, bands, cardWidth, isOn, viewModel)

                Spacer(modifier = Modifier.height(24.dp))
                // AutoEQ
                AutoEq(autoEqModel)

            }
        }
    }
}