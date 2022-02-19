package de.itshasan.iptv_core.model.series.info.info


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("name")
    val name: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("plot")
    val plot: String,
    @SerializedName("cast")
    val cast: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("last_modified")
    val lastModified: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("rating_5based")
    val rating5based: Int,
    @SerializedName("backdrop_path")
    val backdropPath: List<String>,
    @SerializedName("youtube_trailer")
    val youtubeTrailer: String,
    @SerializedName("episode_run_time")
    val episodeRunTime: String,
    @SerializedName("category_id")
    val categoryId: String
)