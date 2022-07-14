package com.example.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.EditListItemDialogBinding
import com.example.shoppinglist.databinding.NewListDialogBinding
import com.example.shoppinglist.entities.ShopListItem

object EditListItemDialog { //lesson 40 //todo ограничить кол-во символов в edInfo

    fun showDialog(context: Context, item: ShopListItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        dialogBuilder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            btnUpdateItem.setOnClickListener {
                if (edName.text.toString().isNotEmpty()) {
                    listener.onClick(
                        item.copy(
                            name = edName.text.toString(),
                            itemInfo = edInfo.text.toString()
                        )
                    )
                }
                dialog?.dismiss()
            }
        }
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ShopListItem)
    }
}