package com.cloudnine.roomdemo

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudnine.roomdemo.db.Subscriber
import com.cloudnine.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(val _subscriberRepo: SubscriberRepository): ViewModel(), Observable {

    private var isUpDateOrDelete:Boolean =false
    private lateinit var subscriberToUpdateDelete:Subscriber
    val subscribers = _subscriberRepo.subscribers
    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAll = MutableLiveData<String>()

    private val statuseMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
    get() = statuseMessage

    init {
        saveOrUpdateButtonText.value="SAVE"
        clearAll.value="CLEAR"
    }

    fun SaveOrUpdate()
    {
        if(inputName.value == null){
            statuseMessage.value=Event("Please Enter Subscribers Name")
        }else if(inputEmail.value == null)
        {
            statuseMessage.value=Event("Please Enter Subscribers Email")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches())
        {
            statuseMessage.value=Event("Please Enter Email Address in Correct Format")
        }else {

            if (isUpDateOrDelete) {
                subscriberToUpdateDelete.name = inputName.value!!
                subscriberToUpdateDelete.email = inputEmail.value!!
                Log.i(
                    "SubcrierViewModel",
                    "${subscriberToUpdateDelete.name}${subscriberToUpdateDelete.email}"
                )
                update(subscriberToUpdateDelete)
            } else {
                val name: String = inputName.value!!
                val email: String = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputEmail.value = null
                inputName.value = null
            }
        }
    }

    fun clear()
    {
        if(isUpDateOrDelete)
        {
            delete(subscriberToUpdateDelete)
        }else {
            deleteSubscribers()
        }
    }

    fun insert(subscriber: Subscriber):Job=    viewModelScope.launch {
        val result:Long = _subscriberRepo.insert(subscriber)
        if(result > -1)
        {
            statuseMessage.value=Event("Subscriber ${subscriber.name} is inserted sucessfully")
        }else
        {
            statuseMessage.value=Event("Error Occured in adding ${subscriber.name}")
        }

        }



     fun update(subscriber: Subscriber):Job=viewModelScope.launch {
             val result =   _subscriberRepo.update(subscriber)
         if(result > 0) {
             inputName.value = null
             inputEmail.value = null
             isUpDateOrDelete = false
             saveOrUpdateButtonText.value = "SAVE"
             clearAll.value = "CLEAR"
             statuseMessage.value = Event("Subscriber ${subscriber.name} is updated sucessfully")
         }else{
             statuseMessage.value=Event("Error Occured in adding ${subscriber.name}")
         }
        }



     fun delete(subscriber: Subscriber):Job=    viewModelScope.launch {

         val result = _subscriberRepo.delete(subscriber)
         if (result > 0) {
             inputName.value = null
             inputEmail.value = null
             isUpDateOrDelete = false
             saveOrUpdateButtonText.value = "SAVE"
             clearAll.value = "CLEAR"
             statuseMessage.value = Event("Subscriber ${subscriber.name} is deleted sucessfully")
         } else {
             statuseMessage.value=Event("Error Occured in deleting ${subscriber.name}")
         }
     }


     fun deleteSubscribers(): Job = viewModelScope.launch {


         val result =  _subscriberRepo.deleteSubscribers()
         if (result > 0) {
             statuseMessage.value = Event("All Subscribers are deleted sucessfully")
         }else
         {
             statuseMessage.value=Event("Error Occured in deleting all")
         }
            }

    fun initUpdateDelete(subscriber:Subscriber)
    {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpDateOrDelete = true
        subscriberToUpdateDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
            clearAll.value = "Delete"

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}