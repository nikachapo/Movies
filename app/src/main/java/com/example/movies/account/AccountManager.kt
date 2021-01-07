package com.example.movies.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.movies.db.AccountRepository
import com.example.movies.di.modules.IO
import com.example.movies.di.modules.SharedPref
import com.example.movies.storage.Storage
import com.example.movies.utils.util.KEY_ACCOUNT_ID
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    private val accountRepository: AccountRepository,
    @SharedPref private val localStorage: Storage<String, String>,
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI,
    @IO private val ioDispatcher: CoroutineDispatcher
) {

    private val _currentAccount = MediatorLiveData<Account>()

    val currentAccount: LiveData<Account> = _currentAccount

    private val currentFirebaseUID: String?
        get() = firebaseAuth.uid

    private val isLoggedInWithFirebase: Boolean
        get() = currentFirebaseUID != null

    suspend fun logInAccount() = withContext(ioDispatcher) {
        if (isAccountRegistered()) {
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, currentFirebaseUID)
        }
    }

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
        return accountRepository.syncAccountData(currentFirebaseUID)
    }

    suspend fun registerAccount(account: Account) = withContext(ioDispatcher) {
        if (isLoggedInWithFirebase && accountRepository.register(account)) {
            _currentAccount.postValue(account)
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, currentFirebaseUID)
            return@withContext true
        }
        false
    }

    suspend fun logOut(context: Context): Boolean = withContext(ioDispatcher) {
        try {
            localStorage.setObjectAtLocation(KEY_ACCOUNT_ID, "")
            authUI.signOut(context)
                .addOnSuccessListener {
                    _currentAccount.postValue(null)
                }.await()
            true
        } catch (e: Exception) {
            false
        }

    }
}