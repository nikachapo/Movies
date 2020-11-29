package com.example.movies.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.example.movies.db.AccountRepositoryImpl
import com.example.movies.di.modules.SharedPref
import com.example.movies.storage.Storage
import com.example.movies.utils.util.KEY_ACCOUNT_ID
import kotlinx.coroutines.Dispatchers.IO
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

    private val _currentAccount = MediatorLiveData<Account>()

    val currentAccount: LiveData<Account>? = _currentAccount

    private val isLoggedInWithFirebase: Boolean
        get() = firebaseAuth.currentUser != null

    suspend fun isAccountLoggedIn(): Boolean {

        if (isLoggedInWithFirebase) {
            localStorage.getObjectAtLocation(KEY_ACCOUNT_ID)?.run {
                if (isNotEmpty()) {
                    _currentAccount.addSource(accountRepository.getAccount(this)) {
                        _currentAccount.value = it
                    }
                    return true
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
            _currentAccount.postValue(account)
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
                    _currentAccount.postValue(null)
                }.await()
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, "")
            true
        } catch (e: Exception) {
            false
        }

    }
}