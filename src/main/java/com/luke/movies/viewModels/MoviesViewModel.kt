package com.luke.movies.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.luke.movies.data.models.movies.Movie
import com.luke.movies.data.repositories.movies.MoviesRepository
import io.reactivex.disposables.CompositeDisposable

/**
 *
 */
class MoviesViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

    /**
     *
     */
    val responseState:LiveData<MovieState> = movieRepository.responseState
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList: LiveData<PagingData<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList()
    }

    fun getMovieDetails(movieId: Int) {
        movieRepository.getMovieDetails(compositeDisposable, movieId)
    }

}