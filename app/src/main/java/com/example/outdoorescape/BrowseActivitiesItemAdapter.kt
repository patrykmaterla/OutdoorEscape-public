package com.example.outdoorescape

import android.os.Build
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Formatter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class BrowseActivitiesItemAdapter(private val mList: List<ItemBrowseActivitiesViewModel>) : RecyclerView.Adapter<BrowseActivitiesItemAdapter.ViewHolder>() {

    private var mListener : OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(view) {
        val ivActivityType: ImageView
        val tvActivityTitle: TextView
        val tvDistanceValue: TextView
        val tvActivityDuration: TextView
        val tvStepsValue: TextView
        val tvDate: TextView
        val tvTime: TextView

        init {
            ivActivityType = view.findViewById(R.id.ivActivityType)
            tvActivityTitle = view.findViewById(R.id.tvActivityTitle)
            tvDistanceValue = view.findViewById(R.id.tvDistanceValue)
            tvActivityDuration = view.findViewById(R.id.tvActivityDuration)
            tvStepsValue = view.findViewById(R.id.tvStepsValue)
            tvDate = view.findViewById(R.id.tvDate)
            tvTime = view.findViewById(R.id.tvTime)

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
            .inflate(R.layout.item_activity, viewGroup, false)

        return ViewHolder(view, mListener)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    override fun onBindViewHolder(viewGroup: ViewHolder, position: Int) {

        val itemBrowseActivitiesViewModel = mList[position]

        val duration = itemBrowseActivitiesViewModel.duration as Duration

        val seconds = duration.toLong(DurationUnit.SECONDS)

        // var formatter = DateTimeFormatter.ofPattern("dd-MM")
        var date = itemBrowseActivitiesViewModel.date // Represents February 2, 2023
        // val date = LocalDate.of(2023, 2, 2) // Represents February 2, 2023
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = date.format(formatter)
        formatter = DateTimeFormatter.ofPattern("hh:mm")
        val formattedTime = date.format(formatter)

        // var formatter = DateTimeFormatter.ofPattern("dd-MM")
        // var date2 = LocalDate.parse(date.toString(), formatter)
        // var thisDate = (if (date.day < 10) "0${date.day}" else "${date.day}") + "." + (if (date.month < 10) "0${date.day}" else "${date.month}")

        viewGroup.ivActivityType.setImageResource((itemBrowseActivitiesViewModel.icon))
        viewGroup.tvActivityTitle.text = (itemBrowseActivitiesViewModel.title)
        viewGroup.tvDistanceValue.text = (itemBrowseActivitiesViewModel.distance.toString())
        viewGroup.tvActivityDuration.text = formatDurationTime(seconds)
        viewGroup.tvStepsValue.text = (itemBrowseActivitiesViewModel.steps.toString())
        viewGroup.tvDate.text = formattedDate
        viewGroup.tvTime.text = formattedTime

        // viewGroup.itemView.setOnClickListener {
        //     listener.onItemClick(position)
        // }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    fun formatDurationTime(durationSeconds: Long): String {
        var hours = 0L
        var minutes = 0L
        var seconds = durationSeconds
        if (seconds >= 3600) {
            hours = seconds / 3600
            seconds -= hours * 3600
        }
        if (seconds >= 60) {
            minutes = seconds / 60
            seconds -= minutes * 60
        }
        return Formatter().format("%1\$02d:%2\$02d:%3\$02d", hours, minutes, seconds).toString()
    }

}