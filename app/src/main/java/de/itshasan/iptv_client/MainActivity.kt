package de.itshasan.iptv_client

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.gson.Gson
import de.itshasan.iptv_client.category.CategoryActivity
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.overview.ui.buttomSheet.ModalBottomSheet
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {

    private val series by lazy {
        findViewById<CardView>(R.id.series)
    }

    private val continueWatchingRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.continueWatchingRecyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        series.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        refreshContinueWatch()
    }

    private fun refreshContinueWatch() {
        GlobalScope.launch(Dispatchers.IO) {
            val history = iptvDatabase.watchHistoryDao().getContinueWatching()
            launch(Dispatchers.Main) {
                val continueWatchingAdapter = ContinueWatchingAdapter(history)
                continueWatchingRecyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false)
                    adapter = continueWatchingAdapter.apply {
                        onWatchHistoryClicked = {

                            Log.d(TAG, "onResume: it: $it")

                            val dialog =
                                ProgressDialog.show(context,
                                    "Fetching content info",
                                    "preparing the content, Please wait...",
                                    true)

                            IptvRepository.getSeriesInfoBySeriesId(it.parentId,
                                object : SeriesInfoCallback() {
                                    override fun onSuccess(backendResponse: SeriesInfo) {
                                        val episodes = backendResponse.episodes
                                        var episode: Episode? = null
                                        for (season in episodes) {
                                            for (ep in season) {
                                                if (ep.id == it.contentId) {
                                                    episode = ep
                                                    break
                                                }
                                            }
                                        }

                                        dialog.dismiss()

                                        val intent =
                                            Intent(context,
                                                SimplePlayerActivity::class.java).apply {
                                                val gson = Gson()
                                                val serializedEpisode = gson.toJson(episode)
                                                putExtra(Constant.CONTENT, serializedEpisode)
                                                putExtra(Constant.SERIES_ID, it.parentId)
                                                putExtra(Constant.COVER_URL, it.coverUrl)
                                                putExtra(Constant.CURRENT_TIME, it.currentTime)
                                            }
                                        startActivity(intent)


                                    }

                                    override fun onError(status: Int, message: String) {

                                    }
                                })

                        }
                        onWatchHistoryLongClicked = {
                            Log.d(TAG, "onResume: $it is long clicked")
                            showBottomSheetDialog(it)
                        }
                    }
                }
            }
        }
    }

    private fun showBottomSheetDialog(watchHistory: WatchHistory) {
        val bottomSheetDialog = ModalBottomSheet(watchHistory)
        bottomSheetDialog.onItemRemovedCallback = {
            refreshContinueWatch()
        }
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
    }
}