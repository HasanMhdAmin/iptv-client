package de.itshasan.iptv_core.model.series.info


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.series.info.episode.episodInfo.EpisodInfo
import de.itshasan.iptv_core.storage.LocalStorage

data class Episode(
    @SerializedName("id")
    val id: String,
    @SerializedName("episode_num")
    val episodeNum: Int,
    @SerializedName("title")
    val name: String,
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
) : Posterable {
    override fun getId() = id.toInt()
    override fun getTitle() = name
    override fun getPosterUrl() = info.movieImage ?: ""
    override fun getImdbRating() = info.rating ?: ""
    override fun getStreamUrl() =
        "${LocalStorage.getServerUrl()}/series/${LocalStorage.getUsername()}/${LocalStorage.getPassword()}/$id.$containerExtension"
}