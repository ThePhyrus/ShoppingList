package com.example.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ListNameItemBinding
import com.example.shoppinglist.entities.NoteItem

import com.example.shoppinglist.entities.ShoppingListName


class ShopListNameAdapter(private val listener:Listener) : ListAdapter<ShoppingListName, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListNameItemBinding.bind(view)

        fun setData(shopListNameItem: ShoppingListName, listener:Listener) = with(binding) {
            tvListName.text = shopListNameItem.name
            tvListCreatingTime.text = shopListNameItem.time
            itemView.setOnClickListener {

            }
            imDeleteList.setOnClickListener {
                listener.deleteItem(shopListNameItem.id!!)
            }
            imEditList.setOnClickListener {
                listener.editItem(shopListNameItem)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context) // надуватель разметки
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListName>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListName, newItem: ShoppingListName
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShoppingListName, newItem: ShoppingListName
        ): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem (id:Int)
        fun editItem (shopListName: ShoppingListName)
        fun onClickItem (shopListName: ShoppingListName)
    }
}