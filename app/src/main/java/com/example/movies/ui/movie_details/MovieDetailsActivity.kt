package com.example.movies.ui.movie_details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.ActivityMovieDetailsBinding
import com.example.movies.model.MovieModel
import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.MoviesListFragment
import com.example.movies.ui.movies_list.Orientation
import com.example.movies.utils.loadImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val EXTRA_MOVIE_DATA = "movie-data"

class MovieDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var binding: ActivityMovieDetailsBinding
    private var genresMap: MutableMap<Long, String>? = null
    private var moviesListFragment: MoviesListFragment? = null

    private var similarMoviesJob: Job? = null

    private lateinit var movie: MovieModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val app = application as App
        app.appComponent.inject(this)
        genresMap = app.genresMap

        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMovieListFragment(savedInstanceState)

        movie = intent.getSerializableExtra(EXTRA_MOVIE_DATA) as MovieModel
        setUpToolbar(movie.name ?: getString(R.string.app_name))
        viewModel = ViewModelProvider(this, factory).get(MovieDetailsViewModel::class.java)
        showMovieData(movie)
    }

    override fun onStart() {
        super.onStart()
        getSimilarMovies()
    }

    private fun getSimilarMovies() {
        similarMoviesJob?.cancel()
        similarMoviesJob = lifecycleScope.launch {
            viewModel.getSimilarMovies(movie.id.toString()).collect { movies ->
                moviesListFragment?.submitData(movies)
            }
        }
    }

    private fun initMovieListFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            moviesListFragment =
                MoviesListFragment.newInstance(LayoutManager.LINEAR, Orientation.HORIZONTAL)
            supportFragmentManager.beginTransaction()
                .add(R.id.similarMoviesListContainer, moviesListFragment!!).commit()
        } else {
            moviesListFragment =
                supportFragmentManager.findFragmentById(R.id.similarMoviesListContainer) as MoviesListFragment?
        }
    }

    private fun setUpToolbar(title: String) {
        setSupportActionBar(binding.detailsToolbar)
        binding.detailsCollapsingLayout.title = title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val textColor = getColor(R.color.def_text_color)
            binding.detailsToolbar.setTitleTextColor(textColor)
            binding.detailsCollapsingLayout.setExpandedTitleColor(textColor)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun showMovieData(movieModel: MovieModel) {
        binding.detailsBackground.loadImage(movieModel.backgroundUrl)
        binding.detailsMovieImage.loadImage(movieModel.posterUrl)
        binding.detailsOverviewTV.text = movieModel.overview
        binding.detailsDateTV.text = movieModel.firstAirDate
        binding.detailsRatingTV.text = "${movieModel.voteAverage}/${movieModel.voteCount}"

        movieModel.genreIds?.run {
            for (genre in this) {
                binding.detailsGenresTV.append("${genresMap!![genre]}, ")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

}