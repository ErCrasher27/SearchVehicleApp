package com.example.searchvehicleapp.ui.vehicle.listfragment

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.VehicleListItemBinding
import com.example.searchvehicleapp.network.logosapi.Logo
import com.example.searchvehicleapp.utils.setAndGetUriByBrandParsingListOfLogoAndImageView


/**
 * [ListAdapter] implementation for the recyclerview.
 */

class VehicleListAdapter(
    private val onVehicleClicked: (Vehicle) -> Unit,
    private val logoDataApi: List<Logo>?,
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
        holder.bind(current, onVehicleClicked, logoDataApi)
    }

    class ItemViewHolder(private var binding: VehicleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle, onVehicleClicked: (Vehicle) -> Unit, logoDataApi: List<Logo>?) {
            binding.apply {
                model.text = vehicle.model
                brand.text = vehicle.brand
                buttonGoDetail.setOnClickListener { onVehicleClicked(vehicle) }
                if (vehicle.image != null) {
                    val bmp = BitmapFactory.decodeByteArray(vehicle.image, 0, vehicle.image.size)
                    image.setImageBitmap(bmp)
                } else {
                    image.setImageResource(R.drawable.ic_baseline_directions_car_24)
                }
                setAndGetUriByBrandParsingListOfLogoAndImageView(
                    logoDataApi,
                    vehicle.brand,
                    binding.logoBrand
                )
            }
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
