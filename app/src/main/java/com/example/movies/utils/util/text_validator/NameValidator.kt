package com.example.movies.utils.util.text_validator

sealed class NameValidator : TextValidator(2, 30) {

    override fun isTextValid(text: CharSequence): Boolean {
        text.forEach {c ->
            if (!c.isLetter()){
                return false
            }
        }
        return true
    }

}

class FirstNameValidator: NameValidator()
class LastNameValidator: NameValidator()