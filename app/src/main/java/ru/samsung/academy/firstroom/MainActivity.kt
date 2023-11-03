package ru.samsung.academy.firstroom

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.samsung.academy.firstroom.adapter.SubscriberRecyclerViewAdapter
import ru.samsung.academy.firstroom.databinding.ActivityMainBinding
import ru.samsung.academy.firstroom.db.Subscriber
import ru.samsung.academy.firstroom.di.MyApplication
import ru.samsung.academy.firstroom.viewmodel.SubscriberViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: SubscriberRecyclerViewAdapter

    companion object {
        private const val TAG = "MY_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        val appContainer = (application as MyApplication).appContainer
        subscriberViewModel = ViewModelProvider(this,
            appContainer.subscriberViewModelFactory)[SubscriberViewModel::class.java]
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        
        subscriberViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let {message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        initRecyclerView()

    }
    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.subscriberRecyclerView.setHasFixedSize(true)
        adapter = SubscriberRecyclerViewAdapter {selectedItem : Subscriber -> listItemSelected(selectedItem)}
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displaySubscribersList() {
        subscriberViewModel.getSavedSubscribers().observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemSelected(subscriber: Subscriber) {
        subscriberViewModel.initUpdateAndDelete(subscriber)
        //Toast.makeText(this, "selected name is ${subscriber.name}", Toast.LENGTH_LONG).show()
    }
}