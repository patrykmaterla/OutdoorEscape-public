package com.example.outdoorescape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class ProfileEditFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var uid: String
    private lateinit var userRef: DocumentReference
    var user = User("" , "", "", "", "" ,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        userRef = firestore.collection("users").document(uid)

        userRef.get().addOnSuccessListener {
            if (it.exists()) {
                user = it.toObject<User>(User::class.java)!!
                view.findViewById<TextView>(R.id.tvEmail).text = user.email
                view.findViewById<EditText>(R.id.etFirstName).hint = it.getString("firstName")
                view.findViewById<EditText>(R.id.etLastName).hint = it.getString("lastName")
                view.findViewById<EditText>(R.id.etBirth).hint = it.getString("birth")
                view.findViewById<EditText>(R.id.etDescription).hint = it.getString("description")
            }
            else {

            }
        }
        .addOnFailureListener {

        }




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (view.findViewById<EditText>(R.id.etFirstName).text.isNotEmpty())
                user.firstName = view.findViewById<EditText>(R.id.etFirstName).text.toString()
            if (view.findViewById<EditText>(R.id.etLastName).text.isNotEmpty())
                user.lastName = view.findViewById<EditText>(R.id.etLastName).text.toString()
            if (view.findViewById<EditText>(R.id.etBirth).text.isNotEmpty())
                user.birth = view.findViewById<EditText>(R.id.etBirth).text.toString()
            if (view.findViewById<EditText>(R.id.etDescription).text.isNotEmpty())
                user.description = view.findViewById<EditText>(R.id.etDescription).text.toString()
            firestore.collection("users").document(uid).set(user)
                .addOnSuccessListener {
                    fragmentManager?.popBackStack()
                }
                .addOnFailureListener {

                }
        }
    }

    companion object {
        fun newInstance() = ProfileEditFragment()
    }
}