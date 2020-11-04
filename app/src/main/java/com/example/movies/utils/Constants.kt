package com.example.movies.utils

import com.example.movies.api.GenresResponseModel
import com.google.gson.Gson

private const val DEFAULT_GENRES_JSON_STRING =
    "{\"genres\":[{\"id\":10759,\"name\":\"Action & Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":10762,\"name\":\"Kids\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10763,\"name\":\"News\"},{\"id\":10764,\"name\":\"Reality\"},{\"id\":10765,\"name\":\"Sci-Fi & Fantasy\"},{\"id\":10766,\"name\":\"Soap\"},{\"id\":10767,\"name\":\"Talk\"},{\"id\":10768,\"name\":\"War & Politics\"},{\"id\":37,\"name\":\"Western\"}]}"

fun getDefaultGenresResponse(): GenresResponseModel? {
    return Gson().fromJson<GenresResponseModel>(
        DEFAULT_GENRES_JSON_STRING,
        GenresResponseModel::class.java
    )
}