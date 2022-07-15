package com.example.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.entities.LibraryItem
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShopListNameItem
import com.example.shoppinglist.entities.ShopListItem
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    /*
    Этот интерфейс служит для того, чтобы получать доступ к базе данных и производить операции записи
    или считывания данных.
     */

    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM  shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query("SELECT * FROM  shop_list_item WHERE listId LIKE :listId") //watch lesson 36
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query("SELECT * FROM  library WHERE name LIKE :name") //watch lesson 44
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Query("DELETE FROM  shop_list_item WHERE listId LIKE :listId") //watch lesson 41
    suspend fun deleteShopItemsByListId(listId: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)

    @Insert // lesson 44
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateListItem(item: ShopListItem) //lesson 39

    @Update
    suspend fun updateListName(shopListNameItem: ShopListNameItem)
}