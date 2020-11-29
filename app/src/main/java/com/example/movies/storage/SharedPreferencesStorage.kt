package com.example.movies.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.movies.BuildConfig
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context): Storage<String, String> {

    private var storage: SharedPreferences = context.getSharedPreferences(
        BuildConfig.APPLICATION_ID,
        Context.MODE_PRIVATE
    )

    override suspend fun getObjectAtLocation(key: String): String? {
        return storage.getString(key, "")
    }

    override suspend fun setObjectAtLocation(key: String, value: String): Boolean {
        return try {
            storage.edit().putString(key, value).apply()
            true
        }catch (e: Exception){
            false
        }
    }
}