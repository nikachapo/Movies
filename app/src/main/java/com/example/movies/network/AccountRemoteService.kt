package com.example.movies.network

import com.example.movies.account.Account

interface AccountRemoteService {

    suspend fun registerAccount(account: Account): Boolean
    suspend fun fetchAccountData(accountId: String): Account?
    suspend fun <T> updateAccountData(accountId: String, dataMap: Map<String, T>): Boolean

}