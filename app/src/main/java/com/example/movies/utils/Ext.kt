package com.example.movies.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .into(this)
}

fun ImageView.loadImage(url: String) {
    loadImage(Uri.parse(url))
}