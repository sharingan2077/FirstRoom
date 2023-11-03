package ru.samsung.academy.firstroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.samsung.academy.firstroom.repository.SubscriberRepository
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class SubscriberViewModelFactory(private val subscriberRepository: SubscriberRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriberViewModel::class.java)) {
            return SubscriberViewModel(subscriberRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }

}