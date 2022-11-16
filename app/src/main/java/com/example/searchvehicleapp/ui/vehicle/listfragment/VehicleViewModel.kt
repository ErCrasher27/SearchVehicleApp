package com.example.searchvehicleapp.ui.vehicle.listfragment

import androidx.lifecycle.*
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle

class VehicleViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    fun getAllVehiclesByType(typeOfVehicle: EnumTypeOfVehicle): LiveData<List<Vehicle>> =
        vehicleDao.getAllVehiclesByType(typeOfVehicle).asLiveData()
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class VehicleViewModelFactory(private val vehicleDao: VehicleDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return VehicleViewModel(vehicleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}