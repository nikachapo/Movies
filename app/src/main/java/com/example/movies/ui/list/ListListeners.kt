package com.example.movies.ui.list

import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.Orientation

interface ListListeners {

    fun scrollToTop()

    fun initAdapter()

    fun changeLayoutManager(
        currentManager: LayoutManager,
        orientation: Orientation = Orientation.VERTICAL
    )

}