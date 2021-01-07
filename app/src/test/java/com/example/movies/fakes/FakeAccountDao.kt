package com.example.movies.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.account.Account
import com.example.movies.db.AccountDao

class FakeAccountDao : AccountDao {

    private val accounts = arrayListOf<Account>()

    override suspend fun insertAccount(account: Account) {
        accounts.add(account)
    }

    override fun getAccount(id: String): LiveData<Account> {
        val exists = accounts.filter { it.id == id }.size == 1
        return if (exists) MutableLiveData()
        else MutableLiveData<Account>().apply { value = accounts.filter { it.id == id }[0] }
    }

}