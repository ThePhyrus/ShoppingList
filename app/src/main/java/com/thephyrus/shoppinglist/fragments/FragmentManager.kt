package com.thephyrus.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.thephyrus.shoppinglist.R


// вспомогательный класс 2, который поможет управлять фрагментами
// служит для переключения между фрагментами

object FragmentManager {

    /*
    "object" для того, чтобы можно было использовать fun setFragment() без создания экземпляра
    класса  FragmentManager.kt
     */

    var currentFrag: BaseFragment? = null
    /*
    Назначение currentFrag:
    Когда в активити будет помещён фрагмент, необходимо будет знать, какой именно фрагмент работает
    в ней на данный момент. Если нужно будет поместить другой фрагмент, то один (рабочий) убирается,
    а на его место помещается другой.
     */

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {

        /*
        При помощи этой функции можно будет переключаться между фрагментами. Аргументами функции
        будут новый фрагмент, который нужно поместить в активити и контекст этой активити. Все
        фрагменты, котрые можно будет сюда передавать, должны быть наследниками нашего
        BaseFragment.kt (это у него одна наша функция, которая будет выполнять разные действия).
         */

        val transaction = activity.supportFragmentManager.beginTransaction() //переключает фрагменты
        transaction.replace(R.id.placeHolder, newFrag) // указатель места, куда поместить фрагмент
        transaction.commit() // подтвердить транзакцию (без этого счастья не будет)
        currentFrag = newFrag // помещаем новый фрагмент в currentFrag
    }

    fun openSettings(newFrag: Fragment, activity: AppCompatActivity) {

        /*
        При помощи этой функции открывается фрагмент с настройками. Аргументами функции
        будут новый фрагмент, который нужно поместить в активити и контекст этой активити.
         */

        val transaction = activity.supportFragmentManager.beginTransaction() //переключает фрагменты
        transaction.replace(R.id.placeHolder, newFrag) // указатель места, куда поместить фрагмент
        transaction.commit() // подтвердить транзакцию (без этого счастья не будет)
        currentFrag = null // НЕ помещаем новый фрагмент в currentFrag
    }
}