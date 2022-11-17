package com.example.searchvehicleapp.ui.vehicle.listfragment

import android.content.ClipData
import androidx.lifecycle.*
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.launch

class VehicleViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    // Cache all items form the database using LiveData.
    fun getAllVehiclesByTypeOrderedByName(typeOfVehicle: EnumTypeOfVehicle): LiveData<List<Vehicle>> =
        vehicleDao.getAllVehiclesByTypeOrderedByName(typeOfVehicle).asLiveData()

    /**
     * Retrieve a vehicle from the repository.
     */
    fun getVehicleById(id: Int): LiveData<Vehicle> {
        return vehicleDao.getVehicleById(id).asLiveData()
    }

    /**
     * Launching a new coroutine to insert a vehicle in a non-blocking way
     */
    private fun insertItem(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.insert(
                vehicle = vehicle
            )
        }
    }

    /**
     * Launching a new coroutine to update a vehicle in a non-blocking way
     */
    private fun updateItem(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.update(
                vehicle = vehicle
            )
        }
    }

    /**
     * Launching a new coroutine to delete a vehicle in a non-blocking way
     */
    private fun deleteItem(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.delete(
                vehicle = vehicle
            )
        }
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(plate: String, brand: String, model: String): Vehicle {
        return Vehicle(
            plate = plate,
            brand = brand,
            model = model
        )
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }
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
