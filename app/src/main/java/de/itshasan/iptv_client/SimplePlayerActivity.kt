package de.itshasan.iptv_client

import android.app.PictureInPictureParams
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EVENT_TRACKS_CHANGED
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import de.itshasan.iptv_client.player.listener.CustomOnScaleGestureListener
import de.itshasan.iptv_client.utils.firebase.Firestore
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.movie.Movie
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.storage.LocalStorage
import de.itshasan.iptv_database.database.iptvDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val TAG = SimplePlayerActivity::class.java.simpleName

class SimplePlayerActivity : AppCompatActivity(), Player.Listener {

    companion object {
        lateinit var allEpisode: List<Posterable>
    }

    private var player: ExoPlayer? = null

    private lateinit var type: String
    private lateinit var content: Posterable
    private lateinit var seriesId: String
    private lateinit var coverUrl: String

    // Views
    private val videoView by lazy {
        findViewById<PlayerView>(R.id.videoView)
    }
    private val back by lazy {
        findViewById<ImageView>(R.id.back)
    }
    private val titleTextView by lazy {
        findViewById<TextView>(R.id.titleTextView)
    }
    private val topGradient by lazy {
        findViewById<View>(R.id.topGradient)
    }
    private val episodesButton by lazy {
        findViewById<ConstraintLayout>(R.id.episodesButton)
    }

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_player)

        Log.d(TAG, "onCreate: ")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        videoView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                back.visibility = View.VISIBLE
                titleTextView.visibility = View.VISIBLE
                topGradient.visibility = View.VISIBLE
                episodesButton.visibility = View.VISIBLE
            } else {
                back.visibility = View.GONE
                titleTextView.visibility = View.GONE
                topGradient.visibility = View.GONE
                episodesButton.visibility = View.GONE
            }
        }

        back.setOnClickListener {
            onBackPressed()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            val gson = Gson()
            val serializedEpisode = intent?.extras?.getString(Constant.CONTENT).toString()

            type = intent?.extras?.getString(Constant.TARGET).toString()

            if (type == Constant.TYPE_MOVIES)
                content = gson.fromJson(serializedEpisode, Movie::class.java)
            if (type == Constant.TYPE_SERIES)
                content = gson.fromJson(serializedEpisode, Episode::class.java)

            seriesId = intent?.extras?.getString(Constant.CONTENT_ID).toString()
            coverUrl = intent?.extras?.getString(Constant.COVER_URL).toString()

            episodesButton.setOnClickListener {
                Navigator.goToEpisodesDialog(
                    content as Episode,
                    seriesId.toInt(),
                    coverUrl,
                    supportFragmentManager,
                    TAG
                )
            }

            GlobalScope.launch(Dispatchers.IO) {

                val watchHistory = iptvDatabase.watchHistoryDao()
                    .getContentItem(
                        (content).getId().toString()
                    )
                playbackPosition = watchHistory?.currentTime ?: 0

                launch(Dispatchers.Main) {
                    initializePlayer()
                }
            }
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        releasePlayer()
        finishAndRemoveTask()

        // TODO; uncomment and test, if it works, leave it.
//        allEpisode = emptyList()
    }

    private fun hideSystemUi() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        supportActionBar?.hide()
        actionBar?.hide()
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()

            val progress = 100 * playbackPosition / player!!.contentDuration
            val totalTime = player?.contentDuration

            Log.d(TAG, " (onResume) releasePlayer: seriesId : $seriesId")

            if (totalTime != null && totalTime > 0) {
                if (playbackPosition > 0) {
                    // Update WatchHistory
                    updateWatchHistory(totalTime)
                }
            }

            Log.d(TAG, "releasePlayer: playbackPosition: $playbackPosition")
            Log.d(TAG, "releasePlayer: contentDuration: ${player?.contentDuration}")
            Log.d(TAG, "releasePlayer: currentWindow: $currentWindow")
            Log.d(TAG, "releasePlayer: playWhenReady: $playWhenReady")


        }

        player = null
        setResult(RESULT_OK, null)

    }

    private fun updateWatchHistory(totalTime: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val watchHistory = WatchHistory(
                0,
                content.getId().toString(),
                parentId = seriesId,
                name = content.getTitle(),
                type,
                System.currentTimeMillis(),
                currentTime = playbackPosition,
                totalTime = totalTime,
                coverUrl = coverUrl,
                uniqueId = LocalStorage.getUniqueContentId(
                    content.getId().toString(),
                    type
                ),
                userId = LocalStorage.getUniqueUserId()
            )
            iptvDatabase.watchHistoryDao().insert(watchHistory)
            // Add to firestore
            Firestore.firestore
                .collection("watch_history")
                .document(watchHistory.uniqueId)
                .set(watchHistory)

            iptvDatabase.watchHistoryDao().updateContinueWatchStatus(seriesId, true)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        releasePlayer()
    }

    //Called when the user touches the Home or Recents button to leave the app.
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val pictureParams = PictureInPictureParams.Builder()
            .build()
        enterPictureInPictureMode(pictureParams)
    }

    private fun initializePlayer() {
        player = ExoPlayer
            .Builder(this)
            .build()
            .also { exoPlayer ->
                videoView.player = exoPlayer
                allEpisode.forEachIndexed { index, content ->
                    val episodeUrl = content.getStreamUrl()
                    val mediaItem = MediaItem.Builder()
                        .setMediaId(content.getId().toString())
                        .setTag(content)
                        .setUri(episodeUrl)
                        .build()
                    exoPlayer.addMediaItem(mediaItem)
                    Log.d(TAG, "initializePlayer: content: ${content.getId().toString()}")
                    Log.d(TAG, "initializePlayer: this.content: ${this.content.getId()}")
                    if (content.getId().toString() == this.content.getId().toString())
                        currentWindow = index
                }

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()

                scaleGestureDetector =
                    ScaleGestureDetector(this, CustomOnScaleGestureListener(videoView))
            }
        player?.addListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        scaleGestureDetector?.onTouchEvent(event)
        return true
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("playWhenReady", playWhenReady)
        savedInstanceState.putInt("currentWindow", currentWindow)
        savedInstanceState.putLong("playbackPosition", playbackPosition)

        // etc.
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        playWhenReady = savedInstanceState.getBoolean("playWhenReady")
        currentWindow = savedInstanceState.getInt("currentWindow")
        playbackPosition = savedInstanceState.getLong("playbackPosition")

    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        val totalTime = player.contentDuration
        updateWatchHistory(totalTime)
        if (events.contains(EVENT_TRACKS_CHANGED)) {
            Log.d(TAG, "onEvents: mediaId : ${player.currentMediaItem?.mediaId}")
            content =
                allEpisode.find { episode ->
                    episode.getId().toString() == player.currentMediaItem?.mediaId
                }!!
            titleTextView.text = content.getTitle()
        }
    }

}