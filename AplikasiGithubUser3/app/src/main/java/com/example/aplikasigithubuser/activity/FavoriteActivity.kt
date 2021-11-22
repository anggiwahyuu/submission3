package com.example.aplikasigithubuser.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.FavoriteAdapter
import com.example.aplikasigithubuser.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.aplikasigithubuser.helper.MapHelper
import com.example.aplikasigithubuser.model.Favorite
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setActionBarTitle()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycleViewFav.layoutManager = LinearLayoutManager(this)
        recycleViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recycleViewFav.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun setActionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = "My Favorites"
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFav.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MapHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            progressBarFav.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listFavorite = favData
            } else {
                adapter.listFavorite = ArrayList()
                showSnackMessage()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackMessage() {
        Toast.makeText(this, getString(R.string.empty_favorite), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}