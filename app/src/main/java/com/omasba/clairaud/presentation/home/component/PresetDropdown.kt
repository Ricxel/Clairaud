package com.omasba.clairaud.presentation.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.presentation.store.state.EqPreset


@Composable
fun PresetDropdown(
    text: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    presets: List<EqPreset>,
    onPresetSelected: (EqPreset) -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(enabled = enabled) { onExpandChange(!expanded) }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (presets.isNotEmpty()) text else "No favorite presets",
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
        }

        if (enabled) {
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
}
