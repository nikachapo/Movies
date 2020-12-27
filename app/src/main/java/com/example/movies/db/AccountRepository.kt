package com.example.movies.db

import androidx.lifecycle.LiveData
import com.example.movies.account.Account

interface AccountRepository {
    suspend fun syncAccountData(id: String?): Boolean
    fun getAccount(id: String): LiveData<Account>
    suspend fun register(account: Account): Boolean
}