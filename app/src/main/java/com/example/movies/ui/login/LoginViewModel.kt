package com.example.movies.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.account.AccountManager
import com.example.movies.account.UnregisteredAccountException
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val accountManager: AccountManager) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState>
        get() = _loginState

    init {
        viewModelScope.launch {
            _loginState.value = if (accountManager.isAccountLoggedIn()) {
                LoginSuccess
            } else {
                LoggedOut
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            try {
                accountManager.run {
                    logInAccount()
                    _loginState.value = LoginSuccess
                }
            } catch (e: UnregisteredAccountException) {
                _loginState.value = Unregistered
            } catch (e: Exception) {
                _loginState.value = LoginError(e.message.toString())
            }
        }
    }

}