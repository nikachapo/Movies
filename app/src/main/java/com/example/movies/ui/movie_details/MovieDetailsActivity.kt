package com.example.movies.ui.movie_details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.ActivityMovieDetailsBinding
import com.example.movies.model.MovieModel
import com.example.movies.paging.load_state.LoadStateAdapter
import com.example.movies.paging.reviews.ReviewsAdapter
import com.example.movies.ui.movies_list.LayoutManager
import com.example.movies.ui.movies_list.MoviesListFragment
import com.example.movies.ui.movies_list.Orientation
import com.example.movies.utils.loadImage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

const val EXTRA_MOVIE_DATA = "movie-data"

class MovieDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var genresMap: MutableMap<Long, String>

    private lateinit var viewModel: MovieDetailsViewModel

    private lateinit var binding: ActivityMovieDetailsBinding
    private val adapter by lazy {
        ReviewsAdapter().apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
    private var moviesListFragment: MoviesListFragment? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var similarMoviesJob: Job? = null
    private var reviewsJob: Job? = null

    private lateinit var movie: MovieModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val app = application as App
        app.appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory).get(MovieDetailsViewModel::class.java)
        movie = intent.getSerializableExtra(EXTRA_MOVIE_DATA) as MovieModel
        setUpToolbar(movie.name ?: getString(R.string.app_name))
        showMovieData(movie)
        initMovieListFragment(savedInstanceState)
        initBottomSheet()
        initReviewList()
        getReviews()
    }

    private fun initReviewList() {
        binding.reviewsLayout.reviewsList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { adapter.retry() },
            footer = LoadStateAdapter { adapter.retry() }
        )
    }

    private fun initBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.reviewsLayout.bottomSheerRootView)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.reviewsLayout.arrowToggle.isChecked =
                    newState == BottomSheetBehavior.STATE_EXPANDED
            }

        })
        binding.reviewsLayout.arrowToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getSimilarMovies()
    }

    private fun getReviews() {
        reviewsJob?.cancel()
        reviewsJob = lifecycleScope.launch {
            viewModel.getReviews(movie.id.toString()).collect {
                adapter.submitData(it)
            }
        }
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
                binding.detailsGenresTV.append("${genresMap[genre]}, ")
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