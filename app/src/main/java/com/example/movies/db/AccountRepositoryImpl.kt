package com.example.movies.db

import com.example.movies.account.Account
import javax.inject.Inject
import javax.inject.Singleton
import com.example.movies.network.AccountRemoteService as Remote

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun syncAccountData(id: String?): Boolean {
        if (id == null) return false
        remote.fetchAccountData(id)?.run {
            accountDao.insertAccount(this)
            return true
        }
        return false
    }

    override fun getAccount(id: String) = accountDao.getAccount(id)

    override suspend fun register(account: Account): Boolean {
        accountDao.insertAccount(account)
        return remote.registerAccount(account)
    }

}