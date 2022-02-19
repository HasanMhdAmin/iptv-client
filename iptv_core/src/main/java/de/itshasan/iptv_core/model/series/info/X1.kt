package de.itshasan.iptv_core.model.series.info


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.series.info.episode.episodInfo.EpisodInfo

data class X1(
    @SerializedName("id")
    val id: String,
    @SerializedName("episode_num")
    val episodeNum: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("container_extension")
    val containerExtension: String,
    @SerializedName("info")
    val info: EpisodInfo,
    @SerializedName("custom_sid")
    val customSid: String,
    @SerializedName("added")
    val added: String,
    @SerializedName("season")
    val season: Int,
    @SerializedName("direct_source")
    val directSource: String
)