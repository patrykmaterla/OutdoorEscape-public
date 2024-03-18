package com.example.outdoorescape

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for items in profile
 */
class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val textView: TextView
        val tvDescription: TextView

        init {
            imageView = view.findViewById(R.id.imageview)
            textView = view.findViewById(R.id.textView)
            tvDescription = view.findViewById(R.id.tvProfileItemDescription)

            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view_design, viewGroup, false)

        return ViewHolder(view, mListener)
    }

    // binds the list items to a view
    override fun onBindViewHolder(viewGroup: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        viewGroup.imageView.setImageResource(ItemsViewModel.image)

        viewGroup.textView.text = (ItemsViewModel.text)

        viewGroup.tvDescription.text = ItemsViewModel.description

        // Add extra margin to the first item
        val layoutParams = viewGroup.itemView.layoutParams as RecyclerView.LayoutParams
        if (position == 0) {
            // Add 2dp margin to the top for the first item
            val density = viewGroup.itemView.resources.displayMetrics.density
            val marginInDp = (2 * density + 0.5f).toInt() // Convert dp to pixels
            layoutParams.setMargins(marginInDp, marginInDp*2, marginInDp, marginInDp)
        }
        viewGroup.itemView.layoutParams = layoutParams

        // viewGroup.itemView.setOnClickListener {
        //     listener.onItemClick(position)
        // }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

}