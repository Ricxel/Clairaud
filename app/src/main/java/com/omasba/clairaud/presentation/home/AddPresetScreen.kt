package com.omasba.clairaud.presentation.home

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.data.repository.EqRepo
import com.omasba.clairaud.presentation.component.PresetGraph
import com.omasba.clairaud.presentation.home.model.AddPresetViewModel
import com.omasba.clairaud.presentation.store.component.TagList
import com.omasba.clairaud.presentation.store.state.Tag

@Composable
fun AddPresetScreen(viewModel: AddPresetViewModel, navController: NavHostController) {
    val eqPreset by viewModel.eqPreset.collectAsState()
    val eqState by EqRepo.eqState.collectAsState()
    val error by viewModel.error.collectAsState()
    var tagInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Add preset",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            //area dell'eq
            val rawBands = if (eqPreset.authorUid != "") eqPreset.bands else eqState.bands

            //se non c'è una sessione attiva, non si posso ottenere le bande vere, quindi bisogna usare le rawbands
            var bands = ArrayList(rawBands)

            if (EqRepo.getFreq(0) != -1) {
                //vuol dire che c'è un eq attivo
                bands = EqRepo.getBandsFormatted(bands)
            } else {
                //non attivo
                bands.clear()
                rawBands.forEach { band ->
                    bands.add(Pair<Int, Short>(band.first, (band.second / 100).toShort()))
                }

            }

            PresetGraph(presetName = eqPreset.name, bands = bands)
            Spacer(modifier = Modifier.height(8.dp))
            //area per le altre info del preset
            TextField(
                value = eqPreset.name,
                onValueChange = { viewModel.updatePresetName(it) },
                label = { Text("Preset name") },
                modifier = Modifier.fillMaxWidth()
            )
            // per mostrare l'errore
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            if (eqPreset.tags.isEmpty())
                Text(
                    text = "No tags added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            TagList(eqPreset.tags) { tag ->
                viewModel.removeTag(tag)
            }
            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("Tag") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier
                    .width(80.dp)
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Button(
                onClick = {
                    viewModel.addTag(Tag(name = tagInput))
                    tagInput = ""
                },
            ) {
                Text("Add tag")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Text(text = "Cancel")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (viewModel.confirmPreset(rawBands))
                            navController.popBackStack()
                    },
                ) {
                    Text(text = "Confirm")
                }
            }

        }

    }
}

@Composable
@Preview(
    showBackground = true
)
fun AddPresetScreenPreview() {
    AddPresetScreen(AddPresetViewModel(), rememberNavController())
}