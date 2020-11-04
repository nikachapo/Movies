package com.example.movies.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import com.squareup.picasso.Picasso

fun ImageView.loadImage(uri: Uri?) {
    Picasso.get()
        .load(uri)
        .into(this)
}

fun ImageView.loadImage(url: String) {
    loadImage(Uri.parse(url))
}

fun <T : Activity> T.moveToActivity(c: Class<*>) {
    val intent = Intent(this, c)
    startActivity(intent)
}

inline fun <T : Activity> T.moveToActivity(c: Class<*>, block: Bundle.() -> Unit) {
    val intent = Intent(this, c)
    intent.putExtras(Bundle().apply(block))
    startActivity(intent)
}

fun <T : Activity> T.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun <T : Activity> T.showToast(@StringRes stringId: Int) {
    showToast(getString(stringId))
}
