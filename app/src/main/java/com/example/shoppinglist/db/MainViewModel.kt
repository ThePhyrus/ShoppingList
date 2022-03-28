package com.example.shoppinglist.db

import androidx.lifecycle.*
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingListName
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDataBase) : ViewModel() {

    /*
    В этот класс-посредник передаётся сознная мной database. С помощью database буду получать свой
    интрфейс Dao, с помощью которого, буду записывать и считывать информацию.
     */

    val dao = database.getDao() // создам экземпляр? интефейса Dao


    // если список обновится, то обновится и allNotes
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNames: LiveData<List<ShoppingListName>> = dao.getAllShopListNames().asLiveData()


    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.insertShopListName(listName)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }


    class MainViewModelFactory(val database: MainDataBase) : ViewModelProvider.Factory {
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