package com.example.movies.ui.login

sealed class LoginState
object LoggedOut : LoginState()
object LoginSuccess : LoginState()
data class LoginError(val errorMessage: String) : LoginState()
object Unregistered : LoginState()
