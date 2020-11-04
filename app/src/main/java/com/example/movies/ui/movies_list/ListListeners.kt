package com.example.movies.ui.movies_list

interface ListListeners {

    fun scrollToTop()

    fun initAdapter()

    fun changeLayoutManager(
        currentManager: LayoutManager,
        orientation: Orientation = Orientation.VERTICAL
    )

}