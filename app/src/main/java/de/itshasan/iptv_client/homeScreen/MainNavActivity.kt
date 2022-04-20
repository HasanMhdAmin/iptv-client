package de.itshasan.iptv_client.homeScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.ActivityButtomNavBinding


private const val TAG = "MainNavActivity"

class MainNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityButtomNavBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.navigationBarColor = getColor(R.color.dark_gray)

        binding = ActivityButtomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_buttom_nav)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_download
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // TODO test later
        navView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home -> navController.navigateUp()
                R.id.favorite -> navController.navigateUp()
                R.id.download -> navController.navigateUp()
            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}