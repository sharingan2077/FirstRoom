package ru.samsung.academy.firstroom.di

import android.content.Context
import ru.samsung.academy.firstroom.db.SubscriberDatabase
import ru.samsung.academy.firstroom.repository.SubscriberRepository
import ru.samsung.academy.firstroom.viewmodel.SubscriberViewModelFactory

class AppContainer(applicationContext: Context) {


    private val subscriberDatabase = SubscriberDatabase.getInstance(applicationContext)
    private val subscriberRepository = SubscriberRepository(subscriberDatabase.subscriberDAO)

    val subscriberViewModelFactory = SubscriberViewModelFactory(subscriberRepository)

}