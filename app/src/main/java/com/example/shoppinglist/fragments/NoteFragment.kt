package com.example.shoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentNoteBinding

//todo добавить описание работы класса
class NoteFragment : BaseFragment() {

    private var _binding: FragmentNoteBinding? = null //FIXME не будет ли утечки?
    private val binding: FragmentNoteBinding get() = _binding!!

    override fun onClickNew() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object { // нужен, чтобы сделать синглтон (будет только одна INSTANCE фрагмента)
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}