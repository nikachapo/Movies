package com.example.movies.ui.list

import androidx.paging.PagingData

interface PagedListListeners<T : Any> : ListListeners {
    suspend fun submitData(data: PagingData<T>)
}