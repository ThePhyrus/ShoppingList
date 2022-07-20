package com.thephyrus.shoppinglist.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.thephyrus.shoppinglist.entities.LibraryItem
import com.thephyrus.shoppinglist.entities.NoteItem
import com.thephyrus.shoppinglist.entities.ShopListItem
import com.thephyrus.shoppinglist.entities.ShopListNameItem

@Database(
    entities = [
        LibraryItem::class,
        NoteItem::class,
        ShopListItem::class,
        ShopListNameItem::class],
    version = 7,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 6, to = 7, spec = MainDataBase.SpecMigration::class)]
)

abstract class MainDataBase : RoomDatabase() {

    @DeleteTable(tableName = "test_two")
    class SpecMigration : AutoMigrationSpec

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