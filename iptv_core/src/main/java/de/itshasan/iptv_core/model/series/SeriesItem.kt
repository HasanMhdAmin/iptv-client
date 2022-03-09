package de.itshasan.iptv_core.model.series


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SeriesItem(
//    @SerializedName("num")
//    val num: Int,
    @SerializedName("name")
    val name: String,
    @PrimaryKey
    @SerializedName("series_id")
    val seriesId: Int,
    @SerializedName("cover")
    val cover: String,
//    @SerializedName("plot")
//    val plot: String,
//    @SerializedName("cast")
//    val cast: String,
//    @SerializedName("director")
//    val director: String,
    @SerializedName("genre")
    val genre: String,
//    @SerializedName("releaseDate")
//    val releaseDate: String,
//    @SerializedName("last_modified")
//    val lastModified: String,
    @SerializedName("rating")
    val rating: String,
//    @SerializedName("rating_5based")
//    val rating5based: Int,
//    @SerializedName("youtube_trailer")
//    val youtubeTrailer: String,
//    @SerializedName("episode_run_time")
//    val episodeRunTime: String,
    @SerializedName("category_id")
    @ColumnInfo(defaultValue = "-1")
    val categoryId: String
)