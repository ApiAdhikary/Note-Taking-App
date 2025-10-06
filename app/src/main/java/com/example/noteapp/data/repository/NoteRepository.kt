package com.example.noteapp.data.repository

import com.example.noteapp.data.local.NoteDao
import com.example.noteapp.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)

    fun getFavoriteNotes(): Flow<List<NoteEntity>> = noteDao.getFavoriteNotes()

    suspend fun getNoteById(noteId: Int): NoteEntity? = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: NoteEntity) = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)

    suspend fun toggleFavorite(note: NoteEntity) {
        noteDao.updateNote(note.copy(isFavorite = !note.isFavorite))
    }
}