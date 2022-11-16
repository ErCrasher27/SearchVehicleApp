package com.example.searchvehicleapp.ui.vehicle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentVehicleBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */

class VehicleListAdapter(private val onVehicleClicked: (Vehicle) -> Unit) :
    ListAdapter<Vehicle, VehicleListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            FragmentVehicleBinding.inflate(
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

    class ItemViewHolder(private var binding: FragmentVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.sectionLabel.text = vehicle.name
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
