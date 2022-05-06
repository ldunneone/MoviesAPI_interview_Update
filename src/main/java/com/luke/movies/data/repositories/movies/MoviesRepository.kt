package com.luke.movies.data.repositories.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.luke.movies.base.MOVIES_PER_PAGE
import com.luke.movies.data.api.ApiServices
import com.luke.movies.data.models.movies.Movie
import com.luke.movies.viewModels.MovieState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface MoviesRepository{
    val responseState: LiveData<MovieState>
    fun fetchLiveMoviePagedList(): LiveData<PagingData<Movie>>
    fun getMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int)
}

class MoviesRepositoryImpl(private val apiService: ApiServices):MoviesRepository {

    private var _responseState: MutableLiveData<MovieState> = MutableLiveData(MovieState.LOADING())
    override val responseState: LiveData<MovieState> = _responseState

    override fun fetchLiveMoviePagedList(): LiveData<PagingData<Movie>> {
        val dataSource= MovieDataSource(apiService)
        _responseState=dataSource._responseState
        return Pager(
            config = PagingConfig(pageSize = MOVIES_PER_PAGE, enablePlaceholders = true),
            pagingSourceFactory = {dataSource}
        ).liveData
    }

    override fun getMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) {
        _responseState.postValue(MovieState.LOADING())
        try {
            compositeDisposable.add(
                apiService.getMovieDetailsOld(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _responseState.postValue(MovieState.SUCCESS(it))
                        }, {
                            _responseState.postValue(MovieState.ERROR(it))
                        })
            )
        } catch (e: Exception) {
            _responseState.postValue(MovieState.ERROR(e))
        }
    }
}