package de.ndhbr.mytank.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.ndhbr.mytank.R
import de.ndhbr.mytank.models.Tank

class TankListAdapter(private val tankList: ArrayList<Tank>): RecyclerView.Adapter<TankListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvListItem = view.findViewById(R.id.tv_list_item) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_tank_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvListItem.text = tankList[position].name
    }

    override fun getItemCount(): Int {
        return tankList.size
    }

    fun updateData(scanResult: ArrayList<Tank>) {
        tankList.clear()
        tankList.addAll(scanResult)

        notifyDataSetChanged()
    }
}