package com.example.aplikasigithubuser.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.adapter.UserAdapter
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.example.aplikasigithubuser.model.ItemsItem
import com.example.aplikasigithubuser.ui.controller.FavoriteViewModel
import com.example.aplikasigithubuser.ui.controller.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model = obtainViewModel(this@FavoriteActivity)

        val layoutManager = LinearLayoutManager(this)
        binding.userGithubFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.userGithubFavorite.addItemDecoration(itemDecoration)
        val adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.userGithubFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        model.getAllFavoriteUser().observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<ItemsItem> {
        val listUsers = ArrayList<ItemsItem>()
        for (user in users) {
            val userMapped = ItemsItem(
                user.avatar_url,
                user.id,
                user.login
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}