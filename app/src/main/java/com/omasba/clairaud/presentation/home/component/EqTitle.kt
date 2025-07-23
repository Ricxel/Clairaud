package com.omasba.clairaud.presentation.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel

@Composable
fun EqTitle(
    isEnabled: Boolean,
    isOn: Boolean,
    viewModel: EqualizerViewModel,
    navController: NavHostController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Equalizer",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(
            onClick = {
                //navigazione all'aggiunta del preset
                navController.navigate("addPreset")
            },
            enabled = isEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Share",
                modifier = Modifier.size(40.dp)
            )
        }
        Switch(
            checked = isOn,
            onCheckedChange = { viewModel.toggleEq() }
        )
    }

}
