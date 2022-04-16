package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class EpisodInfo(
    @SerializedName("movie_image")
    val movieImage: String?,
    @SerializedName("plot")
    val plot: String?,
    @SerializedName("releasedate")
    val releaseDate: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("duration_secs")
    val durationSecs: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("video")
    val video: Video,
    @SerializedName("audio")
    val audio: Audio,
    @SerializedName("bitrate")
    val bitrate: Int
)