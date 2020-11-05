package com.example.movies.paging.source

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

abstract class BasePagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {

    abstract val startingKey: Key

    abstract suspend fun getData(key: Key): List<Value>

    abstract fun getPreviousKey(currentKey: Key): Key

    abstract fun getNextKey(currentKey: Key): Key

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {

        val key = params.key ?: startingKey

        return try {
            val data = getData(key)
            LoadResult.Page(
                data = data,
                prevKey = if (key == startingKey) null else getPreviousKey(key),
                nextKey = if (data.isEmpty()) null else getNextKey(key)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

}