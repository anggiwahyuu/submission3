package com.example.aplikasigithubuser.helper

import android.database.Cursor
import com.example.aplikasigithubuser.database.DatabaseContract
import com.example.aplikasigithubuser.model.Favorite
import java.util.ArrayList

object MapHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.REPOSITORY))
                val favorite =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.FAVORITE))
                favoriteList.add(
                    Favorite(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        favorite
                    )
                )
            }
        }
        return favoriteList
    }
}