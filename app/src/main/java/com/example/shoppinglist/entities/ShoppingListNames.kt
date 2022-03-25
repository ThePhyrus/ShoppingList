package com.example.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_list_names") // таблица для сохранения названий списков

data class ShoppingListNames(
    // здесь укажу какие колонны будут в списке

    @PrimaryKey(autoGenerate = true) // у каждого элемента будет уникальный id (следить не надо)
    val id: Int?,

    @ColumnInfo(name = "name") // название создаваемого списка
    val name: String,

    @ColumnInfo(name = "time") //фиксирует время создания списка
    val time: String,

    @ColumnInfo(name = "allItemCounter") // общее кол-во эл-ов в конкретном списке (for progressBar)
    val allItemCounter: Int,

    @ColumnInfo(name = "checkedItemsCounter") // счётчик отмеченных (купленных) позиций (checkBox)
    val checkedItemsCounter: Int,

    @ColumnInfo(name = "itemsIds") // id всех элементов добавленных в список
    val itemsIds: String,
) : Serializable
