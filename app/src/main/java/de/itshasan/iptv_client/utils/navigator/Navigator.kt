package de.itshasan.iptv_client.utils.navigator

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import de.itshasan.iptv_client.dialog.episodesDialog.EpisodesDialog
import de.itshasan.iptv_client.controller.homeScreen.MainNavActivity
import de.itshasan.iptv_client.controller.gallery.GalleryActivity
import de.itshasan.iptv_client.controller.simplePlayer.SimplePlayerActivity
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.series.info.Episode

object Navigator {

    fun goToSimplePlayer(
        activity: Activity,
        target: String,
        content: Posterable,
        seriesId: String,
        coverUrl: String?,
        allEpisodes: MutableList<Posterable>
    ) {
        if (allEpisodes.isEmpty())
            allEpisodes.add(content)
        SimplePlayerActivity.allEpisode = allEpisodes
        val intent =
            Intent(
                activity,
                SimplePlayerActivity::class.java
            ).apply {
                val gson = Gson()
                val serializedEpisode = gson.toJson(content)
                putExtra(Constant.TARGET, target)
                putExtra(Constant.CONTENT, serializedEpisode)
                putExtra(Constant.CONTENT_ID, seriesId)
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