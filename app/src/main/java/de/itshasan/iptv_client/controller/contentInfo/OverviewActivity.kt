package de.itshasan.iptv_client.controller.contentInfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.contentInfo.ui.OverviewFragment

class OverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.overview_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, OverviewFragment.newInstance())
                .commitNow()
        }
    }
}