package com.lizarragabriel.myfirebase.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizarragabriel.myfirebase.databinding.NameItemBinding
import com.lizarragabriel.myfirebase.entity.Restaurant

class MyAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var list: List<Restaurant> = emptyList()

    var onItemDetail: (Restaurant) -> Unit = {}
    var onItemCard: (Restaurant) -> Unit = {}

    fun setList(newList: List<Restaurant>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: NameItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.restaurant = restaurant
            binding.mVerMas.setOnClickListener {
                onItemDetail(restaurant)
            }
            binding.card.setOnClickListener {
                onItemCard(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        return MyViewHolder(NameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}