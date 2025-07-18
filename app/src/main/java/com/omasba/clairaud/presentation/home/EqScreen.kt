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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omasba.clairaud.autoeq.ui.AutoEq
import com.omasba.clairaud.autoeq.ui.AutoEqViewModel
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.presentation.store.state.EqPreset

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.key
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import com.omasba.clairaud.presentation.component.EqNotFound
import com.omasba.clairaud.presentation.component.PresetGraph
import com.omasba.clairaud.presentation.store.component.TagList
import com.omasba.clairaud.presentation.store.model.StoreViewModel

val TAG = "EqScreen"

@Composable
fun EqScreen(eqViewModel: EqualizerViewModel, storeViewModel: StoreViewModel, navController: NavHostController, isAuthenticated: () -> Boolean){
    val eq by EqRepo.eq.collectAsState()

    if (eq != null) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            EqCard(viewModel = eqViewModel, navController = navController)
            Log.d(TAG, "Eq card loaded")
            Spacer(modifier = Modifier.height(16.dp))

            //sezione da proteggere, i preset applicabili sono solo i preferiti quindi bisogna essere autenticati
            if(isAuthenticated()){
                ApplyPresetCard(eqViewModel, storeViewModel)
                Log.d(TAG, "Preset card loaded")
            }
        }
    }
    else{
        EqNotFound()
    }

}

@Composable
fun EqCard(viewModel: EqualizerViewModel, navController: NavHostController) {

    val eqState by viewModel.eqState.collectAsState()
    val isOn = eqState.isOn
    val eq by EqRepo.eq.collectAsState()
    val bands = eqState.bands
    Log.d(TAG, "bands: $bands.toString()")
    val autoEqModel = AutoEqViewModel()

    Log.d("EqScreen", "eq card ${eq}")

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
                // Prima riga: Titolo e switch
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
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                    Switch(
                        checked = isOn,
                        onCheckedChange = { viewModel.toggleEq() }
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Seconda riga: Colonne con dB, slider e Hz
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val bandsNum = eqState.bands.size
                    bands.take(bandsNum).forEachIndexed { index, band ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width((cardWidth/bandsNum)) // spazio per testo sopra e sotto
                        ) {
                            var dBx = 0


                            // Testo dB
                            Text(
                                text = "${band.second/100}dB",
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
                                    .height(250.dp) // Altezza totale della zona slider
                                    .width(250.dp),  // Larghezza effettiva dello slider verticale
//                                .background(Color.Blue)
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
                                    var sliderValue = band.second/100 // perche nello state e nell'eq vengono impostati come mB

                                    Slider(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        value = sliderValue.toFloat(),
                                        onValueChange = { newValue ->
//                                            val updatedBands = bands
//                                            updatedBands[index] =
//                                                band.first to (newValue).toInt().toShort()

                                            sliderValue = newValue.toInt()
                                            Log.d(TAG, "on change: $newValue")
                                            viewModel.setBand(index, (sliderValue * 100).toShort())
//                                            viewModel.setBandByHz(band.first, (sliderValue*100).toShort())
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

                            // Frequenza
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


                Spacer(modifier = Modifier.height(24.dp))
                // Terza riga: AutoEQ
                AutoEq(autoEqModel)

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
@Composable
fun ApplyPresetCard(eqViewModel: EqualizerViewModel, storeViewModel: StoreViewModel) {
    val presets by storeViewModel.favoritePresetsOnly.collectAsState()
    var rightExpanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<EqPreset?>(null) }

    LaunchedEffect(Unit) {
        //if(presets.isEmpty())
        storeViewModel.fetchPresets()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Apply preset",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Comparison Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        val rightText = selected?.name ?: "Preset"

                        Column {
                            PresetDropdown(
                                text = rightText,
                                expanded = rightExpanded,
                                onExpandChange = { rightExpanded = it },
                                presets = presets,
                                onPresetSelected = {
                                    selected = it
                                    rightExpanded = false

                                    eqViewModel.newBands(it.bands)
                                },
                                enabled = presets.isNotEmpty()
                            )

                            selected?.let { preset ->
                                key (preset.name){
                                    PresetGraph(preset.name, EqRepo.getBandsFormatted(preset.bands))
                                    Log.d(TAG, "graph changed")
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                ){
                                    Text(
                                        text = "Author: ",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = preset.author,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = "Tag ",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    TagList(preset.tags)
                                }
                            }
                        }

                    }

            }

        }
    }
}

@Composable
fun PresetDropdown(
    text: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    presets: List<EqPreset>,
    onPresetSelected: (EqPreset) -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(enabled = enabled) { onExpandChange(!expanded) }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (presets.isNotEmpty()) text else "No favorite presets",
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        if (enabled) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) },
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                presets.forEach { preset ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = preset.name,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = { onPresetSelected(preset) }
                    )
                }
            }
        }
    }
}
