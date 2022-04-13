package de.itshasan.iptv_client.overview.ui.buttomSheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.overview.OverviewActivity
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_database.database.iptvDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val TAG = ModalBottomSheet::class.java.simpleName

class ModalBottomSheet(private val watchHistory: WatchHistory) : BottomSheetDialogFragment() {
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
            val intent =
                Intent(context, OverviewActivity::class.java).apply {
                    putExtra(Constant.SERIES_ID, watchHistory.parentId.toInt())
                    putExtra(Constant.COVER_URL, watchHistory.coverUrl)
                    putExtra(Constant.SERIES_TITLE, watchHistory.name)
                }
            startActivity(intent)
        }

        removeFromRow.setOnClickListener {
            Log.d(TAG, "onViewCreated: removeFromRow: ${watchHistory.parentId}")
            GlobalScope.launch(Dispatchers.IO) {
                iptvDatabase.watchHistoryDao()
                    .updateContinueWatchStatus(watchHistory.parentId, false)
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