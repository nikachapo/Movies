package com.example.movies.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesListBinding
import com.example.movies.model.MovieModel
import com.example.movies.paging.load_state.LoadStateAdapter
import com.example.movies.paging.movies.MoviesAdapter
import com.example.movies.ui.movie_details.EXTRA_MOVIE_DATA
import com.example.movies.ui.movie_details.MovieDetailsActivity
import com.example.movies.utils.moveToActivity
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class MoviesListFragment : Fragment(), MoviesListTemplate {

    private lateinit var listLayoutManager: RecyclerView.LayoutManager
    private lateinit var currentLayoutManager: LayoutManager
    private lateinit var currentOrientation: Orientation

    override lateinit var mView: View
    override val binding: FragmentMoviesListBinding by lazy { FragmentMoviesListBinding.bind(mView) }
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_movies_list, container, false)
        setUpViews()
        initAdapter()
        return mView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ARG_LAYOUT_MANAGER, currentLayoutManager)
        outState.putSerializable(ARG_ORIENTATION, currentOrientation)
    }

    override fun handleArguments(arguments: Bundle?) {
        arguments?.let {
            val orientation = it.getSerializable(ARG_ORIENTATION) as Orientation
            val layoutManager = it.getSerializable(ARG_LAYOUT_MANAGER) as LayoutManager
            changeLayoutManager(layoutManager, orientation)
        }
    }

    override suspend fun submitData(data: PagingData<MovieModel>) {
        adapter.submitData(data)
    }

    override fun scrollToTop() {
        binding.list.scrollToPosition(0)
    }

    override fun changeLayoutManager(currentManager: LayoutManager, orientation: Orientation) {
        currentOrientation = orientation
        val mOrientation = when (currentOrientation) {
            Orientation.VERTICAL -> LinearLayoutManager.VERTICAL
            Orientation.HORIZONTAL -> LinearLayoutManager.HORIZONTAL
        }
        currentLayoutManager = currentManager
        listLayoutManager = when (currentManager) {
            LayoutManager.LINEAR -> LinearLayoutManager(context, mOrientation, false)
            LayoutManager.GRID -> GridLayoutManager(context, 2)
        }
        if (::mView.isInitialized) {
            binding.list.layoutManager = listLayoutManager
        }
    }

    override fun setUpViews() {
        binding.list.layoutManager = listLayoutManager
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    override fun initAdapter() {
        adapter = MoviesAdapter {
            activity?.moveToActivity(MovieDetailsActivity::class.java) {
                putSerializable(EXTRA_MOVIE_DATA, it)
            }
        }.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { adapter.retry() },
            footer = LoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            handleLoadState(loadState)
        }
    }

    private fun handleLoadState(loadState: CombinedLoadStates) {
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