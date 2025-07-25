package com.omasba.clairaud.presentation.store.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.data.repository.StoreRepo
import com.omasba.clairaud.data.repository.UserRepo
import com.omasba.clairaud.presentation.component.PresetGraph
import com.omasba.clairaud.presentation.store.state.EqPreset

@Composable
fun PresetCard(preset: EqPreset, favPresets: State<Set<Int>>, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(animationSpec = tween(0)), // fluidità altezza
        shape = RoundedCornerShape(50.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = preset.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))

                if (preset.authorUid == UserRepo.currentUserProfile.uid) {
                    IconButton(
                        onClick = {
                            navController.navigate("editPreset/${preset.id}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit preset",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            StoreRepo.removePreset(preset)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete preset",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = {
                        Log.d("store", "Click")
                        if (favPresets.value.contains(preset.id))
                            UserRepo.removeFavorite(preset.id)
                        else UserRepo.addFavorite(preset.id)
                        Log.d("store", "Fav: ${favPresets.value}")
                    }
                ) {
                    Icon(
                        imageVector = if (favPresets.value.contains(preset.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "favorite icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }


                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "expand icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            AnimatedVisibility(
                visible = expanded,
                exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
            )
            {
                //contenuto espanso
                Column {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Author: ${preset.author}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TagList(preset.tags)
                    //formatto le bande in modo che abbiamo decibel nel volume, come primo parametro lascio il numero di banda
                    //perchè altrimenti sarebbe possibile vedere il preset solo se si è collegati a una sessione audio
                    val graphBands = ArrayList<Pair<Int, Short>>()
                    preset.bands.forEach { band ->
                        graphBands.add(Pair<Int, Short>(band.first, (band.second / 100).toShort()))
                    }
                    PresetGraph(presetName = preset.name, bands = graphBands)
                }
            }
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
fun PresetCardPreview() {
    val presets by StoreRepo.presets.collectAsState()
    val favPreset = UserRepo.favPresets
    PresetCard(presets.first(), favPreset.collectAsState(), rememberNavController())
}