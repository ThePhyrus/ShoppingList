package com.example.shoppinglist.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.databinding.FragmentShopListNamesBinding
import com.example.shoppinglist.db.MainViewModel
import com.example.shoppinglist.db.ShopListNameAdapter
import com.example.shoppinglist.dialogs.DeleteDialog
import com.example.shoppinglist.dialogs.NewListDialog
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.entities.ShoppingListName
import com.example.shoppinglist.utils.TimeManager

private const val TAG: String = "@@@"

class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {

    private var _binding: FragmentShopListNamesBinding? = null //FIXME не будет ли утечки?
    private val binding: FragmentShopListNamesBinding get() = _binding!!

    private lateinit var adapter: ShopListNameAdapter


    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()

    }

    private fun initRcView() = with(binding) {
        myRcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        myRcView.adapter = adapter
    }

    private fun observer() { //FIXME ???
        mainViewModel.allShopListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopListName(id)
            }

        })
    }

    override fun editItem(shopListName: ShoppingListName) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateListName(shopListName.copy(name = name))
            }
        }, shopListName.name)
    }

    override fun onClickItem(shopListName: ShoppingListName) {
        TODO("Not yet implemented")
    }
}