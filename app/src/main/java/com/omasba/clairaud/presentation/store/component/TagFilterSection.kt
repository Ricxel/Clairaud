package com.omasba.clairaud.presentation.store.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.presentation.store.state.Tag

@Composable
fun TagFilterSection(
    availableTags: Set<Tag>,
    selectedTags: Set<Tag>,
    onTagToggle: (Tag) -> Unit
) {
    val scrollState = rememberScrollState()
    var expanded by remember{ mutableStateOf(false) }
    val maxHeight = if(expanded) 300.dp else 65.dp
    val animatedHeight by animateDpAsState(targetValue = maxHeight, label = "CardHeight")


    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(max = animatedHeight),
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
        ){
            Row{
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .weight(9f)
                        .padding(10.dp),
                ) {
                    availableTags.forEach { tag ->
                        FilterChip(
                            selected = selectedTags.contains(tag),
                            onClick = { onTagToggle(tag) },
                            label = { Text(tag.name) }
                        )
                    }
                }
                IconButton(
                    onClick = {expanded = !expanded}
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .weight(1f),
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "expand icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }

        }


    }
}