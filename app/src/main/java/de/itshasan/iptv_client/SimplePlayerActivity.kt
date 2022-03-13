package de.itshasan.iptv_client

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import de.itshasan.iptv_client.player.listener.CustomOnScaleGestureListener
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_database.database.IptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository.getEpisodeStreamUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val TAG = SimplePlayerActivity::class.java.simpleName

class SimplePlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    var url =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private lateinit var episode: Episode
    private lateinit var seriesId: String
    private lateinit var coverUrl: String
    private val videoView by lazy {
        findViewById<PlayerView>(R.id.videoView)
    }
    private val database by lazy { IptvDatabase.getInstance(application) }

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var scaleGestureDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_player)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            val gson = Gson()
            val serializedEpisode = intent?.extras?.getString(Constant.CONTENT).toString()
            episode = gson.fromJson(serializedEpisode, Episode::class.java)
            seriesId = intent?.extras?.getString(Constant.SERIES_ID).toString()
            coverUrl = intent?.extras?.getString(Constant.COVER_URL).toString()
            val episodeUrl = getEpisodeStreamUrl(episode.id, episode.containerExtension)
            url = episodeUrl

            initializePlayer()
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
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
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
        }

        val progress = 100 * playbackPosition / player!!.contentDuration
        val totalTime = player?.contentDuration

        Log.d(TAG, "releasePlayer: seriesId : $seriesId")

        if (playbackPosition > 0) {

            GlobalScope.launch(Dispatchers.IO) {
                database.watchHistoryDao().insert(
                    WatchHistory(
                        0,
                        episode.id,
                        parentId = seriesId,
                        name = episode.title,
                        "s",
                        System.currentTimeMillis(),
                        currentTime = playbackPosition,
                        totalTime = totalTime!!,
                        coverUrl = coverUrl
                    ))
            }
        }

        Log.d(TAG, "releasePlayer: playbackPosition: $playbackPosition")
        Log.d(TAG, "releasePlayer: contentDuration: ${player?.contentDuration}")
        Log.d(TAG, "releasePlayer: currentWindow: $currentWindow")
        Log.d(TAG, "releasePlayer: playWhenReady: $playWhenReady")

        player = null
    }

    private fun initializePlayer() {
        player = ExoPlayer
            .Builder(this)
            .build()
            .also { exoPlayer ->
                videoView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(url)
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()

                scaleGestureDetector = ScaleGestureDetector(this, CustomOnScaleGestureListener(videoView))

            }
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

}