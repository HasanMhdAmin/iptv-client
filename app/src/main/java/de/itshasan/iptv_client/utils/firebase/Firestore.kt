package de.itshasan.iptv_client.utils.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.itshasan.iptv_client.utils.firebase.dao.FavoriteDao
import de.itshasan.iptv_database.database.dao.FavouriteDao

object Firestore {
    val firestore = Firebase.firestore


    fun favoriteDao(): FavoriteDao {
        return FavoriteDao(firestore)
    }


}