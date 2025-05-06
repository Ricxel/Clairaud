package com.omasba.clairaud.ui.components.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.ui.models.PresetListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchablePresetList(viewModel: PresetListViewModel = viewModel()) {
    val presets by viewModel.presets.collectAsState()
    val filteredPresets by viewModel.filteredPresets.collectAsState()
    var active by remember { mutableStateOf(false)}

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = viewModel.query.collectAsState().value,
            onQueryChange = { viewModel.onQueryChanged(it) },
            onSearch = {active = false},
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

        ) {
            // mostrato solo quando attiva
        }

        if(filteredPresets.isEmpty()){
            Text(
                text = "No presets found",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPresets) { preset ->
                    PresetCard(preset)
                }
            }
        }
    }
}
