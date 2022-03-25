package com.example.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.entities.LibraryItem
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingListItem
import com.example.shoppinglist.entities.ShoppingListNames

@Database(
    entities = [
        LibraryItem::class,
        NoteItem::class,
        ShoppingListItem::class,
        ShoppingListNames::class],
    version = 1
)

abstract class MainDataBase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            /*
            если INSTANCE не пуста - вернётся, а если пуста - создастся
             */
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db" // название создаваемого файла с базой данных
                ).build()
                instance
            }
        }
    }
}