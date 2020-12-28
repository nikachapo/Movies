package com.example.movies.account

import android.content.Context
import com.example.movies.db.AccountRepositoryImpl
import com.example.movies.di.modules.SharedPref
import com.example.movies.storage.Storage
import com.example.movies.utils.util.KEY_ACCOUNT_ID
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    private val context: Context,
    private val accountRepository: AccountRepositoryImpl,
    @SharedPref private val localStorage: Storage<String, String>,
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI
) {

    var currentAccount: Account? = null
        private set

    private val isLoggedInWithFirebase: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun isAccountLoggedIn(): Boolean {
        if (isLoggedInWithFirebase) {
            coroutineScope {
                localStorage.getObjectAtLocation(KEY_ACCOUNT_ID)?.run {
                    if (isNotEmpty()) {
                        accountRepository.getAccount(this).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { currentAccount = it }
                    }
                    true
                }
            }
        }
        return false
    }

    private suspend fun isAccountRegistered(): Boolean {
        return accountRepository.syncAccountData(firebaseAuth.currentUser!!.uid)
    }

    suspend fun registerAccount(account: Account) = withContext(IO) {
        if (isLoggedInWithFirebase && accountRepository.register(account)) {
            currentAccount = account
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, firebaseAuth.currentUser!!.uid)
            return@withContext true
        }
        false
    }

    suspend fun logInAccount() = withContext(IO) {
        if (isAccountRegistered()) {
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, firebaseAuth.currentUser!!.uid)
        }
    }

    suspend fun logOut(): Boolean = withContext(IO) {
        try {
            authUI.signOut(context)
                .addOnSuccessListener {
                    currentAccount?.let { account ->
                        accountRepository.deleteAccount(account)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                currentAccount = null
                            }
                    }
                }.await()
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, "")
            true
        } catch (e: Exception) {
            false
        }

    }
}