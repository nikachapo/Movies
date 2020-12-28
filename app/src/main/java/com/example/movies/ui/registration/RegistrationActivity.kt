package com.example.movies.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.App
import com.example.movies.R
import com.example.movies.account.Account
import com.example.movies.di.RegistrationComponent
import com.example.movies.ui.registration.choose_picture.PictureChooseFragment
import com.example.movies.ui.registration.enter_details.EnterDetailsFragment
import com.example.movies.ui.registration.finish_registration.FinishRegistrationFragment
import com.example.movies.utils.util.showToast
import javax.inject.Inject

class RegistrationActivity : AppCompatActivity(), EnterDetailsFragment.EnterDetailsCallback {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var registrationViewModel: RegistrationViewModel
    lateinit var registrationComponent: RegistrationComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        registrationComponent = (application as App).appComponent
            .registrationComponentFactory().create().also { it.inject(this) }
        registrationComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.registrationFragContainer,
                    EnterDetailsFragment.newInstance(),
                    "enterDetails"
                )
                .commit()
        }

        registrationViewModel = ViewModelProvider(this, factory)
            .get(RegistrationViewModel::class.java)

        registrationViewModel.uploadState.observe(this, Observer { regState ->

            when (regState) {
                is Success -> showToast(R.string.data_stored_success)
                is UploadFailed -> showToast(R.string.registration_failed)
            }

        })

    }

    override fun onFieldAccepted(account: Account) {
        registrationViewModel.registerAccount(account)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.registrationFragContainer,
                PictureChooseFragment.newInstance(),
                "choosePicture"
            )
            .addToBackStack(null)
            .commit()
    }

    fun onRegistrationFinished() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.registrationFragContainer,
                FinishRegistrationFragment.newInstance(),
                "finishReg"
            )
            .addToBackStack(null)
            .commit()
    }
}