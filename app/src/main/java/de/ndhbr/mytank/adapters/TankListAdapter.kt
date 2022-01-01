package de.ndhbr.mytank.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ListTankItemBinding
import de.ndhbr.mytank.interfaces.TankListener
import de.ndhbr.mytank.models.Tank

class TankListAdapter(private val tankList: ArrayList<Tank>,
                      private val listener: TankListener) :
    RecyclerView.Adapter<TankListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListTankItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tank = tankList[position]
        holder.bind(tank, listener)
    }

    override fun getItemCount(): Int {
        return tankList.size
    }

    fun updateData(scanResult: ArrayList<Tank>) {
        tankList.clear()
        tankList.addAll(scanResult)

        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListTankItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tank: Tank, listener: TankListener) {
            binding.tvListItem.text = tank.name
            binding.tvTankSize.text = itemView.context.getString(R.string.metrics_litre,
                tank.size)
            binding.root.setOnClickListener { listener.onTankClick(tank) }
            binding.root.setOnLongClickListener { listener.onTankLongPress(tank) }
        }
    }
}