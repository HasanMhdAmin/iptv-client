package de.itshasan.iptv_client.utils.navigator

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import de.itshasan.iptv_client.SimplePlayerActivity
import de.itshasan.iptv_client.episodesDialog.EpisodesDialog
import de.itshasan.iptv_client.homeScreen.MainNavActivity
import de.itshasan.iptv_client.seriesList.GalleryActivity
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
            Intent(
                activity,
                SimplePlayerActivity::class.java
            ).apply {
                val gson = Gson()
                val serializedEpisode = gson.toJson(episode)
                putExtra(Constant.CONTENT, serializedEpisode)
                putExtra(Constant.SERIES_ID, seriesId)
                putExtra(Constant.COVER_URL, coverUrl)
            }
        activity.startActivity(intent)
    }

    fun goToEpisodesDialog(
        episode: Episode,
        seriesId: Int,
        imageUrl: String,
        fragmentManager: FragmentManager,
        tag: String
    ) {
        val episodesDialog = EpisodesDialog.newInstance(episode, seriesId, imageUrl)
        episodesDialog.show(fragmentManager, tag)
    }

    fun goToMainActivity(activity: Activity) {
        val intent =
            Intent(
                activity,
                MainNavActivity::class.java
            )
        activity.startActivity(intent)
    }

    fun goToSeriesList(activity: Activity, category: String = Constant.ALL_SERIES) {
        val intent =
            Intent(activity, GalleryActivity::class.java).apply {
                putExtra(Constant.CATEGORY_ID, category)
            }
        activity.startActivity(intent)
    }
}