package com.thephyrus.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list_item")

// таблица для каждой конкретной позиции в списке
data class ShopListItem(

    @PrimaryKey(autoGenerate = true) // автогенерация id для каждого элемента таблицы
    val id: Int?,

    @ColumnInfo(name = "name")// для хранения названия продукта
    val name: String,

    @ColumnInfo(name = "itemInfo")// для доп. информации (например, вес или количество продукта)
    val itemInfo: String = "", // nullable для возможности оставить поле пустым (изменил на 40 уроке)

    @ColumnInfo(name = "itemChecked") // куплен продукт или нет
    val itemChecked: Boolean = false,

    @ColumnInfo(name = "listId") // id списка, к которому принадлежит данный элемент
    val listId: Int,

    @ColumnInfo(name = "itemType") // для подсказок при введении названия продукта
    val itemType: Int = 0

)
