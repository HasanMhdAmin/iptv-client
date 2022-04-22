package de.itshasan.iptv_core.model.movie


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.ContentInfo
import de.itshasan.iptv_core.model.movie.movieInfo.Info

data class MovieInfo(
    @SerializedName("info")
    val info: Info,
    @SerializedName("movie_data")
    val movie: Movie
) : ContentInfo