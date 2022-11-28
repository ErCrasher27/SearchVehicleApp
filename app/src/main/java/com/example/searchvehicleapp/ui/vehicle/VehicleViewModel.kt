package com.example.searchvehicleapp.ui.vehicle

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.network.VehicleApi
import com.example.searchvehicleapp.network.VehicleInfo
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.launch

enum class CarMDStatus { LOADING, ERROR, DONE }

class VehicleViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    // Status Api
    private val _status = MutableLiveData<CarMDStatus>()
    val status: LiveData<CarMDStatus> = _status

    private val _vehiclesDataApi = MutableLiveData<List<VehicleInfo>>()
    val vehiclesDataApi: LiveData<List<VehicleInfo>> = _vehiclesDataApi

    private val _vehiclesInfo = MutableLiveData<List<VehicleInfo>>()
    val vehiclesInfo: LiveData<List<VehicleInfo>> = _vehiclesInfo

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
        plate: String,
        year: Int,
        brand: String,
        model: String,
        line: String,
        typeOfFuel: EnumTypeOfFuel,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle
    ): Vehicle {
        return Vehicle(
            plate = plate,
            year = year,
            brand = brand,
            model = model,
            line = line,
            typeOfFuel = typeOfFuel,
            image = image,
            typeOfVehicle = typeOfVehicle,
        )
    }

    /**
     * Called to update an existing entry in the Vehicle database.
     * Returns an instance of the [Vehicle] entity class with the vehicle info updated by the user.
     */
    private fun getUpdatedVehicleEntry(
        id: Int,
        plate: String,
        year: Int,
        brand: String,
        model: String,
        line: String,
        typeOfFuel: EnumTypeOfFuel,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle
    ): Vehicle {
        return Vehicle(
            id = id,
            plate = plate,
            year = year,
            brand = brand,
            model = model,
            line = line,
            typeOfFuel = typeOfFuel,
            image = image,
            typeOfVehicle = typeOfVehicle,
        )
    }

    /**
     * Inserts the new Vehicle into database.
     */
    fun addNewVehicle(
        plate: String,
        year: Int,
        brand: String,
        model: String,
        line: String,
        typeOfFuel: EnumTypeOfFuel,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle,
    ) {
        val newVehicle = getNewVehicleEntry(
            plate = plate.uppercase(),
            year = year,
            brand = brand,
            model = model,
            line = line,
            typeOfFuel = typeOfFuel,
            image = image,
            typeOfVehicle = typeOfVehicle,
        )
        insertVehicle(newVehicle)
    }

    /**
     * Updates an existing Vehicle in the database.
     */
    fun updateVehicle(
        id: Int,
        plate: String,
        year: Int,
        brand: String,
        model: String,
        line: String,
        typeOfFuel: EnumTypeOfFuel,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle,
    ) {
        val updateVehicle = getUpdatedVehicleEntry(
            id = id,
            plate = plate.uppercase(),
            year = year,
            brand = brand,
            model = model,
            line = line,
            typeOfFuel = typeOfFuel,
            image = image,
            typeOfVehicle = typeOfVehicle,
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
        year: String,
        brand: String,
        model: String,
        line: String,
    ): Boolean {
        if (plate.isBlank() || year.isBlank() || brand.isBlank() || model.isBlank() || line.isBlank()) {
            return false
        }
        return true
    }

    fun setCurrentTypeOfVehicle(enumTypeOfVehicle: EnumTypeOfVehicle) {
        _currentTypeOfVehicle.value = enumTypeOfVehicle
    }

    /**
     * Gets YearVehicle information from the Vehicle API Retrofit service and updates the
     * [_vehiclesInfo] [List] [LiveData].
     */
    fun getVehicleInfo() {
        viewModelScope.launch {
            _status.value = CarMDStatus.LOADING
            try {
                _vehiclesDataApi.value = VehicleApi.retrofitService.getVehicleInfo()
                resetVehicleInfo()
                _status.value = CarMDStatus.DONE
            } catch (e: java.lang.Exception) {
                _status.value = CarMDStatus.ERROR
                _vehiclesDataApi.value = listOf()
                resetVehicleInfo()
            }
        }
    }

    fun resetVehicleInfo() {
        _vehiclesInfo.value = _vehiclesDataApi.value
    }

    fun setVehicleInfoFilteredForYear(year: String) {
        resetVehicleInfo()
        _vehiclesInfo.value =
            vehiclesInfo.value?.filter { it.year.contains(year, ignoreCase = true) }

    }

    fun setVehicleInfoFilteredForBrand(brand: String) {
        resetVehicleInfo()
        _vehiclesInfo.value =
            vehiclesInfo.value?.filter { it.maker.contains(brand, ignoreCase = true) }

    }

    fun setVehicleInfoFilteredForModel(model: String) {
        resetVehicleInfo()
        _vehiclesInfo.value =
            _vehiclesInfo.value?.filter { it.model.contains(model, ignoreCase = true) }
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
