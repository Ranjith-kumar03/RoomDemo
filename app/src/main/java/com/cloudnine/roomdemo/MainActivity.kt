package com.cloudnine.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudnine.roomdemo.databinding.ActivityMainBinding
import com.cloudnine.roomdemo.db.Subscriber
import com.cloudnine.roomdemo.db.SubscriberDAO
import com.cloudnine.roomdemo.db.SubscriberDataBase
import com.cloudnine.roomdemo.db.SubscriberRepository
//https://www.youtube.com/watch?v=v2yocpEcE_g
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao:SubscriberDAO = SubscriberDataBase.getInstance(application).SubscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myViewModel=subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun initRecyclerView()
    {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
         adapter = MyRecyclerViewAdapter({selectedItem:Subscriber -> listItemClicked(selectedItem)})
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()
    }




    private fun displaySubscribersList()
    {
        subscriberViewModel.subscribers.observe(this, Observer {
                it-> Log.i("Mytag",it.toString())
 adapter.setList(it)
            adapter.notifyDataSetChanged()
        })

    }

    private fun listItemClicked(subscriber: Subscriber){
        subscriberViewModel.initUpdateDelete(subscriber)
      //Toast.makeText(this, "Name of the Subscriber is ${subscriber.name}",Toast.LENGTH_SHORT).show()
    }
}