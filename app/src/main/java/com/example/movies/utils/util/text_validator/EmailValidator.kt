package com.example.movies.utils.util.text_validator

import android.util.Patterns

class EmailValidator: TextValidator(maxSymbols = 30) {
    override fun isTextValid(text: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }
}