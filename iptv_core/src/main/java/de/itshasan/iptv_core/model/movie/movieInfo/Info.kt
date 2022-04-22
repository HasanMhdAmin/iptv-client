package de.itshasan.iptv_core.model.movie.movieInfo


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("audio")
    val audio: Audio,
    @SerializedName("backdrop")
    val backdrop: String,
    @SerializedName("backdrop_path")
    val backdropPath: List<String>,
    @SerializedName("bitrate")
    val bitrate: Int,
    @SerializedName("cast")
    val cast: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("duration_secs")
    val durationSecs: Int,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("movie_image")
    val movieImage: String,
    @SerializedName("plot")
    val plot: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("releasedate")
    val releasedate: String,
    @SerializedName("tmdb_id")
    val tmdbId: String,
    @SerializedName("video")
    val video: Video,
    @SerializedName("youtube_trailer")
    val youtubeTrailer: String
)