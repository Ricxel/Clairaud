package com.omasba.clairaud.presentation.store.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasba.clairaud.presentation.store.state.Tag

@Composable
fun TagList(tags: Set<Tag>, onTagClick: (Tag) -> Unit = {}) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    )
    {
        tags.forEach { tag ->
            AssistChip(
                onClick = {
                    onTagClick(tag)
                },
                label = { Text(text = tag.name) },
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun TagListPreview() {
    val tags = mutableSetOf(
        Tag(name = "Rock"),
        Tag(name = "nu Rock"),
        Tag(name = "AudioTechnica m30x"),
        Tag(name = "dt770"),
        Tag(name = "Nirvana"),
        Tag(name = "Beatles")
    )
    TagList(tags = tags) {
        //al click sul tag non fa niente
    }
}