package com.example.movies.network

import com.example.movies.account.Account
import com.example.movies.account.UnregisteredAccountException
import com.google.firebase.database.DatabaseReference
import com.example.movies.utils.util.KEY_ACCOUNTS
import com.example.movies.utils.util.getSnapshotValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseRDBServiceAccount @Inject constructor(private val reference: DatabaseReference) :
    AccountRemoteService {

    override suspend fun registerAccount(account: Account): Boolean {
        return try {
            reference.child(KEY_ACCOUNTS)
                .child(account.id)
                .setValue(account)
                .await()
            true
        } catch (e: CancellationException) {
            false
        }
    }

    override suspend fun fetchAccountData(accountId: String): Account? {
        return reference.child(KEY_ACCOUNTS)
            .child(accountId)
            .getSnapshotValue()
            .apply {
                if (!exists()) {
                    throw UnregisteredAccountException()
                }
            }
            .getValue(Account::class.java)
    }

    override suspend fun <T> updateAccountData(
        accountId: String,
        dataMap: Map<String, T>
    ): Boolean {
        var success = false
        reference.child(KEY_ACCOUNTS)
            .child(accountId)
            .updateChildren(dataMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success = true
                }
            }
            .await()
        return success
    }


}