package com.example.movies.db

import com.example.movies.account.Account
import io.reactivex.Completable
import io.reactivex.Flowable

interface AccountRepository {
    suspend fun syncAccountData(id: String): Boolean
    fun getAccount(id: String): Flowable<Account>
    suspend fun register(account: Account): Boolean
    fun deleteAccount(account: Account): Completable
}