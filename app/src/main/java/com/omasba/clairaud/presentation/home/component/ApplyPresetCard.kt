package com.omasba.clairaud.presentation.home.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.presentation.component.PresetGraph
import com.omasba.clairaud.presentation.home.TAG
import com.omasba.clairaud.presentation.home.model.EqualizerViewModel
import com.omasba.clairaud.presentation.store.component.TagList
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.store.state.EqPreset


@Composable
fun ApplyPresetCard(eqViewModel: EqualizerViewModel, storeViewModel: StoreViewModel) {
    val presets by storeViewModel.favoritePresets.collectAsState() //solo i preferiti per il drop down
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
                text = "Apply a favourite preset",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                            key(preset.name) {
                                PresetGraph(preset.name, EqRepo.getBandsFormatted(preset.bands))
                                Log.d(TAG, "graph changed")
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            ) {
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
                            ) {
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
