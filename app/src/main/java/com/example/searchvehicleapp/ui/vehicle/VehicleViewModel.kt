package com.example.searchvehicleapp.ui.vehicle

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.network.VehicleApi
import com.example.searchvehicleapp.network.VehicleInfo
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.example.searchvehicleapp.utils.EnumTypeOfTransmission
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

enum class CarMDStatus { LOADING, ERROR, DONE }

class VehicleViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    // Status CarMDApi
    private val _status = MutableLiveData<CarMDStatus>()
    val status: LiveData<CarMDStatus> = _status

    // Status CarMDApi
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
        displacement: String,
        typeOfFuel: EnumTypeOfFuel,
        forwardGearRatios: Int,
        typeOfTransmission: EnumTypeOfTransmission,
        cv: Int,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle
    ): Vehicle {
        return Vehicle(
            plate = plate,
            year = year,
            brand = brand,
            model = model,
            displacement = displacement,
            typeOfFuel = typeOfFuel,
            forwardGearRatios = forwardGearRatios,
            typeOfTransmission = typeOfTransmission,
            cv = cv,
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
        displacement: String,
        typeOfFuel: EnumTypeOfFuel,
        forwardGearRatios: Int,
        typeOfTransmission: EnumTypeOfTransmission,
        cv: Int,
        image: ByteArray?,
        typeOfVehicle: EnumTypeOfVehicle
    ): Vehicle {
        return Vehicle(
            id = id,
            plate = plate,
            year = year,
            brand = brand,
            model = model,
            displacement = displacement,
            typeOfFuel = typeOfFuel,
            forwardGearRatios = forwardGearRatios,
            typeOfTransmission = typeOfTransmission,
            cv = cv,
            image = image,
            typeOfVehicle = typeOfVehicle,
        )
    }

    /**
     * Inserts the new Vehicle into database.
     */
    fun addNewVehicle(
        plate: String,
        brand: String,
        model: String,
        typeOfVehicle: EnumTypeOfVehicle,
        year: Int,
        image: Bitmap?,
        line: String,
    ) {
        val newVehicle = getNewVehicleEntry(
            plate = plate,
            brand = brand,
            model = model,
            typeOfVehicle = typeOfVehicle,
            year = year,
            image = image?.toByteArray(),
            cV = cV,
            line = line,
            typeOfFuel = typeOfFuel
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
        year: Int,
        image: Bitmap?,
        line: String,
    ) {
        val updateVehicle = getUpdatedVehicleEntry(
            id = id,
            plate = plate,
            brand = brand,
            model = model,
            typeOfVehicle = typeOfVehicle,
            year = year,
            image = image?.toByteArray(),
            cV = cV,
            line = line,
            typeOfFuel = typeOfFuel
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
        year: String,
        cV: String,
        kW: String,
        line: String,
        typeOfFuel: String
    ): Boolean {
        if (plate.isBlank() || brand.isBlank() || model.isBlank() || year.isBlank() || cV.isBlank() || kW.isBlank() || line.isBlank() || typeOfFuel.isBlank()) {
            return false
        }
        return true
    }

    fun setCurrentTypeOfVehicle(enumTypeOfVehicle: EnumTypeOfVehicle) {
        _currentTypeOfVehicle.value = enumTypeOfVehicle
    }

    private fun Bitmap.toByteArray(quality: Int = 50): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    /**
     * Gets YearVehicle information from the Vehicle API Retrofit service and updates the
     * [_vehiclesInfo] [List] [LiveData].
     */
    fun getVehicleInfo() {
        viewModelScope.launch {
            _status.value = CarMDStatus.LOADING
            try {
                _vehiclesInfo.value = VehicleApi.retrofitService.getVehicleInfo()
                _status.value = CarMDStatus.DONE
            } catch (e: java.lang.Exception) {
                _status.value = CarMDStatus.ERROR
                _vehiclesInfo.value = listOf()
            }
        }
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
