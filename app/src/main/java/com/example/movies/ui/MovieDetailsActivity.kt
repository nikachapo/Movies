package com.example.movies.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.ActivityMovieDetailsBinding
import com.example.movies.model.MovieModel
import com.example.movies.utils.loadImage

const val EXTRA_MOVIE_DATA = "movie-data"

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private var genresMap: MutableMap<Long, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genresMap = (application as App).genresMap

        val movie = intent.getSerializableExtra(EXTRA_MOVIE_DATA) as MovieModel
        setUpToolbar(movie.name ?: getString(R.string.app_name))

        showMovieData(movie)
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