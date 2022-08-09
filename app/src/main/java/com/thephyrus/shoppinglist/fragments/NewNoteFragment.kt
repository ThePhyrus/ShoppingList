package com.thephyrus.shoppinglist.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.thephyrus.shoppinglist.R
import com.thephyrus.shoppinglist.activities.MainApp
import com.thephyrus.shoppinglist.databinding.ActivityNewNoteBinding
import com.thephyrus.shoppinglist.db.MainViewModel
import com.thephyrus.shoppinglist.entities.NoteItem
import com.thephyrus.shoppinglist.utils.HtmlManager
import com.thephyrus.shoppinglist.utils.MyTouchListener
import com.thephyrus.shoppinglist.utils.TimeManager

class NewNoteFragment: BaseFragment() {

    private var _binding: ActivityNewNoteBinding? = null
    private val binding: ActivityNewNoteBinding get() = _binding!!
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null
    private lateinit var defPref: SharedPreferences //lesson 55

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {

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
        _binding = ActivityNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defPref = PreferenceManager.getDefaultSharedPreferences(activity) //lesson 55
        actionBarSettings()
        init()
        setRazmerTexta() //todo осознать и переименовать:) См.урок №52
        getNote()
        onClickColorPicker()
        actionMenuCallback() //FIXME action menu всё равно появляется, если нажать на свободное место?
    }


    private fun onClickColorPicker() = with(binding) {
        imbRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        imbGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)
        }
        imbOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)
        }
        imbYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)
        }
        imbBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        imbBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(activity) //lesson 52
    }

    private fun getNote() {
        mainViewModel.noteUpdate.observe(viewLifecycleOwner){
            note = it
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
        edTitle.setText(note?.title)
        edDescription.setText(HtmlManager.getFromHtml(note?.content!!))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
            saveUpdateNote()
        } else if (item.itemId == android.R.id.home) {
            FragmentManager.setFragment(NoteFragment.newInstance(), activity as AppCompatActivity)
        } else if (item.itemId == R.id.id_bold) {
            setBoldForSelectedText()
        } else if (item.itemId == R.id.id_color) {
            if (binding.colorPicker.isShown) {
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd
        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(endPos) //FIXME was startPos
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])
        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(activity as AppCompatActivity, colorId)
            ),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(endPos) //FIXME was startPos
    }

    private fun saveUpdateNote() {
        if (note == null) {
            createNewNote()
        } else {
            updateNote()
        }
        FragmentManager.setFragment(NoteFragment.newInstance(), activity as AppCompatActivity)

    }

    private fun updateNote()  = with(binding) {
        val tempNote = note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )
        if (tempNote != null) {
            mainViewModel.updateNote(tempNote)
        }
    }

    private fun createNewNote()= with(binding) { // функция выдаёт заполненную заметку
       mainViewModel.insertNote(NoteItem(
           null,
           edTitle.text.toString(),
           HtmlManager.toHtml(edDescription.text),
           TimeManager.getCurrentTime(),
           ""
       ))
    }


    private fun actionBarSettings() {
        val ab = (activity as AppCompatActivity).supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(activity, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(activity, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun actionMenuCallback() {
        //FIXME action menu всё равно появляется, если назать на свободное место
        val actionMenuCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }
        }
        binding.edDescription.customSelectionActionModeCallback = actionMenuCallback
    }


    //lesson 52 (отличный пример использования экст-функции!)
    private fun EditText.setTextSize(size: String?) {
        if (size != null) this.textSize = size.toFloat()
    }

    private fun setRazmerTexta() = with(binding){
        edTitle.setTextSize(pref?.getString("title_text_size_key", "16"))
        edDescription.setTextSize(pref?.getString("content_text_size_key", "12"))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}