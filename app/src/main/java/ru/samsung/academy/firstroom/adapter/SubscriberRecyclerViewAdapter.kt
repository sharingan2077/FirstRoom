package ru.samsung.academy.firstroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.samsung.academy.firstroom.R
import ru.samsung.academy.firstroom.databinding.ListItemBinding
import ru.samsung.academy.firstroom.db.Subscriber

class SubscriberRecyclerViewAdapter(private val clickListener: (Subscriber) -> Unit)
    : RecyclerView.Adapter<SubscriberViewHolder>() {

    private val subscribersList = ArrayList<Subscriber>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return SubscriberViewHolder(binding)

    }
    fun setList(subscribers : List<Subscriber>) {
        subscribersList.clear()
        subscribersList.addAll(subscribers)
    }


    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscribersList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }
}


class SubscriberViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
        //binding.root.layoutParams.width = LayoutParams.MATCH_PARENT
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.cardView.setOnClickListener {
            clickListener(subscriber)
        }

    }
}