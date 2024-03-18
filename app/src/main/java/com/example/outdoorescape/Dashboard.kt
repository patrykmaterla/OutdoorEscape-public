package com.example.outdoorescape

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.outdoorescape.databinding.ActivityDashboardBinding

/**
 * Everything was moved to MainActivity
 */
class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Status bar color
        // WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val discoverFragment = DiscoverFragment()

        // val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        // val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // // Passing each menu ID as a set of Ids because each
        // // menu should be considered as top level destinations.
        // val appBarConfiguration = AppBarConfiguration(
        //     setOf(
        //         R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        //     )
        // )
        // setupActionBarWithNavController(navController, appBarConfiguration)
        // bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_discover -> setCurrentFragment(DiscoverFragment())
                R.id.navigation_creation -> setCurrentFragment(CreationFragment())
                R.id.navigation_navigation -> setCurrentFragment(NavigationFragment())
                R.id.navigation_registration -> setCurrentFragment(RegistrationFragment())
                R.id.navigation_profile -> setCurrentFragment(ProfileFragment())
            }
            true
        }



    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
}