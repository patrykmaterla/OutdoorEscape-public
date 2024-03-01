package com.example.outdoorescape

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat

class TrailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Changing status bar color to transparent
        WindowCompat.setDecorFitsSystemWindows(activity!!.window, false)
        activity?.window?.apply {
            // Set the status bar color to transparent
            statusBarColor = Color.TRANSPARENT
            // decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trail, container, false)
    }

    override fun onPause() {
        super.onPause()

        // fragmentManager?.popBackStack()

        activity?.window?.apply {
            // Restore the default status bar behavior
            WindowCompat.setDecorFitsSystemWindows(this, true)
            // Remove the flags that enabled drawing behind the status bar
            decorView.systemUiVisibility = 0
            // Set the status bar color back to the default theme color
            statusBarColor = resources.getColor(R.color.primary)
            val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK // Retrieve the Mode of the App.
            val isDarkModeOn = darkModeFlag == Configuration.UI_MODE_NIGHT_YES // Check if the Dark Mode is On
            if (isDarkModeOn) {
                decorView.systemUiVisibility = View.STATUS_BAR_VISIBLE;
            }
            else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) = TrailFragment()
    }
}