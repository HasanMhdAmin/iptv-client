package de.itshasan.iptv_core.model.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.storage.LocalStorage

@Entity
data class Movie(
    val added: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("container_extension")
    val containerExtension: String?,
    @SerializedName("custom_sid")
    val customSid: String?,
    @SerializedName("direct_source")
    val directSource: String?,
    @SerializedName("is_adult")
    val isAdult: String?,
    val name: String,
    val num: Int?,
    val rating: String?,
    val rating_5based: Float?,
    @SerializedName("stream_icon")
    val streamIcon: String?,
    @PrimaryKey
    @SerializedName("stream_id")
    val streamId: Int,
    @SerializedName("stream_type")
    val streamType: String?
) : Posterable {
    override fun getId() = streamId
    override fun getTitle() = name
    override fun getPosterUrl() = streamIcon ?: ""
    override fun getImdbRating() = rating ?: ""
    override fun getStreamUrl() =
        "${LocalStorage.getServerUrl()}/movie/${LocalStorage.getUsername()}/${LocalStorage.getPassword()}/$streamId.$containerExtension"

}