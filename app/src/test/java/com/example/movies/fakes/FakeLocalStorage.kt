package com.example.movies.fakes

import com.example.movies.storage.Storage

class FakeLocalStorage : Storage<String, String> {

    private val objects = mutableMapOf<String, String?>()

    override suspend fun getObjectAtLocation(key: String): String? {
        return objects[key]
    }

    override suspend fun setObjectAtLocation(key: String, value: String?): Boolean {
        objects[key] = value
        return true
    }

}