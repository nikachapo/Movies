package com.example.movies.utils

import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.loadImage(uri: Uri?) {
    Picasso.get()
        .load(uri)
        .into(this)
}

fun ImageView.loadImage(url: String) {
    loadImage(Uri.parse(url))
}