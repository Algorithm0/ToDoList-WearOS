package com.example.todolist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainMenuAdapter(dataArgs: List<OneToDo>, callback: AdapterCallback?) :
    RecyclerView.Adapter<MainMenuAdapter.RecyclerViewHolder>() {
    private var dataSource = dataArgs

    interface AdapterCallback {
        fun onItemClicked(menuPosition: Int?)
    }

    private val callback: AdapterCallback?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_menu_item, parent, false)
        return RecyclerViewHolder(view)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var menuContainer: RelativeLayout = view.findViewById(R.id.menu_container)
        var menuItem: TextView = view.findViewById(R.id.menu_item)
        //var menuIcon: ImageView = view.findViewById(R.id.menu_icon)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        if (position == 0) {
            holder.menuItem.text = "Добавить"
            holder.menuContainer.setOnClickListener { callback?.onItemClicked(-222) }
        }
        else {
            holder.menuItem.text = dataSource[position-1].text
            holder.menuContainer.setOnClickListener { callback?.onItemClicked(dataSource[position-1].id) }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size+1
    }

    init {
        this.callback = callback
    }
}