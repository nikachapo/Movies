package com.example.movies.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.App
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.utils.moveToActivity
import com.firebase.ui.auth.AuthUI
import com.example.movies.ui.registration.RegistrationActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private lateinit var providers: List<AuthUI.IdpConfig>
    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this, factory)
            .get(LoginViewModel::class.java)

        initProviders()

        addObservers()

    }

    private fun addObservers() {
        loginViewModel.loginState.observe(this, Observer {
            when (it) {
                is LoggedOut -> {
                    showSignInOption()
                }
                is LoginSuccess -> {
                    startMainActivity()
                }
                is Unregistered -> {
                    Toast.makeText(this, "Unregistered", Toast.LENGTH_SHORT).show()
                    moveToActivity(RegistrationActivity::class.java)
                }
                is LoginError -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initProviders() {
        providers = listOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
    }

    private fun showSignInOption() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false) //set true to save credentials
                .setTheme(R.style.AuthUiTheme)
                .build(),
            LOGIN_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOGIN_REQUEST) {
            loginViewModel.login()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val LOGIN_REQUEST = 22
    }
}