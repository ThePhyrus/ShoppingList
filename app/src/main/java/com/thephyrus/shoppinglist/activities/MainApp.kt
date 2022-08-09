package com.thephyrus.shoppinglist.activities

import android.app.Application
import com.thephyrus.shoppinglist.db.MainDataBase

/*
Класс создан для того, чтобы создать INSTANCE на уровне всего приложения. Не важно какие активити
будут открываться/закрываться - этот класс запустится однажды и будет работать, пока не закрыто само
приложение. Все свойства, которые есть внутри класса, будут доступны. В этом классе будет сделанна
инициализация базы данных и доступ к ней будет открыт из любой активити. Не нужно будет в каждой
из них отдельно получать INSTANCE.
 */

class MainApp : Application() {
    /*
    Код работет следующим образом:
    Когда database пустая (null), блок lazy запустится один раз и вернёт INSTANCE (отработает
    функция getDataBase()). Как работает эта функция см.в комментариях класса MainDataBase.kt.

    Если по каким-то причинам этот код запусится второй раз, то в database уже будет сидеть
    INSTANCE и блок lazy выполняться не будет.
     */
    //инициализация базы данных
    val database by lazy { MainDataBase.getDataBase(this) }
}