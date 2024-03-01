package com.example.outdoorescape

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class BrowseActivitiesFragment : Fragment() {

    private fun setCurrentFragment(fragment: Fragment) {
        (context as FragmentActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack("browse_activities_fragment")
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Changing status bar color to transparent
        WindowCompat.setDecorFitsSystemWindows(activity!!.window, false)
        activity?.window?.apply {
            // Set the status bar color to transparent
            statusBarColor = Color.TRANSPARENT
            // decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_activities, container, false)
    }

    @OptIn(ExperimentalTime::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvBrowseActivities = view.findViewById<RecyclerView>(R.id.rvBrowseActivities)

        rvBrowseActivities.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<ItemBrowseActivitiesViewModel>()

        for (i in 1..20) {
            data.add(
                ItemBrowseActivitiesViewModel(
                    R.drawable.ic_activity_running,
                    "My Running",
                    2.56f,
                    35L.minutes,
                    3564,
                    LocalDateTime.now()
                )
            )
            data.add(
                ItemBrowseActivitiesViewModel(
                    R.drawable.ic_activity_walking,
                    "Morning Walk",
                    4.46f,
                    45L.minutes,
                    3564,
                    LocalDateTime.now()
                )
            )
            data.add(
                ItemBrowseActivitiesViewModel(
                    R.drawable.ic_activity_nordic_walking,
                    "Walk in the park",
                    10.23f,
                    123L.minutes,
                    3564,
                    LocalDateTime.now().plusHours(7)
                )
            )
        }
        val adapter = BrowseActivitiesItemAdapter(data)
        adapter.setOnItemClickListener(object : BrowseActivitiesItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                setCurrentFragment(ActivityFragment())
            }
        })

        rvBrowseActivities.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
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
        fun newInstance(param1: String, param2: String) = BrowseActivitiesFragment()
    }
}