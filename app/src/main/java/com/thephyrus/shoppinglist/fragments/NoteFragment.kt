package com.thephyrus.shoppinglist.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thephyrus.shoppinglist.activities.MainApp
import com.thephyrus.shoppinglist.databinding.FragmentNoteBinding
import com.thephyrus.shoppinglist.db.MainViewModel
import com.thephyrus.shoppinglist.db.NoteAdapter
import com.thephyrus.shoppinglist.entities.NoteItem

//todo добавить описание работы класса


class NoteFragment : BaseFragment(), NoteAdapter.Listener {

    private var _binding: FragmentNoteBinding? = null //FIXME не будет ли утечки?
    private val binding: FragmentNoteBinding get() = _binding!!

    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter
    private lateinit var defPref:SharedPreferences //lesson 53

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        FragmentManager.setFragment(NewNoteFragment(), activity as AppCompatActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initRcView()
        observer()

    }

    private fun initRcView() = with(binding) {
        defPref = PreferenceManager.getDefaultSharedPreferences(activity) //lesson 53
        rcViewNote.layoutManager = getLayoutManager() //changed on lesson 54
        adapter = NoteAdapter(this@NoteFragment, defPref)
        rcViewNote.adapter = adapter
    }

    private fun getLayoutManager() : RecyclerView.LayoutManager { //lesson 54
        return if (defPref.getString("note_style_key", "Linear") == "Linear") {
            LinearLayoutManager(activity)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun observer() { //FIXME ???
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
    mainViewModel.noteUpdate.value = note
        FragmentManager.setFragment(NewNoteFragment(), activity as AppCompatActivity)
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}