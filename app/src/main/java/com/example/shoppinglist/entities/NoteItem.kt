package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "note_list")

// таблица для создания заметок
data class NoteItem(
    //todo подумать, какие ещё элементы можно добавить в таблицу заметки

    @PrimaryKey(autoGenerate = true) // для автосоздания уникальных id заметок
    val id: Int?,

    @ColumnInfo(name = "title") // название заметки
    val title: String,

    @ColumnInfo(name = "content") // Содержание заметки
    val content: String, // not-nullable - пустые заметки сохраняться не должны

    @ColumnInfo(name = "time") // для фиксации времени создания заметки
    val time: String,

    @ColumnInfo(name = "category") // для возможности фильтровать заметки по категориям
    val category: String,
) : Serializable
