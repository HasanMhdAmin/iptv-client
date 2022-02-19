package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class Video(
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
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("coded_width")
    val codedWidth: Int,
    @SerializedName("coded_height")
    val codedHeight: Int,
    @SerializedName("has_b_frames")
    val hasBFrames: Int,
    @SerializedName("pix_fmt")
    val pixFmt: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("chroma_location")
    val chromaLocation: String,
    @SerializedName("refs")
    val refs: Int,
    @SerializedName("is_avc")
    val isAvc: String,
    @SerializedName("nal_length_size")
    val nalLengthSize: String,
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
    @SerializedName("bits_per_raw_sample")
    val bitsPerRawSample: String,
    @SerializedName("nb_frames")
    val nbFrames: String,
    @SerializedName("disposition")
    val disposition: Disposition,
    @SerializedName("tags")
    val tags: Tags
)