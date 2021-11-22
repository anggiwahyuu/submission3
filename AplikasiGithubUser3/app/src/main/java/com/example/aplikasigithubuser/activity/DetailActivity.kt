package com.example.aplikasigithubuser.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.SectionsPagerAdapter
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.AVATAR
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.COMPANY
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.FAVORITE
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.LOCATION
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.NAME
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.REPOSITORY
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.aplikasigithubuser.database.FavoriteHelper
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.model.Favorite
import com.example.aplikasigithubuser.model.UserData
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding

    private var isFavorite = false
    private lateinit var gitHelper: FavoriteHelper
    private var favorites: Favorite? = null
    private lateinit var imageAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gitHelper = FavoriteHelper.getInstance(applicationContext)
        gitHelper.open()

        favorites = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorites != null) {
            setDataObject()
            isFavorite = true
            val checked: Int = R.drawable.ic_baseline_favorite
            btn_favorite.setImageResource(checked)
        } else {
            setData()
        }

        viewPagerConfig()
        btn_favorite.setOnClickListener(this)
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra(EXTRA_DATA) as UserData?
        dataUser?.name?.let {this.title = it}
        detail_name.text = dataUser?.name
        detail_username.text = dataUser?.username
        detail_repository.text = getString(R.string.repository, dataUser?.repository)
        detail_location.text = getString(R.string.location, dataUser?.location)
        detail_company.text = getString(R.string.company, dataUser?.company)
        Glide.with(this)
            .load(dataUser?.avatar)
            .apply(RequestOptions().override(300, 300))
            .into(detail_avatar)
        imageAvatar = dataUser?.avatar.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            this.title = title
        }else{
            this.title = "empty username"
        }
    }

    private fun setDataObject() {
        val favoriteUser = intent.getParcelableExtra(EXTRA_NOTE) as Favorite?
        favoriteUser?.name?.let { setActionBarTitle(it) }
        detail_name.text = favoriteUser?.name
        detail_username.text = favoriteUser?.username
        detail_company.text = favoriteUser?.company
        detail_location.text = favoriteUser?.location
        detail_repository.text = favoriteUser?.repository
        Glide.with(this)
            .load(favoriteUser?.avatar)
            .into(detail_avatar)
        imageAvatar = favoriteUser?.avatar.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(view: View) {
        val checked: Int = R.drawable.ic_baseline_favorite
        val unChecked: Int = R.drawable.ic_baseline_favorite_border
        if (view.id == R.id.btn_favorite) {
            if (isFavorite) {
                gitHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(unChecked)
                isFavorite = false
            } else {
                val dataUsername = detail_username.text.toString()
                val dataName = detail_name.text.toString()
                val dataAvatar = imageAvatar
                val dataCompany = detail_company.text.toString()
                val dataLocation = detail_location.text.toString()
                val dataRepository = detail_repository.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, dataCompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FAVORITE, dataFavorite)

                isFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                btn_favorite.setImageResource(checked)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_FAV = "extra_data"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

}
