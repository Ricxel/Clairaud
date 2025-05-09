package com.omasba.clairaud.ui

import androidx.compose.runtime.Composable
import com.omasba.clairaud.ui.components.store.SearchablePresetList
import com.omasba.clairaud.ui.models.PresetListViewModel

@Composable
fun StoreScreen(){
    val presetModel = PresetListViewModel()
    SearchablePresetList(viewModel = presetModel)
}
