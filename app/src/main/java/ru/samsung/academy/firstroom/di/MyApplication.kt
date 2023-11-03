package ru.samsung.academy.firstroom.di

import android.app.Application

class MyApplication : Application() {

    val appContainer : AppContainer by lazy {
        AppContainer(applicationContext)
    }
}