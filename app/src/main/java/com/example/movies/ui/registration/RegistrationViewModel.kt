package com.example.movies.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.account.Account
import com.example.movies.account.AccountManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val accountManager: AccountManager) :
    ViewModel() {

    private val _registrationState = MutableLiveData<UploadState>()

    val uploadState: LiveData<UploadState> = _registrationState

    fun registerAccount(account: Account) {
        viewModelScope.launch {
            if (accountManager.registerAccount(account)) {
                _registrationState.value = Success
            } else {
                _registrationState.value = UploadFailed()
            }
        }
    }
}

sealed class UploadState
object Success : UploadState()
data class UploadFailed(val errorText: String = "") : UploadState()