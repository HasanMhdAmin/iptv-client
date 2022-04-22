package de.itshasan.iptv_core.model.series.info


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.ContentInfo
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season

data class SeriesInfo(
    @SerializedName("seasons")
    val seasons: List<Season>,
    @SerializedName("info")
    val info: Info,
    val episodes: MutableList<List<Episode>>
) : ContentInfo {
    fun exportAllEpisodes(): MutableList<Episode> {
        val exportedEpisodes = mutableListOf<Episode>()
        episodes.forEach { season ->
            exportedEpisodes.addAll(season)
        }
        return exportedEpisodes
    }

}