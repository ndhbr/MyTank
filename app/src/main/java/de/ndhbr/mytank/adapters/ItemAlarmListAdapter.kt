package de.ndhbr.mytank.adapters

import android.content.Context
import android.net.wifi.ScanResult
import android.telephony.NetworkScanRequest
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.ndhbr.mytank.databinding.ListItemAlarmItemBinding
import de.ndhbr.mytank.interfaces.ItemAlarmListener
import de.ndhbr.mytank.models.ItemAlarm

class ItemAlarmListAdapter(
    private val itemAlarmList: ArrayList<ItemAlarm>,
    private val listener: ItemAlarmListener
) : RecyclerView.Adapter<ItemAlarmListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemAlarmListAdapter.ViewHolder {

        val binding = ListItemAlarmItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarmItem = itemAlarmList[position]
        holder.bind(alarmItem, listener)
    }

    override fun getItemCount(): Int {
        return itemAlarmList.size
    }

    fun updateData(scanResult: ArrayList<ItemAlarm>) {
        itemAlarmList.clear()
        itemAlarmList.addAll(scanResult)

        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ListItemAlarmItemBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemAlarm: ItemAlarm, listener: ItemAlarmListener) {
            with(binding) {
                tvItemAlarmListTitle.text = itemAlarm.name
                tvItemAlarmListSubtitle.text = itemAlarm.tankName

                root.setOnClickListener {
                    listener.onItemAlarmClick(itemAlarm)
                }
                root.setOnLongClickListener {
                    listener.onItemAlarmLongPress(itemAlarm)
                }
            }
        }
    }
}