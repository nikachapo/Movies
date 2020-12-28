package com.example.movies.db

import com.example.movies.account.Account
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton
import com.example.movies.network.AccountRemoteService as Remote

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun syncAccountData(id: String): Boolean {
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

    override fun deleteAccount(account: Account): Completable {
        return accountDao.deleteAccount(account)
    }

}