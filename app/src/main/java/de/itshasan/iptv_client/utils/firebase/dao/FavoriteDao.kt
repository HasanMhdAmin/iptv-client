package de.itshasan.iptv_client.utils.firebase.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.itshasan.iptv_client.utils.firebase.Firestore
import de.itshasan.iptv_core.model.Favourite
import de.itshasan.iptv_core.storage.LocalStorage

private const val COLLECTION_NAME = "favorite"

class FavoriteDao(private val firestore: FirebaseFirestore) {

    fun addFavoriteToFirestore(favourite: Favourite) {
        // Add to firestore
        firestore
            .collection(COLLECTION_NAME)
            .document(favourite.uniqueId)
            .set(favourite)
    }

    fun removeFavoriteToFirestore(favourite: Favourite) {
        // Remove from firestore
        firestore
            .collection(COLLECTION_NAME)
            .document(favourite.uniqueId)
            .delete()
    }

    fun getUserFavorite(): Query {
        return Firestore.firestore.collection(COLLECTION_NAME)
            .whereEqualTo("userId", LocalStorage.getUniqueUserId())
    }

}