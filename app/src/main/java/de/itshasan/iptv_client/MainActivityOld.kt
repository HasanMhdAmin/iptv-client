package de.itshasan.iptv_client

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import de.itshasan.iptv_client.category.CategoryActivity
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.login.LoginActivity
import de.itshasan.iptv_client.overview.ui.buttomSheet.ModalBottomSheet
import de.itshasan.iptv_client.utils.firebase.Firestore.firestore
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.utils.Serializer.appGson
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback
import de.itshasan.iptv_repository.storage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {

    private val series by lazy {
        findViewById<CardView>(R.id.series)
    }
    private val live by lazy {
        findViewById<CardView>(R.id.live)
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

        live.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        refreshContinueWatch()
    }

    private fun refreshContinueWatch() {
        Log.d(TAG, "onResume - > refreshContinueWatch: ")
        // TODO: Do i need to send the request in refresh? after exiting the view
        GlobalScope.launch(Dispatchers.IO) {

            // Pull db updates
            firestore.collection("users_test").whereEqualTo("userId", LocalStorage.getUniqueUserId()).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val jsonString = appGson.toJson(document.data)
                        val watchHistory = appGson.fromJson(jsonString, WatchHistory::class.java)
                        GlobalScope.launch(Dispatchers.IO) {
                            iptvDatabase.watchHistoryDao().insert(watchHistory)
                            val history = iptvDatabase.watchHistoryDao().getContinueWatching()

                            // Push db updates
                            history.forEachIndexed { index, it ->
                                firestore.collection("users_test").document(it.uniqueId).set(it)
                            }

                            launch(Dispatchers.Main) {
                                val continueWatchingAdapter = ContinueWatchingAdapter(history)
                                continueWatchingRecyclerView.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity, LinearLayoutManager.HORIZONTAL, false
                                    )
                                    adapter = continueWatchingAdapter.apply {
                                        onWatchHistoryClicked = {

                                            Log.d(TAG, "onResume: it: $it")

                                            val dialog = ProgressDialog.show(
                                                context,
                                                "Fetching content info",
                                                "preparing the content, Please wait...",
                                                true
                                            )

                                            IptvRepository.getSeriesInfoBySeriesId(
                                                it.parentId,
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

                                                        Navigator.goToSimplePlayer(
                                                            this@MainActivity,
                                                            episode = episode!!,
                                                            seriesId = it.parentId,
                                                            coverUrl = it.coverUrl,
                                                            backendResponse.exportAllEpisodes()
                                                        )
                                                    }

                                                    override fun onError(
                                                        status: Int, message: String
                                                    ) {

                                                    }
                                                })

                                        }
                                        onWatchHistoryLongClicked = {
                                            showBottomSheetDialog(it)
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
        }
    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshContinueWatch()
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