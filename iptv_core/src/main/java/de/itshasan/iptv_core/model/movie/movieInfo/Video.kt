package de.itshasan.iptv_core.model.movie.movieInfo


import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("avg_frame_rate")
    val avgFrameRate: String,
    @SerializedName("bit_rate")
    val bitRate: String,
    @SerializedName("bits_per_raw_sample")
    val bitsPerRawSample: String,
    @SerializedName("chroma_location")
    val chromaLocation: String,
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
    @SerializedName("coded_height")
    val codedHeight: Int,
    @SerializedName("coded_width")
    val codedWidth: Int,
    @SerializedName("display_aspect_ratio")
    val displayAspectRatio: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("duration_ts")
    val durationTs: Int,
    @SerializedName("has_b_frames")
    val hasBFrames: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("index")
    val index: Int,
    @SerializedName("is_avc")
    val isAvc: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("nal_length_size")
    val nalLengthSize: String,
    @SerializedName("nb_frames")
    val nbFrames: String,
    @SerializedName("pix_fmt")
    val pixFmt: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("r_frame_rate")
    val rFrameRate: String,
    @SerializedName("refs")
    val refs: Int,
    @SerializedName("sample_aspect_ratio")
    val sampleAspectRatio: String,
    @SerializedName("start_pts")
    val startPts: Int,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("time_base")
    val timeBase: String,
    @SerializedName("width")
    val width: Int
)