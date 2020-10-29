package com.example.movies.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListToString(list: List<Long>): String {
        return list.joinToString(SEPARATOR)
    }

    @Suppress("UNCHECKED_CAST")
    @TypeConverter
    fun fromStringToNumericList(string: String): List<Long>? {
        if (string.isEmpty()) return null
        return string.split(SEPARATOR).map { it.toLong() }
    }

    companion object {
        private const val SEPARATOR = ";"
    }

}