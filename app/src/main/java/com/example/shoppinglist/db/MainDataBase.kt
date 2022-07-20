package com.example.shoppinglist.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.entities.*

@Database(
    entities = [
        LibraryItem::class,
        NoteItem::class,
        ShopListItem::class,
        ShopListNameItem::class,
        TestItem::class,
        TestItemTwo::class],
    version = 4, exportSchema = true, autoMigrations = [AutoMigration(from = 3, to = 4)]
)

abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao(): Dao // для получения доступа к интерфейсу Dao

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