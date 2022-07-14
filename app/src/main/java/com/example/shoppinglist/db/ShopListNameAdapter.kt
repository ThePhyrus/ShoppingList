package com.example.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ListNameItemBinding
import com.example.shoppinglist.entities.ShopListNameItem


class ShopListNameAdapter(private val listener: Listener) :
    ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        /*
        функция будет создавать для каждого списка покупок, которая берётся из базы данных, свой
        собсвенный ItemHolder, который в себе будет создавать разметку для каждого элемента.
        После создания разметки она сразу заполняется про помощи onBindViewHolder.
         */
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListNameItemBinding.bind(view)

        fun setData(shopListNameItem: ShopListNameItem, listener: Listener) = with(binding) {

            tvListName.text = shopListNameItem.name
            tvListCreatingTime.text = shopListNameItem.time
            itemView.setOnClickListener {
            }
            btnDeleteList.setOnClickListener {
                listener.deleteItem(shopListNameItem.id!!)
            }
            btnEditList.setOnClickListener {
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

    class ItemComparator : DiffUtil.ItemCallback<ShopListNameItem>() {
        override fun areItemsTheSame(
            oldItem: ShopListNameItem, newItem: ShopListNameItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopListNameItem, newItem: ShopListNameItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(shopListName: ShopListNameItem)
        fun onClickItem(shopListName: ShopListNameItem)
    }
}