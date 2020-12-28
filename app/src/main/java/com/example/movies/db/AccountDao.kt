package com.example.movies.db

import androidx.room.*
import com.example.movies.account.Account
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: Account): Completable

    @Query("select * from Account where id = :id")
    fun getAccount(id: String) : Flowable<Account>

    @Delete
    fun deleteAccount(account: Account): Completable
}