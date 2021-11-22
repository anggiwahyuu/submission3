package com.example.aplikasigithubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.adapter.UserAdapter
import com.example.aplikasigithubuser.adapter.userFilterList
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import com.example.aplikasigithubuser.helper.PrefHelper
import com.example.aplikasigithubuser.model.UserData
import com.example.aplikasigithubuser.token.ACCESS_TOKEN
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var listData: ArrayList<UserData> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private val pref by lazy { PrefHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Users search ..."

        searchUser()
        getUsers()
        adapter = UserAdapter(listData)
        recyclerViewConfig()

        if (pref.getBoolean("pref_is_dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun getUsers() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $ACCESS_TOKEN")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun recyclerViewConfig() {
        userGithub.layoutManager = LinearLayoutManager(userGithub.context)
        userGithub.setHasFixedSize(true)
        userGithub.addItemDecoration(
            DividerItemDecoration(
                userGithub.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getUserDetail(id: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $ACCESS_TOKEN")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String? = jsonObject.getString("login")
                    val name: String? = jsonObject.getString("name")
                    val avatar: String? = jsonObject.getString("avatar_url")
                    val company: String? = jsonObject.getString("company")
                    val location: String? = jsonObject.getString("location")
                    val repository: String? = jsonObject.getString("public_repos")
                    val followers: String? = jsonObject.getString("followers")
                    val following: String? = jsonObject.getString("following")
                    listData.add(
                        UserData(
                            username,
                            name,
                            avatar,
                            company,
                            location,
                            repository,
                            followers,
                            following
                        )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showRecyclerList() {
        userGithub.layoutManager = LinearLayoutManager(this)
        val listDataAdapter = UserAdapter(userFilterList)
        userGithub.adapter = adapter

        listDataAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: UserData) {
                showSelectedUser(dataUsers)
            }
        })
    }

    private fun searchUser() { search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()) {
                return true
            } else {
                listData.clear()
                getUserSearch(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    })
    }

    private fun getUserSearch(id: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $ACCESS_TOKEN")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getUserDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun showSelectedUser(dataUser: UserData) {
        UserData(
            dataUser.username,
            dataUser.name,
            dataUser.avatar,
            dataUser.company,
            dataUser.location,
            dataUser.repository,
            dataUser.followers,
            dataUser.following
        )
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, dataUser)

        this@MainActivity.startActivity(intent)
        Toast.makeText(
            this,
            "${dataUser.name}",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_setting -> {
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}