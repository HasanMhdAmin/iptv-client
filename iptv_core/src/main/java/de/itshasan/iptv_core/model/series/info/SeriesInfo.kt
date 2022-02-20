package de.itshasan.iptv_core.model.series.info


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season

data class SeriesInfo(
//    @SerializedName("seasons")
//    val seasons: List<Season>,
    @SerializedName("info")
    val info: Info,
//    @SerializedName("episodes")
//    val episodes: Episodes
)