package com.omasba.clairaud.presentation.store.component

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.presentation.store.model.StoreViewModel
import com.omasba.clairaud.presentation.theme.ClairaudTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Store(viewModel: StoreViewModel, navController: NavHostController) {
    val filteredPresets by viewModel.filteredPresets.collectAsState() // per tags
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


        //riga dei filtri
        Row (
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            ){

            //filtro sui preferiti
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = filterByFavorites,
                    onCheckedChange = {viewModel.toggleFavoriteFilter()}
                )
            }

            Spacer(Modifier.width(6.dp))

            // filtro sui preset fatti dall'utente
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My preset",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = showUserPresets,
                    onCheckedChange = { viewModel.toggleShowUserPresets() }
                )
            }

            //Bottone di refresh
            IconButton(
                onClick = {
                    viewModel.fetchPresets()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "favorite icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

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
                modifier = Modifier
                    .fillMaxSize()
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
    ClairaudTheme {
        Store(StoreViewModel(), rememberNavController())
    }
}
