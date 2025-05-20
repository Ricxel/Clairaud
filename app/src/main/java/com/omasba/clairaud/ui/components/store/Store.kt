package com.omasba.clairaud.ui.components.store

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.components.UserRepo
import com.omasba.clairaud.ui.models.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Store(viewModel: StoreViewModel, navController: NavHostController) {
    val presets by viewModel.presets.collectAsState()
//    val filteredPresets by viewModel.filteredPresets.collectAsState() //per barra di ricerca
    val filteredPresets by viewModel.filteredItemsByTags.collectAsState() // per tags
    val showUserPresets by viewModel.showUserPresets.collectAsState()
    val filterByFavorites by viewModel.filterByFavorites.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val allTags = remember { //estrae i tag dispinibili dai preset
        viewModel.presets.value.flatMap { it.tags }.toSet().sortedBy { it.name }.toSet()
    }
    var active by remember { mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
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
                .padding(start = 8.dp, end = 8.dp)

        ) {
            // mostrato solo quando attiva
        }

        //filtro per i tag
        TagFilterSection(
            availableTags = allTags,
            selectedTags = selectedTags,
            onTagToggle = {tag ->
                if(selectedTags.contains(tag)){
                    viewModel.onTagRemoved(tag)
                }
                else{
                    viewModel.onTagSelected(tag)
                }
            }
        )
        //filtro preferiti
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            ){
            Text(
                text = "Show only favourites",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = filterByFavorites,
                onCheckedChange = {viewModel.toggleFavoriteFilter()}
            )
        }
        // filtro sui preset fatti dall'utente
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Show only my presets",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = showUserPresets,
                onCheckedChange = {viewModel.toggleShowUserPresets()}
            )
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
                    PresetCard(preset, viewModel.favPresets.collectAsState(), navController = navController)
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SearchablePresetListPreview() {
    Store(StoreViewModel(), rememberNavController())
}
