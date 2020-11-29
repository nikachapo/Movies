package com.example.movies.utils.util.text_validator

abstract class TextValidator(val minSymbols: Int = 1, val maxSymbols: Int) {

    fun isLengthValid(text: CharSequence) = text.length in minSymbols..maxSymbols

    abstract fun isTextValid(text: CharSequence): Boolean

}

const val CODE_VALIDATOR_FIRST_NAME = "v-f"
const val CODE_VALIDATOR_LAST_NAME = "v-l"
const val CODE_VALIDATOR_EMAIL = "v-email"
const val CODE_VALIDATOR_USERNAME = "v-un"