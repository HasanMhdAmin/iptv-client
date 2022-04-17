package de.itshasan.iptv_core.model.user


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("auth")
    val auth: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("exp_date")
    val expDate: String,
    @SerializedName("is_trial")
    val isTrial: String,
    @SerializedName("active_cons")
    val activeCons: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("max_connections")
    val maxConnections: String,
    @SerializedName("allowed_output_formats")
    val allowedOutputFormats: List<String>
)