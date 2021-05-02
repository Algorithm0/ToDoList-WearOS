package com.example.todolist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainMenuAdapter(dataArgs: List<TodoEntity>, private val callback: AdapterCallback?) :
        RecyclerView.Adapter<MainMenuAdapter.RecyclerViewHolder>() {
    private var dataSource = dataArgs

    interface AdapterCallback {
        fun onItemClicked(menuPosition: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var menuContainer: RelativeLayout = view.findViewById(R.id.menu_container)
        var menuItem: TextView = view.findViewById(R.id.menu_item)
        var menuIcon: ImageView = view.findViewById(R.id.menu_icon)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.menuItem.text = "Добавить";
                holder.menuIcon.visibility = View.VISIBLE
                holder.menuIcon.setImageResource(R.drawable.baseline_add_circle_white_36);
                holder.menuContainer.setOnClickListener { callback?.onItemClicked(-1) }
            }
            1 -> {
                if (dataSource.isEmpty()) {
                    holder.menuItem.text = "здесь пока\nничего нет"
                    holder.menuItem.textSize = 15f
                }
                else {
                    if (dataSource[position-1].content.length > 10)
                        holder.menuItem.text = dataSource[position-1].content.substring(0, 15)
                    else
                        holder.menuItem.text = dataSource[position-1].content
                    holder.menuIcon.visibility = View.INVISIBLE
                    holder.menuContainer.setOnClickListener { callback?.onItemClicked(dataSource[position-1].create_on) }
                }
            }
            else -> {
                holder.menuItem.text = dataSource[position-1].content
                holder.menuIcon.visibility = View.INVISIBLE
                holder.menuContainer.setOnClickListener { callback?.onItemClicked(dataSource[position-1].create_on) }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (dataSource.isEmpty()) 2
        else dataSource.size+1
    }

}