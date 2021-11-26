package com.example.aplikasigithubuser.ui.controller

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.database.FavoriteUserDao
import com.example.aplikasigithubuser.database.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getFavoriteUser()
}