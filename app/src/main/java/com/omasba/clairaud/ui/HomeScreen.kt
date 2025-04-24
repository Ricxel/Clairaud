package com.omasba.clairaud.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.omasba.clairaud.ui.models.EqualizerViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.autoeq.ui.AutoEq
import com.omasba.clairaud.autoeq.ui.AutoEqViewModel
import com.omasba.clairaud.ui.theme.ClairaudTheme



@Composable
fun EqCard(viewModel: EqualizerViewModel = EqualizerViewModel()) {

    val eqState by viewModel.eqState.collectAsState()
    val isOn = eqState.isOn
    val bands = eqState.bands
    Log.d("before", bands.toString())
    val autoEqModel = AutoEqViewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Prima riga: Titolo e switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Equalizer",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isOn,
                    onCheckedChange = { viewModel.ToggleEq() }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Seconda riga: Colonne con dB, slider e Hz
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                bands.take(6).forEachIndexed { index, band ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(48.dp)
                    ) {
                        // Valore dB
                        Text(
                            text = "${band.second}dB",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isOn) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // Slider verticale
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .width(40.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Slider(
                                value = band.second.toFloat(),
                                onValueChange = { newValue ->
                                    val updatedBands = ArrayList(bands)
                                    updatedBands[index] = band.first to (newValue).toInt().toShort()
                                    viewModel.NewBands(updatedBands)
                                },
                                valueRange = -15f..15f,
                                enabled = isOn,
                                modifier = Modifier
                                    .rotate(-90f)
                                    .fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }

                        // Frequenza
                        Text(
                            text = formatFrequency(band.first),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isOn) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))
            // Terza riga: AutoEQ
            AutoEq(autoEqModel)
        }
        }
    }
@Composable
fun formatFrequency(hz: Int): String {
    return when {
        hz >= 1000 -> "${hz / 1000}k"
        else -> "$hz"
    } + "Hz"

}