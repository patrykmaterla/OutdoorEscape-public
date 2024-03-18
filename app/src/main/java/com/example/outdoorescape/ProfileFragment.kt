package com.example.outdoorescape

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firestore = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = firestore.collection("users").document(uid)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // currentUser = documentSnapshot.toObject(User::class.java)!!
                    // // Update UI elements with user data (e.g., username, email)
                    // binding.usernameTextView.text = currentUser.username
                    // binding.emailTextView.text = currentUser.email
                    if (documentSnapshot.getString("firstName")!!.isNotEmpty() || documentSnapshot.getString("lastName")!!.isNotEmpty()) {
                        view.findViewById<TextView>(R.id.nameTextView).text = "${documentSnapshot.getString("firstName")} ${documentSnapshot.getString("lastName")}"
                    }
                    view.findViewById<TextView>(R.id.tvDescription).text = documentSnapshot.getString("description")


                } else {
                    // Handle case where user data doesn't exist
                }
            }
            .addOnFailureListener { e ->
                // Handle errors fetching user data
            }

        // Clear back stack completely on start of this fragment
        // fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        view.findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            var intent = Intent(activity, SettingsActivity::class.java)
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // finish()
        }

        view.findViewById<ImageButton>(R.id.btnProfileEdit).setOnClickListener {
            setCurrentFragment(ProfileEditFragment())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // getting the recyclerview by its id
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<ItemsViewModel>()
        data.add(ItemsViewModel(R.drawable.ic_saved_trails,getString(R.string.saved_trails), getString(R.string.saved_trails_description)))
        data.add(ItemsViewModel(R.drawable.ic_browse_activities, getString(R.string.browse_activities), getString(R.string.browse_activities_description)))

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

    private fun setCurrentFragment(fragment: Fragment) {
        (context as FragmentActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack("profile_fragment")
            commit()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ProfileFragment()
    }
}