package com.example.movies.storage

interface Storage<KEY,VALUE> {

    suspend fun getObjectAtLocation(key: KEY): VALUE?
    suspend fun setObjectAtLocation(key: KEY, value: VALUE): Boolean

}