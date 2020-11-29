package com.example.movies.utils.util.text_validator

import com.example.movies.di.scope.ActivityScope
import java.lang.Exception

@ActivityScope
class ValidatorFactory {

    @Throws(ValidatorTypeNotFound::class)
    fun create(validatorCode: String): TextValidator {
        return when (validatorCode) {
            CODE_VALIDATOR_EMAIL -> EmailValidator()
            CODE_VALIDATOR_FIRST_NAME -> FirstNameValidator()
            CODE_VALIDATOR_LAST_NAME -> LastNameValidator()
            CODE_VALIDATOR_USERNAME -> UserNameValidator()
            else -> throw ValidatorTypeNotFound()
        }
    }

}

class ValidatorTypeNotFound : Exception("Wrong Validator code")