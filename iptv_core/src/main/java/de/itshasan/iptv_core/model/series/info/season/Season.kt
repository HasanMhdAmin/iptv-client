package de.itshasan.iptv_core.model.series.info.season


import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("air_date")
    val airDate: Any,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_big")
    val coverBig: String
)