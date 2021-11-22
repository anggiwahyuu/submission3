package com.example.aplikasigithubuser.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.activity.DetailActivity
import com.example.aplikasigithubuser.model.UserData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_user.view.*
import java.util.*

var userFilterList = ArrayList<UserData>()

@SuppressLint("StaticFieldLeak")
lateinit var mainContext: Context

class UserAdapter(private var listData: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>(),
    Filterable {
    init {
        userFilterList = listData
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_user, viewGroup, false)
        val sch = ListViewHolder(view)
        mainContext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = userFilterList[position]
        Glide.with(holder.itemView.context)
            .load(data.avatar)
            .apply(RequestOptions())
            .into(holder.imgAvatar)
        holder.txtUsername.text = data.username
        holder.txtName.text = data.name
        holder.itemView.setOnClickListener {
            val dataUser = UserData(
                data.username,
                data.name,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following,
                data.isFav
            )
            val intentDetail = Intent(mainContext, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_DATA, dataUser)
            intentDetail.putExtra(DetailActivity.EXTRA_FAV, dataUser)
            mainContext.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: UserData)
    }

    override fun getItemCount(): Int = userFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar: CircleImageView = itemView.item_avatar
        var txtUsername: TextView = itemView.item_username
        var txtName: TextView = itemView.item_name
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                userFilterList = if (charSearch.isEmpty()) {
                    listData
                } else {
                    val resultList = ArrayList<UserData>()
                    for (row in userFilterList) {
                        if ((row.username.toString().lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                UserData(
                                    row.username,
                                    row.name,
                                    row.avatar,
                                    row.company,
                                    row.location,
                                    row.repository,
                                    row.followers,
                                    row.following
                                )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                userFilterList = results.values as ArrayList<UserData>
                notifyDataSetChanged()
            }
        }
    }

}