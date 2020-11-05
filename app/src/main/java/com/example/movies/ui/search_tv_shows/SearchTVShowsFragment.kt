package com.example.movies.ui.search_tv_shows

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.movies_list.MovieListPresenterBaseFragment
import com.example.movies.ui.movies_list.LayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchTVShowsFragment : MovieListPresenterBaseFragment(LayoutManager.LINEAR) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: SearchTVShowsViewModel
    private var searchJob: Job? = null
    private var lastSearchQuery = ""

    private lateinit var searchEditText: EditText

    override val containerId: Int
        get() = R.id.searchTVShowsListContainer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_tv_shows_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(SearchTVShowsViewModel::class.java)
        savedInstanceState?.getString(LAST_SEARCH_QUERY)?.also {
            lastSearchQuery = it
        }
    }

    override fun onStart() {
        super.onStart()
        search(lastSearchQuery)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearch(lastSearchQuery)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LAST_SEARCH_QUERY, searchEditText.text.toString().trim())
        super.onSaveInstanceState(outState)
    }

    private fun initSearch(query: String) {
        searchEditText = (activity as MainActivity).findViewById(R.id.search_movie_et)
        searchEditText.setText(query)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                search(searchEditText.text.toString())
                true
            } else {
                false
            }
        }
        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search(searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun search(query: String) {
        if (query.isEmpty()) return
        lastSearchQuery = query
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            viewModel.searchMovie(query).collect {
                movieListFragment?.submitData(it)
            }
        }
    }

    companion object {
        fun newInstance() = SearchTVShowsFragment()
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}