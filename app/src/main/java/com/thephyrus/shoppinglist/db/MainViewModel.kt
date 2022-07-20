package com.thephyrus.shoppinglist.db

import androidx.lifecycle.*
import com.thephyrus.shoppinglist.entities.LibraryItem
import com.thephyrus.shoppinglist.entities.NoteItem
import com.thephyrus.shoppinglist.entities.ShopListItem
import com.thephyrus.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch

class MainViewModel(database: MainDataBase) : ViewModel() {

    /*
    В этот класс-посредник передаётся сознная мной database. С помощью database буду получать свой
    интрфейс Dao, с помощью которого, буду записывать и считывать информацию.
     */

    private val dao = database.getDao() // создам экземпляр? интефейса Dao

    val libraryItems = MutableLiveData<List<LibraryItem>>() //lesson 45

    // если список обновится, то обновится и allNotes
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNames: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>> { //lesson 36
        return dao.getAllShopListItems(listId).asLiveData()
    }

    fun getAllLibraryItems(name: String) = viewModelScope.launch{ //lesson 45
       libraryItems.postValue(dao.getAllLibraryItems(name))
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listName: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(listName)
    }

    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if (!isLibraryItemExists(shopListItem.name)) dao.insertLibraryItem( //lesson 44
            LibraryItem(
                null,
                shopListItem.name
            )
        )
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch { //lesson 46
        dao.updateLibraryItem(item)
    }

    fun updateListItem(item: ShopListItem) = viewModelScope.launch { //lesson 39
        dao.updateListItem(item)
    }

    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListNameItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteLibraryItem(id: Int) = viewModelScope.launch { //lesson 46
        dao.deleteLibraryItem(id)
    }

    fun deleteShopList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if (deleteList) dao.deleteShopListName(id)
        dao.deleteShopItemsByListId(id) //lesson 41
    }

    private suspend fun isLibraryItemExists(name: String): Boolean { //lesson 44
        return dao.getAllLibraryItems(name).isNotEmpty()
    }


    class MainViewModelFactory(private val database: MainDataBase) : ViewModelProvider.Factory {
        // Этот класс будет иницилизировать MainViewModel (всегда одинаково пишется??)
        // Так рекомендуют делать на официальном сайте
        // Не делай мозг, а просто запомни. И фсё что ли??
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // эта функция и будет создавать наш вью модел
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T // возвращает инициализированный класс
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}