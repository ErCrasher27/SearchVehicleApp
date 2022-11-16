package com.example.searchvehicleapp.ui.vehicle.listfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.VehicleListItemBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */

class VehicleListAdapter(private val onVehicleClicked: (Vehicle) -> Unit) :
    ListAdapter<Vehicle, VehicleListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VehicleListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onVehicleClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: VehicleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.id.text = vehicle.id.toString()
            binding.name.text = vehicle.name
            binding.type.text = vehicle.type.toString()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}
