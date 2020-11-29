package com.example.movies.utils.util

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun <T : AppCompatActivity> T.moveToActivity(c: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, c)
    bundle?.run {
        intent.putExtras(this)
    }
    startActivity(intent)
}

fun <T : AppCompatActivity> T.showToast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun <T : AppCompatActivity> T.showToast(@StringRes stringId :Int){
    showToast(getString(stringId))
}

fun AppCompatActivity.openGallery() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
}

fun Fragment.openGallery() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
}