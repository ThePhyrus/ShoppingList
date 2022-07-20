package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_two")

// мини-библитека для хранения названий ранее вводимых пользователем
data class TestItemTwo(
    @PrimaryKey(autoGenerate = true) // автогенерация id
    val id: Int?,

    @ColumnInfo(name = "name") // название элемента в библиотеке
    val name: String,

    @ColumnInfo(name = "price", defaultValue = "") // название элемента в библиотеке
    val price: String
)
