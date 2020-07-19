package com.cloudnine.roomdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cloudnine.roomdemo.db.SubscriberRepository
import java.lang.IllegalArgumentException

class SubscriberViewModelFactory(val _subscriberRepo: SubscriberRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(SubscriberViewModel::class.java))
       {
           return SubscriberViewModel(_subscriberRepo) as T
       }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}