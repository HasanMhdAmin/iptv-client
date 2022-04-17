package de.itshasan.iptv_repository.network.service

import de.itshasan.iptv_core.model.user.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("player_api.php")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String,
    ): Call<User>
}