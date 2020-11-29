package com.example.movies.ui.registration.enter_details

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.R
import com.google.firebase.auth.FirebaseAuth
import com.example.movies.account.Account
import com.example.movies.databinding.EnterDetailsFragmentBinding
import com.example.movies.ui.registration.RegistrationActivity
import com.example.movies.utils.util.TextChangeWatcher
import com.example.movies.utils.util.text_validator.CODE_VALIDATOR_EMAIL
import com.example.movies.utils.util.text_validator.CODE_VALIDATOR_FIRST_NAME
import com.example.movies.utils.util.text_validator.CODE_VALIDATOR_LAST_NAME
import com.example.movies.utils.util.text_validator.CODE_VALIDATOR_USERNAME
import javax.inject.Inject

class EnterDetailsFragment : Fragment() {

    private var isFNameValid = false
    private var isLNameValid = false
    private var isUserNameValid = false
    private var isEmailValid = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: EnterDetailsViewModel

    private lateinit var binding: EnterDetailsFragmentBinding

    private lateinit var enterDetailsCallback: EnterDetailsCallback

    private val nextBtn: Button by lazy { requireActivity().findViewById<Button>(R.id.nextBtn) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as RegistrationActivity).registrationComponent.inject(this)
        enterDetailsCallback = activity as EnterDetailsCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EnterDetailsFragmentBinding.bind(
            inflater.inflate(R.layout.enter_details_fragment, container, false)
        )
        addTextWatchers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EnterDetailsViewModel::class.java)
        addObservers()
        nextBtn.setOnClickListener {
            enterDetailsCallback.onFieldAccepted(getAccountFromFields())
        }
    }

    private fun getAccountFromFields(): Account {
        binding.run {
            return Account(
                firebaseAuth.currentUser!!.uid,
                userNameLayout.editText!!.text.toString(),
                "empty",
                emailLayout.editText!!.text.toString(),
                "0000",
                firstNameLayout.editText!!.text.toString(),
                lastNameLayout.editText!!.text.toString()
            )
        }
    }

    private fun addTextWatchers() {
        binding.firstNameLayout.editText!!.addTextChangedListener(object : TextChangeWatcher {
            override fun afterTextChanged(input: Editable?) {
                viewModel.inputField(input.toString(), CODE_VALIDATOR_FIRST_NAME)
            }
        })

        binding.lastNameLayout.editText!!.addTextChangedListener(object : TextChangeWatcher {
            override fun afterTextChanged(input: Editable?) {
                viewModel.inputField(input.toString(), CODE_VALIDATOR_LAST_NAME)
            }
        })

        binding.userNameLayout.editText!!.addTextChangedListener(object : TextChangeWatcher {
            override fun afterTextChanged(input: Editable?) {
                viewModel.inputField(input.toString(), CODE_VALIDATOR_USERNAME)
            }
        })

        binding.emailLayout.editText!!.addTextChangedListener(object : TextChangeWatcher {
            override fun afterTextChanged(input: Editable?) {
                viewModel.inputField(input.toString(), CODE_VALIDATOR_EMAIL)
            }
        })
    }

    private fun addObservers() {
        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            checkFieldState(binding.emailLayout.editText!!, it) { isValid ->
                isEmailValid = isValid
            }
        })

        viewModel.firstNameError.observe(viewLifecycleOwner, Observer {
            checkFieldState(binding.firstNameLayout.editText!!, it) { isValid ->
                isFNameValid = isValid
            }
        })

        viewModel.lastNameError.observe(viewLifecycleOwner, Observer {
            checkFieldState(binding.lastNameLayout.editText!!, it) { isValid ->
                isLNameValid = isValid
            }
        })

        viewModel.userNameError.observe(viewLifecycleOwner, Observer {
            checkFieldState(binding.userNameLayout.editText!!, it) { isValid ->
                isUserNameValid = isValid
            }
        })
    }

    private fun checkFieldState(
        editText: EditText,
        inputState: FieldInputState?,
        isValid: (Boolean) -> Unit
    ) {

        when (inputState) {
            is FieldError -> {
                editText.error = inputState.errorText
                nextBtn.isEnabled = false
                isValid(false)
            }
            is FieldSuccess -> {
                isValid(true)
                nextBtn.isEnabled = everyFieldIsValid()
            }
        }
    }

    private fun everyFieldIsValid(): Boolean {
        return isFNameValid && isLNameValid && isUserNameValid && isEmailValid
    }

    companion object {
        fun newInstance() = EnterDetailsFragment()
    }

    interface EnterDetailsCallback {
        fun onFieldAccepted(account: Account)
    }

}