package com.example.movies.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.account.AccountManager
import com.example.movies.data.IMovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountProfileViewModel
@Inject constructor(
    private val accountManager: AccountManager,
    private val context: Context
) : ViewModel() {

    val currentAccount = accountManager.currentAccount

    private val _logOut = MutableLiveData<Boolean>()
    private val logOut: LiveData<Boolean> = _logOut

    fun logOut() {
        viewModelScope.launch {
            _logOut.value = accountManager.logOut(context)
        }
    }

}