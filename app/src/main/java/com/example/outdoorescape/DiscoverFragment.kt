package com.example.outdoorescape

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DiscoverFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvDiscover = view.findViewById<RecyclerView>(R.id.rvDiscover)

        rvDiscover.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<ItemDiscoverViewModel>()

        // Dummy data
        for (i in 1..20) {
            data.add(ItemDiscoverViewModel(R.drawable.map, getString(R.string.discover_trail_title), getString(R.string.discover_trail_description), 2.13f))
        }

        val adapter = DiscoverItemAdapter(data)
        adapter.setOnItemClickListener(object : DiscoverItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                setCurrentFragment(TrailFragment())
            }
        })
        rvDiscover.adapter = adapter



        // rvDiscover.adapter = adapter
        /**
         * Changing fragment on click
         */

    }

    private fun setCurrentFragment(fragment: Fragment) {
        (context as FragmentActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack("discover_fragment")
            commit()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) = DiscoverFragment()
    }
}