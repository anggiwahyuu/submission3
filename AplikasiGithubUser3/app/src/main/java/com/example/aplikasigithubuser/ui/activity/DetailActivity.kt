package com.example.aplikasigithubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.SectionPagerAdapter
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.ui.controller.DetailViewModel
import com.example.aplikasigithubuser.ui.controller.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model = obtainViewModel(this@DetailActivity)

        val username = intent.getStringExtra(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra((EXTRA_AVATAR_URL))

        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        val sectionsPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        if (username != null) {
            model.setUserDetail(username)
        }

        model.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(profilePhoto)
                    profileName.text = it.name
                    profileUsername.text = it.login
                    followers.text = "${it.followers}"
                    following.text = "${it.following}"
                }
            }
        }

        var checked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = model.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.toggleFavorite.isChecked = true
                    checked = true
                } else {
                    binding.toggleFavorite.isChecked = false
                    checked = false
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            checked = !checked
            if (checked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                        model.addFavorite(username, id, avatarUrl)
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                model.deleteFavorite(id)
            }
            binding.toggleFavorite.isChecked = checked
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

private operator fun Unit.compareTo(i: Int): Int {
    return i
}