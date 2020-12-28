package com.example.movies.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movies.App
import com.example.movies.R
import com.example.movies.account.AccountManager
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.di.AppComponent
import com.example.movies.ui.popular_tv_shows.PopularTVShowsFragment
import com.example.movies.ui.search_tv_shows.SearchTVShowsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var accountManager: AccountManager
    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppComponent
    private var searching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as App).appComponent
        appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this,accountManager.currentAccount.toString(), Toast.LENGTH_SHORT).show()
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
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, SearchTVShowsFragment.newInstance())
                .addToBackStack(null)
                .commit()
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
