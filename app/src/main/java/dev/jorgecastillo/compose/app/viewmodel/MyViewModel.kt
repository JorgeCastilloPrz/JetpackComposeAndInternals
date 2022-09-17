package dev.jorgecastillo.compose.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jorgecastillo.compose.app.data.FakeSpeakerRepository
import dev.jorgecastillo.compose.app.data.SpeakerRepository
import dev.jorgecastillo.compose.app.models.Speaker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SpeakersState(
    val isLoading: Boolean = false,
    val speakers: List<Speaker> = emptyList()
)

class SpeakersViewModel(
    private val repo: SpeakerRepository = FakeSpeakerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpeakersState())
    val uiState: StateFlow<SpeakersState>
        get() = _uiState.asStateFlow()

    init {
        loadSpeakers()
    }

    fun onPullToRefresh() {
        loadSpeakers()
    }

    private fun loadSpeakers() {
        viewModelScope.launch {
            // A fake 2 second 'refresh'
            _uiState.value = _uiState.value.copy(isLoading = true)
            val speakers = repo.getSpeakers()
            delay(2000)
            _uiState.value = _uiState.value.copy(isLoading = false, speakers = speakers)
        }
    }
}
