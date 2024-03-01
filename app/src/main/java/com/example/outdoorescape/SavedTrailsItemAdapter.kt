package com.example.outdoorescape

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SavedTrailsItemAdapter(private val mList: List<ItemSavedTrailsViewModel>) : RecyclerView.Adapter<SavedTrailsItemAdapter.ViewHolder>() {

    private var mListener : OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        val ivMap: ImageView
        val tvTitle: TextView
        val tvDescription: TextView
        val tvDistance: TextView

        init {
            ivMap = view.findViewById(R.id.ivMap)
            view.context
            tvTitle = view.findViewById(R.id.tvTitle)
            tvDescription = view.findViewById(R.id.tvDescription)
            tvDistance = view.findViewById(R.id.tvTrailDistanceValue)

            view.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    // create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_saved_trail, viewGroup, false)

        return ViewHolder(view, mListener)
    }

    // binds the list items to a view
    override fun onBindViewHolder(viewGroup: ViewHolder, position: Int) {

        val itemSavedTrailsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        viewGroup.ivMap.setImageResource(itemSavedTrailsViewModel.image)
        viewGroup.tvTitle.text = (itemSavedTrailsViewModel.title)
        viewGroup.tvDescription.text = (itemSavedTrailsViewModel.description)
        viewGroup.tvDistance.text = itemSavedTrailsViewModel.distance.toString()

        // viewGroup.itemView.setOnClickListener {
        //     listener.onItemClick(position)
        // }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
}