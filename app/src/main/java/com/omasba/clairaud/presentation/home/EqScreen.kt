package com.omasba.clairaud.presentation.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.component.EqNotFound
import com.omasba.clairaud.presentation.component.PresetGraph
import com.omasba.clairaud.presentation.home.component.ApplyPresetCard
import com.omasba.clairaud.presentation.home.component.EqCard
import com.omasba.clairaud.presentation.home.model.AutoEqViewModel
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import com.omasba.clairaud.presentation.home.state.EqualizerUiState
import com.omasba.clairaud.presentation.store.component.TagList
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.store.state.EqPreset

val TAG = "EqScreen"

@Composable
fun EqScreen(
    eqViewModel: EqualizerViewModel,
    storeViewModel: StoreViewModel,
    navController: NavHostController
) {
    val eq by EqRepo.eq.collectAsState()

    var isAuthenticated by remember { mutableStateOf<Boolean?>(null) } //per capire quando si è autenticati

    LaunchedEffect(Unit) {
        isAuthenticated =
            UserRepo.isLogged() //si verifica se l'utente è loggato e anche la validità del token
    }

    if (eq != null) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState(), enabled = true)
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            EqCard(viewModel = eqViewModel, navController = navController)
            Log.d(TAG, "Eq card loaded")
            Spacer(modifier = Modifier.height(16.dp))

            //sezione da proteggere, i preset applicabili sono solo i preferiti quindi bisogna essere autenticati
            when (isAuthenticated) {
                true -> {
                    ApplyPresetCard(eqViewModel, storeViewModel)
                }

                false -> {
                    //niente
                }

                null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    } else {
        EqNotFound()
    }

}



