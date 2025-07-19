package com.omasba.clairaud.service.autoeq.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.service.autoeq.presentation.model.AutoEqViewModel
import com.omasba.clairaud.service.autoeq.presentation.state.AutoEqStateHolder

@Composable
fun AutoEq(viewModel: AutoEqViewModel) {
    val state by viewModel.uiState.collectAsState()

    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isEnabled = UserRepo.isLogged()
        if(!isEnabled){
            AutoEqStateHolder.setIsOn(false)
        }
    }

    Row (
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = "AutoEq",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = state.currentPreset.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(18.dp,5.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Switch(
            checked = state.isOn,
            onCheckedChange = {checked ->
                viewModel.changeIsOn(checked)
            },
            enabled = isEnabled
        )
    }
}