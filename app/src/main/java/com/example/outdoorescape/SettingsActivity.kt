package com.example.outdoorescape

import LocaleHelper
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

// private const val TITLE_TAG = "settingsActivityTitle"
private const val TITLE_TAG = "xDXDDD"


class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, PreferenceManager.OnPreferenceTreeClickListener, Preference.OnPreferenceClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    public val KEY_PREF_LANGUAGE = "pref_key_language"

    override fun onPreferenceClick(preference: Preference): Boolean {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()

        return true
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        val key  = preference.key

        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)



        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, HeaderFragment())
                .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
            }
        }

        setTitle(R.string.title_activity_settings)

        // val drawable: Drawable = toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // The Toolbar defined in the layout has the id "my_toolbar".
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window?.statusBarColor = resources.getColor(R.color.purple_500, null)
        }
    }
    // For back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle back button click here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment as String
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        // title = pref.title
        title = pref.title
        return true
    }

    class HeaderFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey)
            // Log out user from the app, clear is_signed_in Flag in shared_pref
            findPreference<Preference>("logout_header")?.setOnPreferenceClickListener {
                    val sharedPreferences = activity!!.getSharedPreferences("shared_pref", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    FirebaseAuth.getInstance().signOut();
                    editor.apply() {
                        putBoolean("is_signed_in", false)
                        apply()
                    }
                    Intent(requireActivity(), MainActivity::class.java).apply {
                        startActivity(this)
                    }
                    // Return true to indicate that the click event has been consumed
                    true
                }
            findPreference<Preference>("feedback_header")?.setOnPreferenceClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "developer@mail.com", null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "OutdoorEscape feedback")
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
                true
            }
        }
    }

    class MessagesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.messages_preferences, rootKey)
        }
    }

    class SyncFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.sync_preferences, rootKey)
        }
    }

    class LanguageFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.language_preferences, rootKey)
            // Find the preference
            val englishPreference = findPreference<Preference>("english")
            val polishPreference = findPreference<Preference>("polish")
            // Set click listener for the preference
            englishPreference?.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireContext(), "en")
                activity?.recreate()
                activity?.onBackPressed()
                // Return true to indicate that the click event has been consumed
                true
            }
            polishPreference?.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireContext(), "pl")
                activity?.recreate();
                // MainActivity().recreate()
                // LocaleHelper.triggerRestart(MainActivity())
                activity?.onBackPressed()
                // Return true to indicate that the click event has been consumed
                true
            }
        }
    }

    class FeedbackFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // setPreferencesFromResource(R.xml.feedback_preferences, rootKey)
        }
    }

    class AboutFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.about_preferences, rootKey)
            val version = BuildConfig.VERSION_NAME
            findPreference<Preference>("about_version")?.summary = version
        }
    }

    class LogoutFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // setPreferencesFromResource(R.xml.logout_preferences, "Language")
        }
    }

    class EnglishFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.language_preferences, "Language")
        }
    }

}
