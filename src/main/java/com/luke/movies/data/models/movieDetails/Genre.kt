package com.luke.movies.data.models.movieDetails

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name")
    val name: String
)