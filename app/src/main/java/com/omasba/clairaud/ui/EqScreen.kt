package com.omasba.clairaud.ui

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasba.clairaud.ui.models.EqualizerViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.autoeq.ui.AutoEq
import com.omasba.clairaud.autoeq.ui.AutoEqViewModel
import com.omasba.clairaud.components.EqService
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.model.EqPreset

import androidx.compose.material3.DropdownMenuItem
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.omasba.clairaud.ui.components.PresetGraph

@Composable
fun EqScreen(navController: NavHostController){
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
        var eqViewModel = remember { EqualizerViewModel() }
        EqCard(eqViewModel, navController = navController)
        Spacer(modifier = Modifier.height(16.dp))

        PresetComparisonCard(eqViewModel)
    }

}

@Composable
fun EqCard(viewModel: EqualizerViewModel = remember {EqualizerViewModel()}, navController: NavHostController) {
    val TAG = "EqScreen"

    val eqState by viewModel.eqState.collectAsState()
    val isOn = eqState.isOn
    val bands = eqState.bands
    Log.d(TAG, "bands: $bands.toString()")
    val autoEqModel = AutoEqViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val serviceIntent = Intent(context, EqService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        Log.d(TAG,"Lanciato!")
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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

                    val bandsNum = 5
                    var hz = 250
                    bands.take(bandsNum).forEachIndexed { index, band ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width((cardWidth/bandsNum)) // spazio per testo sopra e sotto
                        ) {
                            // Testo dB
                            Text(
                                text = "${band.second}dB",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isOn) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.padding(bottom = 4.dp)
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
                                                placeable.place(-325, 320)
                                            }
                                        }
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .align(Alignment.Center),
                                ) {
                                    var sliderValue by remember { mutableStateOf(band.first) }

                                    Slider(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        value = sliderValue.toFloat(),
                                        onValueChange = { newValue ->
                                            val updatedBands = ArrayList(bands)
                                            updatedBands[index] =
                                                band.first to (newValue).toInt().toShort()

                                            sliderValue = newValue.toInt()
                                            Log.d(TAG, "on change: $newValue")
                                            viewModel.setBand(band.first, (sliderValue * 100).toShort(), updatedBands)
//                                            viewModel.newBands(updatedBands)

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
                                text = formatFrequency(hz),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isOn) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            hz *= 2
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
fun PresetComparisonCard(viewModel: EqualizerViewModel = remember {EqualizerViewModel()}) {
    val presets by StoreRepo.presets.collectAsState()
    var leftExpanded by remember { mutableStateOf(false) }
    var rightExpanded by remember { mutableStateOf(false) }
    var selectedLeft by remember { mutableStateOf<EqPreset?>(null) }
    var selectedRight by remember { mutableStateOf<EqPreset?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Compare Presets",
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
                var showGraph by remember { mutableStateOf(false) }
                // Right Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    val rightText = selectedRight?.name ?: "Preset"
                    PresetDropdown(
                        text = rightText,
                        expanded = rightExpanded,
                        onExpandChange = { rightExpanded = it },
                        presets = presets,
                        onPresetSelected = {
                            selectedRight = it
                            rightExpanded = false
                            showGraph = true
                        }
                    )
                }

                if (showGraph && selectedRight != null) {
                    PresetGraph(selectedRight!!.name, selectedRight!!.bands)
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
    onPresetSelected: (EqPreset) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onExpandChange(!expanded) }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }

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
