package com.thephyrus.shoppinglist.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.thephyrus.shoppinglist.R
import com.thephyrus.shoppinglist.activities.MainApp
import com.thephyrus.shoppinglist.databinding.ActivityShopListBinding
import com.thephyrus.shoppinglist.db.MainViewModel
import com.thephyrus.shoppinglist.db.ShopListItemAdapter
import com.thephyrus.shoppinglist.dialogs.DeleteDialog
import com.thephyrus.shoppinglist.dialogs.EditListItemDialog
import com.thephyrus.shoppinglist.entities.LibraryItem
import com.thephyrus.shoppinglist.entities.ShopListItem
import com.thephyrus.shoppinglist.entities.ShopListNameItem
import com.thephyrus.shoppinglist.utils.ShareHelper

class ShopListFragment : BaseFragment(), ShopListItemAdapter.Listener {

    private var _binding: ActivityShopListBinding? = null
    private val binding: ActivityShopListBinding get() = _binding!!
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private lateinit var defPref: SharedPreferences
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher //lesson 43

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        defPref = PreferenceManager.getDefaultSharedPreferences(activity)
        initShopListNameItemObserver()
        initRcView()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.etNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher() //lesson 43
    }

    private fun textWatcher(): TextWatcher { //lesson 43
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(word: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("@@@", "onTextChanged: $word ")
                mainViewModel.getAllLibraryItems("%$word%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //FIXME HARDCODE!
        when (item.itemId) {
            R.id.save_item -> { //todo add dialog??
                addNewShopItem(edItem?.text.toString())
            }
            R.id.delete_list -> { //todo add delete dialog??
                mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
                FragmentManager.setFragment(
                    ShopListNamesFragment.newInstance(),
                    activity as AppCompatActivity
                )
            }
            R.id.clear_list -> { //todo add clear dialog??
                mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
            }
            R.id.share_list -> { //todo add share dialog??
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareShopList(adapter?.currentList!!, shopListNameItem?.name!!),
                        "Share by"
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem(name: String) {
        if (name.isEmpty()) return
        val item = ShopListItem(
            null,
            name,
            "",
            false,
            shopListNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }


    private fun listItemObserver() { //вариант, предложенный студией //FIXME разница 1?
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!)
            .observe(viewLifecycleOwner) {
                adapter?.submitList(it)
                binding.tvEmpty.visibility = if (it.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
    }


    private fun libraryItemObserver() { //lesson 45 //FIXME вариант студии 2
        mainViewModel.libraryItems.observe(this) {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach { item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = ShopListItemAdapter(this@ShopListFragment)
        rcViewRename.layoutManager = LinearLayoutManager(activity)
        rcViewRename.adapter = adapter
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher) //lesson 43
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!)
                    .removeObservers(this@ShopListFragment)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher) //lesson 43
                (activity as AppCompatActivity).invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShopListFragment)
                edItem?.setText("")
                listItemObserver()
                return true
            }

        }
    }

    private fun initShopListNameItemObserver() {
        mainViewModel.shopListNameItemUpdate.observe(viewLifecycleOwner){
            shopListNameItem = it
            listItemObserver()
        }
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem, state: Int) { // lesson 39
        when (state) {
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
            ShopListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(shopListItem)
            ShopListItemAdapter.SELECT_LIBRARY_ITEM -> addNewShopItem(shopListItem.name)
            ShopListItemAdapter.DELETE -> {
                DeleteDialog.showDialog(
                    activity as AppCompatActivity,
                    object : DeleteDialog.Listener {
                        override fun onClick() {//FIXME какой урок?
//                        shopListItem.id?.let { mainViewModel.deleteShopItem(it) }
                        }

                    })
            }
            ShopListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        }

    }

    private fun editListItem(item: ShopListItem) {
        EditListItemDialog.showDialog(
            activity as AppCompatActivity,
            item,
            object : EditListItemDialog.Listener {
                override fun onClick(item: ShopListItem) {
                    mainViewModel.updateListItem(item)
                }
            })
    }

    private fun editLibraryItem(item: ShopListItem) {
        EditListItemDialog.showDialog(
            activity as AppCompatActivity,
            item,
            object : EditListItemDialog.Listener {
                override fun onClick(item: ShopListItem) {
                    mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                    mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
                }
            })
    }

    private fun saveItemCount() {
        var checkedItemCounter = 0
        adapter?.currentList?.forEach {
            if (it.itemChecked) checkedItemCounter++
        }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedItemCounter
        )
        mainViewModel.updateListName(tempShopListNameItem!!)
    }

    override fun onDetach() {
        super.onDetach()
        saveItemCount()
    }


    override fun onClickNew() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}