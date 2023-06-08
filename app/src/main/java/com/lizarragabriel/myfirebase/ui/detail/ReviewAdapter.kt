package com.lizarragabriel.myfirebase.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizarragabriel.myfirebase.databinding.ReviewItemBinding
import com.lizarragabriel.myfirebase.entity.Review

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    private var list: List<Review> = emptyList()

    fun setList(newList: List<Review>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: ReviewItemBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.review = review
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.MyViewHolder {
        return MyViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReviewAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}