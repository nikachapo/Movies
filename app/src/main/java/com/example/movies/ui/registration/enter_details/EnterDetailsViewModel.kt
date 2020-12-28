package com.example.movies.ui.registration.enter_details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.App
import com.example.movies.R
import com.example.movies.utils.util.text_validator.*
import javax.inject.Inject

class EnterDetailsViewModel @Inject constructor(context: Context) :
    AndroidViewModel(context as Application) {

    private val validatorFactory = ValidatorFactory()

    private val firstNameField = Field(validatorFactory.create(CODE_VALIDATOR_FIRST_NAME))
    private val lastNameField = Field(validatorFactory.create(CODE_VALIDATOR_LAST_NAME))
    private val userNameField = Field(validatorFactory.create(CODE_VALIDATOR_USERNAME))
    private val emailField = Field(validatorFactory.create(CODE_VALIDATOR_EMAIL))

    val firstNameError: LiveData<FieldInputState> = firstNameField.errorText
    val lastNameError: LiveData<FieldInputState> = lastNameField.errorText
    val userNameError: LiveData<FieldInputState> = userNameField.errorText
    val emailError: LiveData<FieldInputState> = emailField.errorText

    fun inputField(text: String, validatorCode: String) {
        when (validatorCode) {
            CODE_VALIDATOR_FIRST_NAME -> firstNameField.value.value = text
            CODE_VALIDATOR_LAST_NAME -> lastNameField.value.value = text
            CODE_VALIDATOR_USERNAME -> userNameField.value.value = text
            CODE_VALIDATOR_EMAIL -> emailField.value.value = text
            else -> throw ValidatorTypeNotFound()
        }
    }

    inner class Field(
        private val validator: TextValidator,
        val value: MutableLiveData<String> = MutableLiveData()
    ) {
        val errorText = MediatorLiveData<FieldInputState>()

        init {
            errorText.addSource(value) { input ->
                if (!validator.isLengthValid(input)) {
                    errorText.value = FieldError(
                        (getApplication<App>())
                            .getString(R.string.enter_valid_size_text) +
                                "${validator.minSymbols}-${validator.maxSymbols}"
                    )
                    return@addSource
                }

                if (!validator.isTextValid(input)) {
                    errorText.value = FieldError(
                        (getApplication<App>())
                            .getString(R.string.not_valid)
                    )
                    return@addSource
                }

                errorText.value = FieldSuccess
            }
        }
    }
}

sealed class FieldInputState
object FieldSuccess : FieldInputState()
data class FieldError(val errorText: String) : FieldInputState()