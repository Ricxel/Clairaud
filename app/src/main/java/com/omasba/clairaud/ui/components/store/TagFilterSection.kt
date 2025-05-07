package com.omasba.clairaud.ui.components.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.model.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFilterSection(
    availableTags: Set<Tag>,
    selectedTags: Set<Tag>,
    onTagToggle: (Tag) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableTags.forEach { tag ->
            FilterChip(
                selected = selectedTags.contains(tag),
                onClick = { onTagToggle(tag) },
                label = { Text(tag.name) }
            )
        }
    }
}