package com.example.noteapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.ui.screens.addedit.AddEditViewModel

class ViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddEditViewModel::class.java) -> {
                AddEditViewModel(repository, extras.createSavedStateHandle()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}