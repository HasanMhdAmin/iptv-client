package de.itshasan.iptv_core.model.series.info


import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season
import org.json.JSONArray
import org.json.JSONObject

data class SeriesInfo(
    @SerializedName("seasons")
    val seasons: List<Season>,
    @SerializedName("info")
    val info: Info,
//    @SerializedName("episodes")
//    val episodesMap: String,
    val episodes: MutableList<List<Episode>>
) {
    fun exportAllEpisodes(): List<Episode> {
        val exportedEpisodes = mutableListOf<Episode>()
        episodes.forEach { season ->
            exportedEpisodes.addAll(season)
//            season.forEach { episode ->
//                exportedEpisodes.add(episode)
//            }
        }
        return exportedEpisodes
    }

}