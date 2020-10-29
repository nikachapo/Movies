package com.example.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "nextPageKey")
    val nextPageKey: Int? = null
)
