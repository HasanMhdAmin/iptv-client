package de.itshasan.iptv_core.model.movie.movieInfo


import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("avg_frame_rate")
    val avgFrameRate: String,
    @SerializedName("bit_rate")
    val bitRate: String,
    @SerializedName("bits_per_sample")
    val bitsPerSample: Int,
    @SerializedName("channel_layout")
    val channelLayout: String,
    @SerializedName("channels")
    val channels: Int,
    @SerializedName("codec_long_name")
    val codecLongName: String,
    @SerializedName("codec_name")
    val codecName: String,
    @SerializedName("codec_tag")
    val codecTag: String,
    @SerializedName("codec_tag_string")
    val codecTagString: String,
    @SerializedName("codec_time_base")
    val codecTimeBase: String,
    @SerializedName("codec_type")
    val codecType: String,
    @SerializedName("dmix_mode")
    val dmixMode: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("duration_ts")
    val durationTs: Int,
    @SerializedName("index")
    val index: Int,
    @SerializedName("loro_cmixlev")
    val loroCmixlev: String,
    @SerializedName("loro_surmixlev")
    val loroSurmixlev: String,
    @SerializedName("ltrt_cmixlev")
    val ltrtCmixlev: String,
    @SerializedName("ltrt_surmixlev")
    val ltrtSurmixlev: String,
    @SerializedName("nb_frames")
    val nbFrames: String,
    @SerializedName("r_frame_rate")
    val rFrameRate: String,
    @SerializedName("sample_fmt")
    val sampleFmt: String,
    @SerializedName("sample_rate")
    val sampleRate: String,
    @SerializedName("start_pts")
    val startPts: Int,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("time_base")
    val timeBase: String
)