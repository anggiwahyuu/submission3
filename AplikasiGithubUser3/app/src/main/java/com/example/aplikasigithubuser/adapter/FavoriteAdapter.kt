package com.example.aplikasigithubuser.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.activity.CustomItemClick
import com.example.aplikasigithubuser.activity.DetailActivity
import com.example.aplikasigithubuser.model.Favorite
import com.example.aplikasigithubuser.model.UserData
import kotlinx.android.synthetic.main.row_user.view.*
import java.util.ArrayList

@Suppress("DEPRECATION")
class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.NoteViewHolder>() {
    var listFavorite = ArrayList<Favorite>()
        @SuppressLint("NotifyDataSetChanged")
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions())
                    .into(itemView.item_avatar)
                item_username.text = fav.username
                item_name.text = fav.name
                val dataUser = UserData(
                    fav.username,
                    fav.name,
                    fav.avatar,
                    fav.company,
                    fav.location,
                    fav.repository,
                    fav.followers,
                    fav.following,
                    fav.isFav
                )
                itemView.setOnClickListener (
                    CustomItemClick(
                        adapterPosition,
                        object : CustomItemClick.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_DATA, dataUser)
                                intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                                intent.putExtra(DetailActivity.EXTRA_NOTE, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }
}