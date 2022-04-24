package de.itshasan.iptv_client.controller.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.gallery.ui.main.GalleryFragment

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GalleryFragment.newInstance())
                .commitNow()
        }
    }
}