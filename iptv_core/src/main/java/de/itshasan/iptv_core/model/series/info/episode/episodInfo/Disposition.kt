package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class Disposition(
    @SerializedName("default")
    val default: Int,
    @SerializedName("dub")
    val dub: Int,
    @SerializedName("original")
    val original: Int,
    @SerializedName("comment")
    val comment: Int,
    @SerializedName("lyrics")
    val lyrics: Int,
    @SerializedName("karaoke")
    val karaoke: Int,
    @SerializedName("forced")
    val forced: Int,
    @SerializedName("hearing_impaired")
    val hearingImpaired: Int,
    @SerializedName("visual_impaired")
    val visualImpaired: Int,
    @SerializedName("clean_effects")
    val cleanEffects: Int,
    @SerializedName("attached_pic")
    val attachedPic: Int,
    @SerializedName("timed_thumbnails")
    val timedThumbnails: Int
)