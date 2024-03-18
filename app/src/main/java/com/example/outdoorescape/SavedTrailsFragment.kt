package com.example.outdoorescape

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedTrailsFragment : Fragment() {

    private fun setCurrentFragment(fragment: Fragment) {
        (context as FragmentActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack("saved_trails_fragment")
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
        return inflater.inflate(R.layout.fragment_saved_trails, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSavedTrails = view.findViewById<RecyclerView>(R.id.rvSavedTrails)
        rvSavedTrails.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<ItemSavedTrailsViewModel>()
        // Dummy data
        for (i in 1..50) {
            data.add(ItemSavedTrailsViewModel(R.drawable.map, getString(R.string.discover_trail_title), getString(R.string.discover_trail_description), 2.13f))
        }

        val adapter = SavedTrailsItemAdapter(data)
        adapter.setOnItemClickListener(object : SavedTrailsItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                setCurrentFragment(TrailFragment())
            }
        })
        rvSavedTrails.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
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
        fun newInstance(param1: String, param2: String) = SavedTrailsFragment()
    }
}