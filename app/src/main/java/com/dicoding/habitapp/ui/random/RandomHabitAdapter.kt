package com.dicoding.habitapp.ui.random

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit

class RandomHabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {

    private val habitMap = LinkedHashMap<PageType, Habit>()

    fun submitData(key: PageType, habit: Habit) {
        habitMap[key] = habit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_item, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = getIndexKey(position) ?: return
        val pageData = habitMap[key] ?: return
        holder.bind(key, pageData)
    }

    override fun getItemCount() = habitMap.size

    private fun getIndexKey(position: Int) = habitMap.keys.toTypedArray().getOrNull(position)

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val pager_tv_title: TextView = itemView.findViewById(R.id.pager_tv_title)
        private val pager_tv_start_time: TextView = itemView.findViewById(R.id.pager_tv_start_time)
        private val item_priority_level:ImageView = itemView.findViewById(R.id.item_priority_level)
        private val pager_tv_minutes:TextView = itemView.findViewById(R.id.pager_tv_minutes)
        private val btn_open_count_down:Button = itemView.findViewById(R.id.btn_open_count_down)

        //TODO 14 : Create view and bind data to item view

        fun bind(pageType: PageType, pageData: Habit) {
            btn_open_count_down.setOnClickListener{
                onClick(pageData)
            }
            pager_tv_title.text = pageData.title
            pager_tv_start_time.text = pageData.startTime
            when(pageType){
                PageType.HIGH -> item_priority_level.setImageResource(R.drawable.ic_priority_high)
                PageType.MEDIUM -> item_priority_level.setImageResource(R.drawable.ic_priority_medium)
                PageType.LOW -> item_priority_level.setImageResource(R.drawable.ic_priority_low)
            }
            pager_tv_minutes.text = "${pageData.minutesFocus}"

        }
    }
}
