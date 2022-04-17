package de.itshasan.iptv_core.model.user


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_info")
    val userInfo: UserInfo,
    @SerializedName("server_info")
    val serverInfo: ServerInfo
)