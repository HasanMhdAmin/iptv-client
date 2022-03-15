package de.itshasan.iptv_client

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import de.itshasan.iptv_client.category.CategoryActivity
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.overview.OverviewActivity
import de.itshasan.iptv_database.database.IptvDatabase
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
    private val database by lazy { IptvDatabase.getInstance(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        GlobalScope.launch(Dispatchers.IO) {
            val history = database.watchHistoryDao().getContinueWatching()
            launch(Dispatchers.Main) {
                val continueWatchingAdapter = ContinueWatchingAdapter(history)
                continueWatchingRecyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    adapter = continueWatchingAdapter.apply {
                        onWatchHistoryClicked = {

                        }
                    }
                }
            }

        }
    }
}