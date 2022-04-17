package de.itshasan.iptv_core.model.user


import com.google.gson.annotations.SerializedName

data class ServerInfo(
    @SerializedName("url")
    val url: String,
    @SerializedName("port")
    val port: String,
    @SerializedName("https_port")
    val httpsPort: String,
    @SerializedName("server_protocol")
    val serverProtocol: String,
    @SerializedName("rtmp_port")
    val rtmpPort: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timestamp_now")
    val timestampNow: Int,
    @SerializedName("time_now")
    val timeNow: String,
    @SerializedName("process")
    val process: Boolean
)