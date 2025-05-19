package com.omasba.clairaud.ui.components.store

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(max = 200.dp),
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState),
        ){
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).padding(10.dp),
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

    }
}