package de.itshasan.iptv_client.controller.contentInfo.ui.buttomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.homeScreen.ui.home.HomeViewModel
import de.itshasan.iptv_client.utils.firebase.Firestore
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_database.database.iptvDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val TAG = ModalBottomSheet::class.java.simpleName

class ModalBottomSheet(
    private val homeViewModel: HomeViewModel,
    private val watchHistory: WatchHistory
) : BottomSheetDialogFragment() {
    lateinit var onItemRemovedCallback: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val removeFromRow = view.findViewById<LinearLayout>(R.id.removeFromRow)
        val details = view.findViewById<LinearLayout>(R.id.details)

        details.setOnClickListener {
            Log.d(TAG, "onViewCreated: details:  ${watchHistory.parentId}")
            dismiss()
            val bundle = bundleOf(
                Constant.TARGET to watchHistory.contentType,
                Constant.CONTENT_ID to watchHistory.parentId.toInt(),
                Constant.COVER_URL to watchHistory.coverUrl,
                Constant.SERIES_TITLE to watchHistory.name
            )
            findNavController().navigate(R.id.action_navigation_home_to_navigation_details, bundle)
        }

        removeFromRow.setOnClickListener {
            watchHistory.showInContinueWatch = false
            Log.d(TAG, "onViewCreated: removeFromRow: ${watchHistory.parentId}")
            GlobalScope.launch(Dispatchers.IO) {
                iptvDatabase.watchHistoryDao()
                    .updateContinueWatchStatus(watchHistory.parentId, false)

                // Add to firestore
                Firestore.firestore
                    .collection("watch_history")
                    .document(watchHistory.uniqueId)
                    .set(watchHistory)

                launch(Dispatchers.Main) {
                    onItemRemovedCallback()
                    dismiss()
                }
            }
        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}