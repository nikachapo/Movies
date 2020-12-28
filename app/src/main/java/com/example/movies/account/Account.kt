package com.example.movies.account

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Account(
    @PrimaryKey val id: String,
    val userName: String,
    val photoUrl: String,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String
):Parcelable{
    constructor():this("","","","","","","")
}