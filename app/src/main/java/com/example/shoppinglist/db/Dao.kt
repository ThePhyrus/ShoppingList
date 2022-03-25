package com.example.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shoppinglist.entities.NoteItem
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    /*
    Этот интерфейс служит для того, чтобы получать доступ к базе данных и производить операции записи
    или считывания данных.
     */

    @Query("SELECT * FROM note_list") // синтаксис базы данных SQLite
    fun getAllNotes(): Flow<List<NoteItem>> // функция для считывания, выдаст список с заметками

    @Insert // аннотация для функции записи данных в базу
    // "suspend" прописывается для того, чтобы запускать функцию внутри корутины
    suspend fun insertNote(note: NoteItem) // в функцию передаётся entity для заметок (NoteItem)
}