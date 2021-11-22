package com.example.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.model.UserData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_user.view.*

var followingFilterList = ArrayList<UserData>()

class FollowingAdapter(listUser: ArrayList<UserData>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    init {
        followingFilterList = listUser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_user, viewGroup, false)
        val sch = ListViewHolder(view)
        mainContext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = followingFilterList[position]
        holder.apply {
            Glide.with(holder.itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(200, 200))
                .into(imgAvatar)
            txtUsername.text = data.username
            txtName.text = data.name
        }
    }

    override fun getItemCount(): Int = followingFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgAvatar: CircleImageView = itemView.item_avatar
        var txtUsername: TextView = itemView.item_username
        var txtName: TextView = itemView.item_name
    }

}