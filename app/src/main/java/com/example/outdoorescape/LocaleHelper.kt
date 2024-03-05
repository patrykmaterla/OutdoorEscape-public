import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.PreferenceManager
import com.example.outdoorescape.MainActivity
import java.util.Locale


private lateinit var sharedPreferences: SharedPreferences

object LocaleHelper {
    // fun onAttach(context: Context) {
    //     val locale: String? = getPersistedLocale(context)
    //     return setLocale(context, locale)
    // }

    fun getPersistedLocale(context: Context): String {
        val preferences: SharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        return preferences.getString("lang_pref", "")!!
    }

    // the method is used to set the language at runtime
    fun setLocale(context: Context, language: String) {

        var ll = language

        sharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply() {
            putString("lang_pref", language)
            apply()
        }

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // if (ll != getPersistedLocale(context)) {
        //     triggerRestart(context)
        // }

        // Restart activity
        // recreate(activity)
    }

    fun getDeviceLocale(): String {
        return Locale.getDefault().language
    }

    fun triggerRestart(context: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            (context as Activity).finish()
        }
        Runtime.getRuntime().exit(0)
    }
}
