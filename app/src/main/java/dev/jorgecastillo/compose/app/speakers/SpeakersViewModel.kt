@file:OptIn(SavedStateHandleSaveableApi::class)

package dev.jorgecastillo.compose.app.speakers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpeakersViewModel(
    private val repo: SpeakersRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<List<Speaker>> =
        savedStateHandle.getStateFlow("speakers", emptyList())

    init {
        // This scope is cancelled when the ViewModel is cleared, which depends on how the ViewModel
        // is scoped. When scoped to the host activity or fragment, it will be cleared when the host
        // is gone for good. If it is scoped to a navigation destination via Compose Navigation,
        // it will be cleared when this screen is removed from the backstack.
        viewModelScope.launch {
            val speakers = repo.loadSpeakers()
            savedStateHandle["speakers"] = speakers
        }
    }
}
