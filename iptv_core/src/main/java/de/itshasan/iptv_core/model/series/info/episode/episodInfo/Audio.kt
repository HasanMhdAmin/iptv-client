package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("index")
    val index: Int,
    @SerializedName("codec_name")
    val codecName: String,
    @SerializedName("codec_long_name")
    val codecLongName: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("codec_type")
    val codecType: String,
    @SerializedName("codec_time_base")
    val codecTimeBase: String,
    @SerializedName("codec_tag_string")
    val codecTagString: String,
    @SerializedName("codec_tag")
    val codecTag: String,
    @SerializedName("sample_fmt")
    val sampleFmt: String,
    @SerializedName("sample_rate")
    val sampleRate: String,
    @SerializedName("channels")
    val channels: Int,
    @SerializedName("channel_layout")
    val channelLayout: String,
    @SerializedName("bits_per_sample")
    val bitsPerSample: Int,
    @SerializedName("r_frame_rate")
    val rFrameRate: String,
    @SerializedName("avg_frame_rate")
    val avgFrameRate: String,
    @SerializedName("time_base")
    val timeBase: String,
    @SerializedName("start_pts")
    val startPts: Int,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("duration_ts")
    val durationTs: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("bit_rate")
    val bitRate: String,
    @SerializedName("max_bit_rate")
    val maxBitRate: String,
    @SerializedName("nb_frames")
    val nbFrames: String,
    @SerializedName("disposition")
    val disposition: Disposition,
    @SerializedName("tags")
    val tags: Tags
)