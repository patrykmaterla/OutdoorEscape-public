package com.example.outdoorescape

import LocaleHelper
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.outdoorescape.databinding.ActivityDashboardBinding

private lateinit var sharedPreferences: SharedPreferences
private var isSignedIn: Boolean? = null
private var langPref: String? = null
private var once: Boolean = true

class MainActivity : AppCompatActivity() {

    private var initialLocale: String? = null
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        initialLocale = LocaleHelper.getPersistedLocale(this)
    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)

        langPref = sharedPreferences.getString("lang_pref", "")
        isSignedIn = sharedPreferences.getBoolean("is_signed_in", false)

        if (langPref == "") {
            langPref = LocaleHelper.getDeviceLocale()
        }
        LocaleHelper.setLocale(this, langPref!!)
        while (once) {
            recreate()
            once = false
        }

        // If user did not signed in send him to AuthActivity
        if (isSignedIn == false) {
            Intent(this@MainActivity, AuthActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        // var account = GoogleSignIn.getLastSignedInAccount(this)

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (initialLocale != null && !initialLocale.equals(LocaleHelper.getPersistedLocale(this))) {
            recreate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with location-related tasks
                } else {
                    // Permission not granted
                }
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
