package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class EpisodInfo(
    @SerializedName("duration_secs")
    val durationSecs: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("video")
    val video: Video,
    @SerializedName("audio")
    val audio: Audio,
    @SerializedName("bitrate")
    val bitrate: Int
)