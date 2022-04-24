package de.itshasan.iptv_client.controller.contentInfo.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.DialogSeasonsBinding
import de.itshasan.iptv_client.controller.contentInfo.adapter.seasons.SeasonsAdapter
import de.itshasan.iptv_core.CoreDialog
import de.itshasan.iptv_core.model.series.info.season.Season

private val TAG = SeasonsDialog::class.java.simpleName

class SeasonsDialog : CoreDialog<DialogSeasonsBinding>(R.layout.dialog_seasons) {

    companion object {
        fun newInstance() = SeasonsDialog()
    }

    lateinit var seasons: List<Season>
    lateinit var selectedSeason: Season
    lateinit var onSeasonSelected: ((Season) -> Unit)

    override fun provideBinding(layoutInflater: LayoutInflater) =
        DialogSeasonsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {
        val seasonAdapter = SeasonsAdapter(seasons = seasons, selectedSeason = selectedSeason)

        binding.seasonsRecyclerview.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = seasonAdapter.apply {
                onSeasonClicked = {
                    onSeasonSelected(it)
                    dismiss()
                }
            }
        }

        binding.closeActionButton.setOnClickListener {
            dismiss()
        }
    }
}