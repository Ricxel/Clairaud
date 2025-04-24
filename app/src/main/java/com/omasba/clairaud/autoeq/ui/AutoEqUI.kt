package com.omasba.clairaud.autoeq.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AutoEq(viewModel: AutoEqViewModel) {
    val state by viewModel.uiState.collectAsState()
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "AutoEq - ${state.currentPreset.name}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = state.isOn,
            onCheckedChange = {checked ->
                viewModel.changeIsOn(checked)
            }
        )
    }
}