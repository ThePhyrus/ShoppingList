package com.example.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.NewNoteActivity
import com.example.shoppinglist.databinding.FragmentNoteBinding
import com.example.shoppinglist.db.MainViewModel

//todo добавить описание работы класса

private const val TAG: String = "@@@"

class NoteFragment : BaseFragment() {

    private var _binding: FragmentNoteBinding? = null //FIXME не будет ли утечки?
    private val binding: FragmentNoteBinding get() = _binding!!

    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "title: ${it.data?.getStringExtra(TITLE_KEY)} ")
                Log.d(TAG, "description: ${it.data?.getStringExtra(DESC_KEY)} ")
            }
        }
    }

    companion object { // нужен, чтобы сделать синглтон (будет только одна INSTANCE фрагмента)
        const val TITLE_KEY = "title_key"
        const val DESC_KEY = "desc_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}