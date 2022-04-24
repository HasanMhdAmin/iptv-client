package de.itshasan.iptv_client.controller.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.player.ui.main.PlayerFragment

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment.newInstance())
                .commitNow()
        }
    }
}