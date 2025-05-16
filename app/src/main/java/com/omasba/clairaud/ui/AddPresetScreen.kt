package com.omasba.clairaud.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omasba.clairaud.components.StoreRepo
import com.omasba.clairaud.model.EqPreset
import com.omasba.clairaud.model.Tag
import com.omasba.clairaud.ui.components.BottomNavItem
import com.omasba.clairaud.ui.components.PresetGraph
import com.omasba.clairaud.ui.components.store.FloatingButton
import com.omasba.clairaud.ui.components.store.TagList
import com.omasba.clairaud.ui.models.AddPresetViewModel

@Composable
fun AddPresetScreen(viewModel: AddPresetViewModel, navController: NavHostController){
    val eqPreset by viewModel.eqPreset.collectAsState()
    var tagInput by remember { mutableStateOf("") }
    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Text(
                text = "Add preset",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            //area dell'eq
            //TODO
            PresetGraph(presetName = eqPreset.name, bands = EqPreset().bands)

            //area per le altre info del preset
            TextField(
                value = eqPreset.name,
                onValueChange = {viewModel.updatePresetName(it)},
                label = {Text("Preset name")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            if(eqPreset.tags.isEmpty())
                Text(
                    text = "No tags added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            TagList(eqPreset.tags)
            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("Tag") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                modifier = Modifier
                    .width(100.dp)
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Button(
                onClick = {viewModel.addTag(Tag(name = tagInput))},
            ) {
                Text("Add tag")
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    onClick = {
                    TODO()
                }) {
                    Text(text = "Add")
                }
            }

        }
        FloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            icon = Icons.Filled.Close
        )
        {
            navController.navigate(BottomNavItem.Store.route)
        }

    }
}

@Composable
@Preview(
    showBackground = true
)
fun AddPresetScreenPreview(){
    AddPresetScreen(AddPresetViewModel(), rememberNavController())
}