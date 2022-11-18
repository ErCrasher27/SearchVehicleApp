package com.example.searchvehicleapp.ui.vehicle

import androidx.lifecycle.*
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.launch

class VehicleViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    // currentTypeOfVehicle
    private val _currentTypeOfVehicle = MutableLiveData<EnumTypeOfVehicle>()
    val currentTypeOfVehicle: LiveData<EnumTypeOfVehicle> = _currentTypeOfVehicle

    // Cache all items form the database using LiveData.
    fun getAllVehiclesByTypeOrderedByName(typeOfVehicle: EnumTypeOfVehicle): LiveData<List<Vehicle>> =
        vehicleDao.getAllVehiclesByTypeOrderedByName(typeOfVehicle).asLiveData()

    /**
     * Retrieve a vehicle from the repository.
     */
    fun getVehicleById(id: Int): LiveData<Vehicle> = vehicleDao.getVehicleById(id).asLiveData()

    /**
     * Returns an instance of the [Vehicle] entity class with the vehicle info entered by the user.
     * This will be used to add a new entry to the Vehicle database.
     */
    private fun getNewVehicleEntry(
        plate: String, brand: String, model: String, typeOfVehicle: EnumTypeOfVehicle, year: Int
    ): Vehicle {
        return Vehicle(
            plate = plate, brand = brand, model = model, typeOfVehicle = typeOfVehicle, year = year
        )
    }

    /**
     * Called to update an existing entry in the Vehicle database.
     * Returns an instance of the [Vehicle] entity class with the vehicle info updated by the user.
     */
    private fun getUpdatedVehicleEntry(
        id: Int,
        plate: String,
        brand: String,
        model: String,
        typeOfVehicle: EnumTypeOfVehicle,
        year: Int
    ): Vehicle {
        return Vehicle(
            id = id,
            plate = plate,
            brand = brand,
            model = model,
            typeOfVehicle = typeOfVehicle,
            year = year
        )
    }

    /**
     * Inserts the new Vehicle into database.
     */
    fun addNewVehicle(
        plate: String, brand: String, model: String, typeOfVehicle: EnumTypeOfVehicle, year: Int
    ) {
        val newVehicle = getNewVehicleEntry(
            plate = plate, brand = brand, model = model, typeOfVehicle = typeOfVehicle, year = year
        )
        insertVehicle(newVehicle)
    }

    /**
     * Updates an existing Vehicle in the database.
     */
    fun updateVehicle(
        id: Int,
        plate: String,
        brand: String,
        model: String,
        typeOfVehicle: EnumTypeOfVehicle,
        year: Int
    ) {
        val updateVehicle = getUpdatedVehicleEntry(
            id = id,
            plate = plate,
            brand = brand,
            model = model,
            typeOfVehicle = typeOfVehicle,
            year = year
        )
        updateVehicle(updateVehicle)
    }

    /**
     * Launching a new coroutine to insert a vehicle in a non-blocking way
     */
    private fun insertVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.insert(
                vehicle = vehicle
            )
        }
    }

    /**
     * Launching a new coroutine to update a vehicle in a non-blocking way
     */
    private fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.update(
                vehicle = vehicle
            )
        }
    }

    /**
     * Launching a new coroutine to delete a vehicle in a non-blocking way
     */
    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.delete(
                vehicle = vehicle
            )
        }
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(
        plate: String,
        brand: String,
        model: String,
        year: String
    ): Boolean {
        if (plate.isBlank() || brand.isBlank() || model.isBlank() || year.isBlank()) {
            return false
        }
        return true
    }

    fun setCurrentTypeOfVehicle(enumTypeOfVehicle: EnumTypeOfVehicle) {
        _currentTypeOfVehicle.value = enumTypeOfVehicle
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
