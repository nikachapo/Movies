package com.example.movies

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.api.MoviesService
import com.example.movies.data.MovieRepository
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.paging.MovieLoadStateAdapter
import com.example.movies.paging.MoviesAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = MoviesAdapter().also {
        it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
    private var lastSearchQuery = ""

    private var searchJob: Job? = null
    private var getPopularMoviesJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retryButton.setOnClickListener { adapter.retry() }

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MovieRepository(MoviesService.create()))
        ).get(MainViewModel::class.java)

        initAdapter()
        savedInstanceState?.getString(LAST_SEARCH_QUERY)?.also {
            if (it.isEmpty()) {
                getPopularMovies()
                return@also
            }
            lastSearchQuery = it
            search(it)
        } ?: getPopularMovies()

        initSearch(lastSearchQuery)

    }

    private fun getPopularMovies() {
        getPopularMoviesJob?.cancel()
        getPopularMoviesJob = lifecycleScope.launch {
            viewModel.getPopularMovies().collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text.trim().toString())
    }

    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { adapter.retry() },
            footer = MovieLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                // We're refreshing: either loading or we had an error
                // So we can hide the list
                binding.list.visibility = View.GONE
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.refresh is LoadState.Error
            } else {
                // We're not actively refreshing
                // So we should show the list
                binding.list.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> {
                        loadState.append as LoadState.Error
                    }
                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }
                    else -> {
                        null
                    }
                }
                errorState?.let {
                    Toast.makeText(
                        this,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initSearch(query: String) {
        binding.searchRepo.setText(query)
        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovie(query).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun updateRepoListFromInput() {
        binding.searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                search(it.toString())
            }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}
