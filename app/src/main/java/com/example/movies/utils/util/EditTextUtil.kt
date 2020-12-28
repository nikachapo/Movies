package com.example.movies.utils.util

import android.text.TextWatcher

interface TextChangeWatcher: TextWatcher{
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //ignore
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //ignore
    }

}