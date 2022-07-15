package com.example.shoppinglist.utils

import android.content.Intent
import com.example.shoppinglist.entities.ShopListItem
import java.lang.StringBuilder

object ShareHelper { //lesson 42

    fun shareShopList(shopList: List<ShopListItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, listName))
        }
        return intent
    }

    private fun makeShareText(shopList: List<ShopListItem>, listName: String): String {
        val strBuilder = StringBuilder()
        strBuilder.append("<<$listName>>")
        strBuilder.append("\n")
        var counter = 0
        shopList.forEach {
            strBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            strBuilder.append("\n")
        }
        return strBuilder.toString()
    }
}