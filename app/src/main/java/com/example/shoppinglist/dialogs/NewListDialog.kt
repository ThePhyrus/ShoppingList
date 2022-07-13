package com.example.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.shoppinglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        dialogBuilder.setView(binding.root)
        binding. apply {
            btnCrateList.setOnClickListener {
                val listName = edNewListName.text.toString()
                if (listName.isNotEmpty()){
                    listener.onClick(listName)
                }
                dialog?.dismiss()
            }
        }
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(name:String)
    }
}