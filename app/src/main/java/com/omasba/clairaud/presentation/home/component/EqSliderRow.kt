package com.omasba.clairaud.presentation.home.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.presentation.home.TAG
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import com.omasba.clairaud.presentation.home.state.EqualizerUiState


@Composable
fun EqSliderRow(
    eqState: EqualizerUiState,
    bands: ArrayList<Pair<Int, Short>>,
    cardWidth: Dp,
    isOn: Boolean,
    viewModel: EqualizerViewModel
) {
    // colonne con dB, slider e Hz
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        val bandsNum = eqState.bands.size
        bands.take(bandsNum).forEachIndexed { index, band ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width((cardWidth / bandsNum)) // spazio per testo sopra e sotto
            ) {
                var dBx = 0

                // Testo dB
                Text(
                    text = "${band.second / 100}dB",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOn) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInParent()
                            val size = coordinates.size
                            dBx = (position.x + size.width / 2).toInt()
                        }
                )

                //Slider verticale libero dai vincoli della colonna
                Box(
                    modifier = Modifier
                        .height(250.dp) // altezza totale della zona slider
                        .width(250.dp),  // larghezza effettiva dello slider verticale
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .rotate(-90f)
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        maxWidth = constraints.maxHeight,
                                        maxHeight = constraints.maxWidth
                                    )
                                )

                                layout(placeable.height, placeable.width) {
                                    val layoutHeight = placeable.width
                                    val x = dBx - placeable.width / 2.7f
                                    val y = (layoutHeight - placeable.height) / 1.8f

                                    placeable.placeRelative(x.toInt(), y.toInt())

                                }
                            }
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .align(Alignment.Center),
                    ) {
                        var sliderValue =
                            band.second / 100 // perche nello state e nell'eq vengono impostati come mB

                        Slider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = sliderValue.toFloat(),
                            onValueChange = { newValue ->
                                sliderValue = newValue.toInt()
                                Log.d(TAG, "on change: $newValue")
                                viewModel.setBand(index, (sliderValue * 100).toShort())
                            },

                            valueRange = -15f..15f,
                            enabled = isOn,
                            steps = 30,

                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }

                // Hz
                Text(
                    text = formatFrequency(EqRepo.getFreq(index.toShort())),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOn) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

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