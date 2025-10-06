package com.example.noteapp.ui.screens.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.local.NoteEntity
import com.example.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int? = savedStateHandle["noteId"]

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _eventFlow = MutableStateFlow<UiEvent?>(null)
    val eventFlow: StateFlow<UiEvent?> = _eventFlow.asStateFlow()

    init {
        noteId?.let { id ->
            viewModelScope.launch {
                repository.getNoteById(id)?.let { note ->
                    _title.value = note.title
                    _description.value = note.description
                    _isFavorite.value = note.isFavorite
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }

    fun saveNote() {
        viewModelScope.launch {
            if (_title.value.isBlank()) {
                _eventFlow.value = UiEvent.ShowError("Title cannot be empty")
                return@launch
            }

            val note = NoteEntity(
                id = noteId ?: 0,
                title = _title.value.trim(),
                description = _description.value.trim(),
                isFavorite = _isFavorite.value,
                timestamp = System.currentTimeMillis()
            )

            repository.insertNote(note)
            _eventFlow.value = UiEvent.SaveSuccess
        }
    }

    fun clearEvent() {
        _eventFlow.value = null
    }

    sealed class UiEvent {
        data object SaveSuccess : UiEvent()
        data class ShowError(val message: String) : UiEvent()
    }
}