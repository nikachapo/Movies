package com.example.movies.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.di.AppComponent
import com.example.movies.ui.popular_tv_shows.PopularTVShowsFragment
import com.example.movies.ui.profile.AccountProfileFragment
import com.example.movies.ui.search_tv_shows.SearchTVShowsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppComponent
    private var searching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as App).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarLayout.searchToolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, PopularTVShowsFragment.newInstance())
                .commit()
        } else {
            searching = savedInstanceState.getBoolean(EXTRA_IS_SEARCHING)
            if (searching) {
                binding.toolbarLayout.searchMovieEt.visibility = View.VISIBLE
                binding.toolbarLayout.searchBtn.visibility = View.GONE
            }
        }

        binding.toolbarLayout.searchBtn.setOnClickListener {
            searching = true
            binding.toolbarLayout.searchMovieEt.visibility = View.VISIBLE
            binding.toolbarLayout.searchBtn.visibility = View.GONE
            binding.toolbarLayout.accountBtn.visibility = View.GONE

            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, SearchTVShowsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.toolbarLayout.accountBtn.setOnClickListener {
            searching = false
            binding.toolbarLayout.searchBtn.visibility = View.GONE
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                add(R.id.mainContainer, AccountProfileFragment())
                addToBackStack(null)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_IS_SEARCHING, searching)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            binding.toolbarLayout.searchMovieEt.visibility = View.GONE
            binding.toolbarLayout.searchBtn.visibility = View.VISIBLE
            binding.toolbarLayout.accountBtn.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
            searching = false
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_IS_SEARCHING = "searching"
    }
}
