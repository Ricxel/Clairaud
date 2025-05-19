package com.omasba.clairaud.ui.components.store

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.omasba.clairaud.components.EqRepo
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.components.UserRepo
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.ui.components.PresetGraph

@Composable
fun PresetCard(preset: EqPreset, favPresets: State<Set<Int>>, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
//            .background(MaterialTheme.colorScheme.surface)
            .animateContentSize(animationSpec = tween(0)), // fluidità altezza
        shape = RoundedCornerShape(50.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {expanded = !expanded}
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = preset.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))

                if(preset.authorUid == UserRepo.currentUser.uid){
                    IconButton(
                        onClick = {
                            navController.navigate("addPreset")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit preset",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = {
                        Log.d("store", "Click")
                        if(favPresets.value.contains(preset.id))
                            UserRepo.removeFavorite(preset.id)
                        else UserRepo.addFavorite(preset.id)
                        Log.d("store","Fav: ${favPresets.value}")
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
                    Text(text = "Author: ${preset.author}", style = MaterialTheme.typography.bodyMedium)
                    TagList(preset.tags)
                    PresetGraph(presetName = preset.name, bands = EqRepo.getBandsFormatted(preset.bands))
                }
            }
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
fun PresetCardPreview(){
    val presets by StoreRepo.presets.collectAsState()
    val favPreset = UserRepo.favPresets
    PresetCard(presets.first(), favPreset.collectAsState(), rememberNavController())
}