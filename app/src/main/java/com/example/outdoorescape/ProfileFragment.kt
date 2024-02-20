package com.example.outdoorescape

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var btnSettings: Button
private lateinit var myView: View

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Enabling no transparent status bar
        // WindowCompat.setDecorFitsSystemWindows(activity!!.window, true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Clear back stack completely on start of this fragment
        // fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        // Set status bar color
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //     activity?.window?.statusBarColor = resources.getColor(R.color.ProfilePink, null)
        //
        // }
        myView = inflater.inflate(R.layout.fragment_profile, container, false)

        myView.findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            var intent = Intent(activity, SettingsActivity::class.java)
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // finish()
        }
        return myView
    }

    /**
     * Recycler View
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the recyclerview by its id
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<ItemsViewModel>()
        data.add(ItemsViewModel(R.drawable.ic_saved_trails,getString(R.string.saved_trails), getString(R.string.saved_trails_description)))
        data.add(ItemsViewModel(R.drawable.ic_browse_activities, getString(R.string.browse_activities), getString(R.string.browse_activities_description)))
        // Dummy data
        // for (i in 1..20) {
        //     data.add(ItemsViewModel(R.drawable.baseline_settings_20, "Item $i"))
        // }

        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter
        /**
         * Changing fragment on click
         */
        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when(position) {
                    0 -> setCurrentFragment(SavedTrailsFragment())
                    1 -> setCurrentFragment(BrowseActivitiesFragment())
                }
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        (context as FragmentActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack("profile_fragment")
            commit()
        }
    }

    override fun onPause() {
        super.onPause()
        // Changing status bar color to transparent
        // WindowCompat.setDecorFitsSystemWindows(activity!!.window, false)
        // activity?.window?.apply {
        //     // Set the status bar color to transparent
        //     statusBarColor = Color.TRANSPARENT
        //
        //     // Add flags to the window to enable drawing behind the status bar
        //     // decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // }
    }

}