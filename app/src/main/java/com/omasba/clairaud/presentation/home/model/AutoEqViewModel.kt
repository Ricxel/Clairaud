package com.omasba.clairaud.presentation.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omasba.clairaud.presentation.home.state.AutoEqStateHolder
import com.omasba.clairaud.presentation.home.state.AutoEqUiState
import kotlinx.coroutines.flow.StateFlow

class AutoEqViewModel : ViewModel() {
    val uiState: StateFlow<AutoEqUiState> = AutoEqStateHolder.uiState

    fun changeIsOn(value: Boolean) {
        AutoEqStateHolder.setIsOn(value)
        Log.d("autoeq", "isOn: ${uiState.value.isOn}")
    }
}
