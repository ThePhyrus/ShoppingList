package com.thephyrus.shoppinglist.fragments

import androidx.fragment.app.Fragment

// вспомогательный класс 1, который поможет управлять фрагментами
// классы-наследники будут иметь fun onClickNew(), но она будет выполнять разные действия
// полиморфизм в действии!!! ))
abstract class BaseFragment : Fragment() {
    abstract fun onClickNew()
}