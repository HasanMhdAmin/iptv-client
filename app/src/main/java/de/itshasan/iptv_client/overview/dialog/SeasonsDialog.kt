package de.itshasan.iptv_client.overview.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.overview.adapter.seasons.SeasonsAdapter
import de.itshasan.iptv_core.model.series.info.season.Season

class SeasonsDialog : DialogFragment() {

    companion object {
        fun newInstance() = SeasonsDialog()
    }

    lateinit var seasons: List<Season>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.dialog_seasons, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.statusBarColor = resources.getColor(android.R.color.transparent)
        dialog?.window?.navigationBarColor = resources.getColor(android.R.color.transparent)

    }

    private fun setupView(view: View) {
        val seasonsRecyclerview = view.findViewById<RecyclerView>(R.id.seasonsRecyclerview)
        val closeActionButton = view.findViewById<FloatingActionButton>(R.id.closeActionButton)

        val categoryAdapter = SeasonsAdapter(seasons = seasons)

        seasonsRecyclerview.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = categoryAdapter.apply {
                onSeasonClicked = {

                }
            }
        }

        closeActionButton.setOnClickListener {
            dismiss()
        }
    }


}