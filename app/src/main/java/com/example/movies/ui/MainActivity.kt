package com.example.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.di.AppComponent
import dagger.Component
import kotlinx.coroutines.Job
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var lastSearchQuery = ""
    private var searchJob: Job? = null
    lateinit var appComponent: AppComponent

    private var popularTVShowsFragment: PopularTVShowsFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as App).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, PopularTVShowsFragment.newInstance())
                .commit()
        } else {
            popularTVShowsFragment =
                supportFragmentManager.findFragmentById(R.id.mainContainer) as PopularTVShowsFragment?
        }


//        initSearch(lastSearchQuery)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, lastSearchQuery.trim())
    }

//    private fun initSearch(query: String) {
//        binding.searchRepo.setText(query)
//        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_GO) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
//        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                updateRepoListFromInput()
//                true
//            } else {
//                false
//            }
//        }
//    }

//    private fun search(query: String) {
//        lastSearchQuery = query
//        searchJob?.cancel()
//        searchJob = lifecycleScope.launch {
//            viewModel.searchMovie(query).collect {
//                adapter.submitData(it)
//            }
//        }
//    }
//
//    private fun updateRepoListFromInput() {
//        binding.searchRepo.text.trim().let {
//            if (it.isNotEmpty()) {
//                binding.list.scrollToPosition(0)
//                search(it.toString())
//            }
//        }
//    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}
