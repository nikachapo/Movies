package com.example.movies.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.model.MovieModel
import com.example.movies.paging.MovieLoadStateAdapter
import com.example.movies.paging.MoviesAdapter
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class MoviesListFragment : Fragment(), MovieListTemplate {

    private lateinit var listLayoutManager: RecyclerView.LayoutManager
    override lateinit var mView: View
    override val binding: FragmentMoviesListBinding by lazy { FragmentMoviesListBinding.bind(mView) }
    private val adapter = MoviesAdapter().also {
        it.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
        lifecycle.addObserver(this)
    }

    override fun handleArguments() {
        arguments?.let {
            val orientation = it.getSerializable(ARG_ORIENTATION) as Orientation
            val layoutManager = it.getSerializable(ARG_LAYOUT_MANAGER) as LayoutManager
            changeLayoutManager(layoutManager, orientation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_movies_list, container, false)
        return mView
    }

    override suspend fun submitData(data: PagingData<MovieModel>) {
        adapter.submitData(data)
    }

    override fun changeLayoutManager(currentManager: LayoutManager, orientation: Orientation) {
        val mOrientation = when (orientation) {
            Orientation.VERTICAL -> LinearLayoutManager.VERTICAL
            Orientation.HORIZONTAL -> LinearLayoutManager.HORIZONTAL
        }
        listLayoutManager = when (currentManager) {
            LayoutManager.LINEAR -> LinearLayoutManager(context, mOrientation, false)
            LayoutManager.GRID -> GridLayoutManager(context, 2)
        }
        if (::mView.isInitialized) binding.list.layoutManager = listLayoutManager
    }

    override fun setUpViews() {
        binding.list.layoutManager = listLayoutManager
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieLoadStateAdapter { adapter.retry() },
            footer = MovieLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                if (adapter.itemCount == 0) {
                    binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                    binding.retryButton.isVisible = loadState.refresh is LoadState.Error
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.retryButton.visibility = View.GONE
                }
            } else {
                binding.list.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.GONE

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
                        context,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {

        private const val ARG_ORIENTATION = "orientation"
        private const val ARG_LAYOUT_MANAGER = "manager"

        @JvmStatic
        fun newInstance(
            layoutManager: LayoutManager,
            orientation: Orientation = Orientation.VERTICAL
        ) = MoviesListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ORIENTATION, orientation)
                putSerializable(ARG_LAYOUT_MANAGER, layoutManager)
            }
        }
    }
}

enum class Orientation : Serializable {
    VERTICAL, HORIZONTAL
}

enum class LayoutManager : Serializable {
    LINEAR, GRID
}