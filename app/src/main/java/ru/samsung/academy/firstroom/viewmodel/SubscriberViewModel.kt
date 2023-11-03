package ru.samsung.academy.firstroom.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.samsung.academy.firstroom.db.Subscriber
import ru.samsung.academy.firstroom.repository.SubscriberRepository
import kotlin.math.E

class SubscriberViewModel(private val subscriberRepository: SubscriberRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
        get() = statusMessage

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber
    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            updateSubscriber(subscriberToUpdateOrDelete)
        }
        else {
            if (inputName.value == null) {
                statusMessage.value = Event("Please enter subscriber's name")
            }
            else if (inputEmail.value == null) {
                statusMessage.value = Event("Please enter subscriber's email")
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
                statusMessage.value = Event("Please enter a correct email address")
            }
            else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insertSubscriber(Subscriber(0, name, email))
                inputName.value = ""
                inputEmail.value = ""
            }
        }

    }
    fun clearOfDelete() {
        if (isUpdateOrDelete) {
            deleteSubscriber(subscriberToUpdateOrDelete)
        }
        else {
            clearAll()
        }
    }

    private fun deleteSubscriber(subscriber: Subscriber) {
        viewModelScope.launch{
            val rowDeleted = subscriberRepository.delete(subscriber)
            if (rowDeleted > 0) {
                setDefaultViewCondition()
                statusMessage.value = Event("$rowDeleted Row Deleted Successfully")
            }
            else {
                statusMessage.value = Event("Error occurred")
            }
        }

    }

    private fun insertSubscriber(subscriber: Subscriber) =
        viewModelScope.launch {
            val newRowId = subscriberRepository.insert(subscriber)
            if (newRowId > -1) {
                statusMessage.value = Event("Successfully Inserted Subscriber $newRowId")
            }
            else {
                statusMessage.value = Event("Error occurred")
            }
        }
    private fun clearAll() =
        viewModelScope.launch {
            val numberRowsDeleted = subscriberRepository.deleteAll()
            if (numberRowsDeleted > 0) {
                statusMessage.value = Event("$numberRowsDeleted Subscribers Deleted Successfully")
            }
            else {
                statusMessage.value = Event("Error occurred")
            }
        }

    fun getSavedSubscribers() = liveData {
        subscriberRepository.subscribers.collect {
            emit(it)
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"
    }
    private fun updateSubscriber(subscriber: Subscriber) {
        viewModelScope.launch {
            val rowsUpdated = subscriberRepository.update(subscriber)
            if (rowsUpdated > 0) {
                setDefaultViewCondition()
                statusMessage.value = Event("$rowsUpdated Row Update Successfully")
            }
            else {
                statusMessage.value = Event("Error occurred")
            }
        }
    }

    private fun setDefaultViewCondition() {
        inputName.value = ""
        inputEmail.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

}