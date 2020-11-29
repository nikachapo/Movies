package com.example.movies.utils.util.text_validator

class UserNameValidator: TextValidator(maxSymbols = 20) {

    override fun isTextValid(text: CharSequence): Boolean {
        text.forEach {c ->
            if (!c.isLetterOrDigit()){
                return false
            }
        }
        return true
    }

}