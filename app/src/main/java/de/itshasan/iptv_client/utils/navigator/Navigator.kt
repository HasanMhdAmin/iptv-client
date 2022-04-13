package de.itshasan.iptv_client.utils.navigator

import android.app.Activity
import android.content.Intent
import com.google.gson.Gson
import de.itshasan.iptv_client.SimplePlayerActivity
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.series.info.Episode

object Navigator {

    fun goToSimplePlayer(
        activity: Activity,
        episode: Episode,
        seriesId: String,
        coverUrl: String?,
        allEpisodes: List<Episode>
    ) {
        SimplePlayerActivity.allEpisode = allEpisodes
        val intent =
            Intent(activity,
                SimplePlayerActivity::class.java).apply {
                val gson = Gson()
                val serializedEpisode = gson.toJson(episode)
                putExtra(Constant.CONTENT, serializedEpisode)
                putExtra(Constant.SERIES_ID, seriesId)
                putExtra(Constant.COVER_URL, coverUrl)
            }
        activity.startActivity(intent)
    }
}