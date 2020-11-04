package com.example.movies.ui.movies_list

import androidx.paging.PagingData

interface PagedListListeners<T : Any> : ListListeners {
    suspend fun submitData(data: PagingData<T>)
}