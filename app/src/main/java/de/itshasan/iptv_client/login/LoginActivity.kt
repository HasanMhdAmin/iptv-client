package de.itshasan.iptv_client.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.login.ui.LoginFragment
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_repository.storage.LocalStorage

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        if (LocalStorage.getServerUrl()?.isNotBlank()!!) {
            finish()
            Navigator.goToMainActivity(this)
        }

        setContentView(R.layout.overview_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}