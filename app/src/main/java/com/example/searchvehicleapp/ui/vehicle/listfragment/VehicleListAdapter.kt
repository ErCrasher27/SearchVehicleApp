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

class VehicleListAdapter(
    private val onVehicleClicked: (Vehicle) -> Unit,
) :
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
        holder.bind(current, onVehicleClicked)
    }

    class ItemViewHolder(private var binding: VehicleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle, onVehicleClicked: (Vehicle) -> Unit) {
            binding.plate.text = vehicle.plate
            binding.model.text = vehicle.model
            binding.brand.text = vehicle.brand
            binding.buttonGoDetail.setOnClickListener { onVehicleClicked(vehicle) }
            /*if (vehicle.image != null) {
                binding.image.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            vehicle.image, 0, vehicle.image.size
                        ), binding.image.width, binding.image.height, false
                    )
                )
            } else {
                binding.image.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }

            binding.logoBrand.setImageBitmap(
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeByteArray(
                        vehicle.logoBrand, 0, vehicle.logoBrand.size
                    ), binding.logoBrand.width, binding.logoBrand.height, false
                )
            )*/

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem.model == newItem.model
            }
        }
    }
}
