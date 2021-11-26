package com.example.aplikasigithubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.RowUserBinding
import com.example.aplikasigithubuser.model.ItemsItem

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val listUser = ArrayList<ItemsItem>()

    private var onItemClickCallback: OnItemClickCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<ItemsItem>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(itemAvatar)
                itemUsername.text = user.login
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: ItemsItem)
    }
}