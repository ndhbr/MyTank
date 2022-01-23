package de.ndhbr.mytank.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ListFishStockItemBinding
import de.ndhbr.mytank.interfaces.TankItemListener
import de.ndhbr.mytank.models.TankItem

class FishStockListAdapter(
    private val tankStockList: ArrayList<TankItem>,
    private val listener: TankItemListener
) :
    RecyclerView.Adapter<FishStockListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FishStockListAdapter.ViewHolder {
        val binding = ListFishStockItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: FishStockListAdapter.ViewHolder, position: Int) {
        val tankItem = tankStockList[position]
        holder.bind(tankItem, listener)
    }

    override fun getItemCount(): Int {
        return tankStockList.size
    }

    fun updateData(scanResult: ArrayList<TankItem>) {
        tankStockList.clear()
        tankStockList.addAll(scanResult)

        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ListFishStockItemBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tankItem: TankItem, listener: TankItemListener) {
            val types = context.resources.getStringArray(R.array.item_type)

            with(binding) {
                tvFishStockListTitle.text = tankItem.name
                tvFishStockListSubtitle.text = tankItem.type?.name
                if (tankItem.type != null) {
                    tvFishStockListSubtitle.text = types[tankItem.type?.ordinal!!]
                } else {
                    tvFishStockListSubtitle.text = types[0]
                }
                tvFishStockCount.text = "${tankItem.count}x"
                root.setOnClickListener {
                    listener.onTankItemClick(tankItem)
                }
                root.setOnLongClickListener {
                    listener.onTankItemLongPress(tankItem)
                }
            }

        }
    }
}