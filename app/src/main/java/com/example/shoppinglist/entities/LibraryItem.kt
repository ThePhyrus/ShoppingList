package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")

// мини-библитека для хранения названий ранее вводимых пользователем
data class LibraryItem(
    @PrimaryKey(autoGenerate = true) // автогенерация id
    val id: Int?,

    @ColumnInfo(name = "name") // название элемента в библиотеке
    val name: String
)
