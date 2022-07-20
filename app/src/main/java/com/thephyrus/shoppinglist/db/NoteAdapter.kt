package com.thephyrus.shoppinglist.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thephyrus.shoppinglist.R
import com.thephyrus.shoppinglist.databinding.NoteListItemBinding
import com.thephyrus.shoppinglist.entities.NoteItem
import com.thephyrus.shoppinglist.utils.HtmlManager
import com.thephyrus.shoppinglist.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val defPref: SharedPreferences) :
    ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    /*
    Примерное описание работы класса NoteAdapter.kt (recycler view adapter):
    Тут будет исползован класс DiffUtil, который будет сравнивать элементы из старого списка и из
    нового и сам будет их обновлять, зная, что нужно делать, если элемент был удалён либо был
    добавлен новый элемент и тд.
    В треугольных скобках указан элемент, который будет в нашем списке.
    В данном случае это будет наш NoteItem.kt потому, что именно NoteItem.kt содержит в себе всю
    информацию о заметке. После запятой укажем специальный класс, который будет создавать и
    содержать в себе разметку, которую мы сделали под заметку (note_list_item.xml). Вообще, этот
    специальный класс называется ViewHolder, но нам нужно создать свой клас (в данном случае это
    будет ItemHolder) и унаследоваться от ViewHolder.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        /*
        функция будет создавать для каждой заметки, которая берётся из базы данных, свой собсвенный
        ItemHolder, который в себе будет создавать разметку для каждого элемента. После создания
        разметки она сразу заполняется про помощи onBindViewHolder.
         */
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPref)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        /*
        Внутри этого класса будем передавать разметку, которая "раздуется" с помощью LayoutInflater.
        Если в списке будет 50 элементов, то создастся 50 экземпляров данного класса, которые в себе
        будут хранить ссылку на эту разметку и на все элементы в ней. Именно этот класс должен быть
        унаследован от RecyclerView.ViewHolder.
         */

        private val binding = NoteListItemBinding.bind(view)

        fun setData(note: NoteItem, listener: Listener, defPref: SharedPreferences) = with(binding) {

            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defPref) //changed on lesson 53
            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
            imDelete.setOnClickListener {
                listener.deleteItem(note.id!!)
            }
        }

        companion object {// для упрощения инициализации ItemHolder

            fun create(parent: ViewGroup): ItemHolder {
                //эта функция будет выдавать инициализированный класс ItemHolder
                return ItemHolder(
                    LayoutInflater.from(parent.context) // надуватель разметки
                        .inflate(R.layout.note_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        /*
        Этот класс сравнивает новое и старое.
         */
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id // сравнение по id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem // полное (общее) сравнение. Сравнивает весь контент элементов
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}