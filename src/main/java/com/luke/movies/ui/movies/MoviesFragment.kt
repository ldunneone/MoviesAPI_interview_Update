package com.luke.movies.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.luke.movies.R
import com.luke.movies.databinding.FragmentMoviesBinding
import com.luke.movies.helpers.extensions.visible
import com.luke.movies.helpers.network.NetworkUtil
import com.luke.movies.ui.movieDetails.MovieDetailFragment.Companion.REQUEST_MOVIE_ID
import com.luke.movies.ui.movieDetails.MovieDetailsActivity
import com.luke.movies.viewModels.MovieState
import com.luke.movies.viewModels.MoviesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 */
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModel()
    private var movieAdapter = MoviesPagedAdapter()

    private val binding by lazy{
        FragmentMoviesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeData()
        return binding.root
    }

    /**
     *
     */
    private fun observeData() {
        setUpRecyclerView()
        viewModel.moviePagedList.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                movieAdapter.submitData(it)
            }
        })
        viewModel.responseState.observe(viewLifecycleOwner, {movieState->
            when(movieState){
                is MovieState.LOADING -> {
                    binding.progressCircular?.visibility = if (movieState.isLoading) View.VISIBLE else View.GONE
                }
                is MovieState.ERROR->{
                    binding.error.visible()
                    binding.error.text = movieState.error.localizedMessage
                }
            }
        })

        if (!NetworkUtil.isInternetAvailable)
            Toast.makeText(requireContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT)
                .show()
    }

    private fun setUpRecyclerView() {
        binding.moviesRecyclerView?.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
        movieAdapter.onMovieClicked = {
            it?.let { movie ->
                openMovieDetails(movie.id)
            }
        }
    }

    private fun openMovieDetails(movieId: Int) {
        activity?.let {
            it.startActivity(
                Intent(it, MovieDetailsActivity::class.java)
                    .putExtra(REQUEST_MOVIE_ID, movieId)
            )
        }
    }
}