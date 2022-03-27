package de.itshasan.iptv_client

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.gson.Gson
import de.itshasan.iptv_client.category.CategoryActivity
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.overview.OverviewActivity
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

    private var player: ExoPlayer? = null
    val url =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private val playerBtn by lazy {
        findViewById<Button>(R.id.playerBtn)
    }
    private val catBtn by lazy {
        findViewById<Button>(R.id.catBtn)
    }
    private val detailsBtn by lazy {
        findViewById<Button>(R.id.detailsBtn)
    }
    private val continueWatchingRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.continueWatchingRecyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

//        val inputStream: InputStream = assets.open("m3u/channels.m3u")
//        val simpleM3UParser = SimpleM3UParser()
//        val list = simpleM3UParser.parse(inputStream)
//        val bbt = list.filter { it.name == "The Big Bang Theory S01 E01" }[0]
//
//        val liveList = mutableListOf<SimpleM3UParser.M3U_Entry>()
//        val seriesList = mutableListOf<SimpleM3UParser.M3U_Entry>()
//        val moviesList = mutableListOf<SimpleM3UParser.M3U_Entry>()
//
//        for (i in 0 until list.size) {
//            Log.d(TAG, "onCreate: groupTitle: ${list[i].groupTitle}")
//            when {
//                list[i].url.contains("series") -> {
//                    seriesList.add(list[i])
//                }
//                list[i].url.contains("movie") -> {
//                    moviesList.add(list[i])
//                }
//                else -> {
//                    liveList.add(list[i])
//                }
//            }
//        }
//
//
//        Log.d(TAG, "onCreate: liveList: ${liveList.size}")
//        Log.d(TAG, "onCreate: seriesList: ${seriesList.size}")
//        Log.d(TAG, "onCreate: moviesList: ${moviesList.size}")

        playerBtn.setOnClickListener {
            val intent = Intent(this, SimplePlayerActivity::class.java)
            startActivity(intent)
        }

        catBtn.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        detailsBtn.setOnClickListener {
            val intent = Intent(this, OverviewActivity::class.java)
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