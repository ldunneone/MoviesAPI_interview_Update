package com.luke.movies.viewModels

sealed class MovieState{
    class LOADING(val isLoading:Boolean=true) : MovieState()
    class SUCCESS(val response: Any): MovieState()
    class ERROR(val error:Throwable):MovieState()
}