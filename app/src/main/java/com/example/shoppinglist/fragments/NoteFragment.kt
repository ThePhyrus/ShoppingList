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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.R
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.NewNoteActivity
import com.example.shoppinglist.databinding.FragmentNoteBinding
import com.example.shoppinglist.db.MainViewModel
import com.example.shoppinglist.db.NoteAdapter
import com.example.shoppinglist.entities.NoteItem

//todo добавить описание работы класса

private const val TAG: String = "@@@"

class NoteFragment : BaseFragment() {

    private var _binding: FragmentNoteBinding? = null //FIXME не будет ли утечки?
    private val binding: FragmentNoteBinding get() = _binding!!

    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // функция запустится когда все вью будут созданны
        super.onViewCreated(view, savedInstanceState)
        // только теперь можно запустить функцию initRcView
        initRcView()
        observer()

    }

    private fun initRcView() = with(binding) {
        // здесь инициализирую мой ресайкл вью и адаптер
        rcViewNote.layoutManager = LinearLayoutManager(activity) // заметки будут идти вертикально
        adapter = NoteAdapter() // инициализация адаптера
        rcViewNote.adapter = adapter // передаём адаптер куда следует
    }

    private fun observer() { //FIXME ???
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
            }
        }
    }

    companion object { // нужен, чтобы сделать синглтон (будет только одна INSTANCE фрагмента)
        const val NEW_NOTE_KEY = "new_note_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}