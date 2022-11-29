package com.example.searchvehicleapp.ui.vehicle

import android.app.Application
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.database.VehicleDao
import com.example.searchvehicleapp.network.VehicleApi
import com.example.searchvehicleapp.network.logosapi.Logo
import com.example.searchvehicleapp.network.logosapi.LogoApi
import com.example.searchvehicleapp.network.vehicleapi.VehicleInfo
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import com.example.searchvehicleapp.worker.MyGarageReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class VehicleApiStatus { LOADING, ERROR, DONE }
enum class LogoApiStatus { LOADING, ERROR, DONE }

class VehicleViewModel(
    private val vehicleDao: VehicleDao,
    application: Application
) : ViewModel() {

    // Status Vehicle Api
    private val _statusVehicleApi = MutableLiveData<VehicleApiStatus>()
    val statusVehicleApi: LiveData<VehicleApiStatus> = _statusVehicleApi

    private val _vehiclesDataApi = MutableLiveData<List<VehicleInfo>>()
    val vehiclesDataApi: LiveData<List<VehicleInfo>> = _vehiclesDataApi

    private val _vehiclesInfo = MutableLiveData<List<VehicleInfo>>()
    val vehiclesInfo: LiveData<List<VehicleInfo>> = _vehiclesInfo

    // Status Logo Api
    private val _statusLogApi = MutableLiveData<LogoApiStatus>()
    val statusLogApi: LiveData<LogoApiStatus> = _statusLogApi

    private val _logoDataApi = MutableLiveData<List<Logo>>()
    val logoDataApi: LiveData<List<Logo>> = _logoDataApi

    // currentTypeOfVehicle
    private val _currentTypeOfVehicle = MutableLiveData<EnumTypeOfVehicle>()
    val currentTypeOfVehicle: LiveData<EnumTypeOfVehicle> = _currentTypeOfVehicle

    private val workManager = WorkManager.getInstance(application)

    // Cache all items form the database using LiveData.
    fun getAllVehiclesByTypeOrderedByName(typeOfVehicle: EnumTypeOfVehicle): LiveData<List<Vehicle>> =
        vehicleDao.getAllVehiclesByTypeOrderedByName(typeOfVehicle).asLiveData()

    init {
        getLogo()
    }

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
        typeOfVehicle: EnumTypeOfVehicle,
        km: Int
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
            km = km
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
        typeOfVehicle: EnumTypeOfVehicle,
        km: Int
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
            km = km
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
        km: Int
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
            km = km
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
        km: Int
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
            km = km
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
        km: String
    ): Boolean {
        if (plate.isBlank() || year.isBlank() || brand.isBlank() || model.isBlank() || line.isBlank() || km.isBlank()) {
            return false
        }
        return true
    }

    fun setCurrentTypeOfVehicle(enumTypeOfVehicle: EnumTypeOfVehicle) {
        _currentTypeOfVehicle.value = enumTypeOfVehicle
    }

    /**
     * Gets Logo information from the Vehicle API Retrofit service and updates the
     * [_logoDataApi] [List] [LiveData].
     */
    fun getLogo() {
        viewModelScope.launch {
            _statusLogApi.value = LogoApiStatus.LOADING
            try {
                _logoDataApi.value = LogoApi.retrofitService.getLogo()
                _statusLogApi.value = LogoApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _statusLogApi.value = LogoApiStatus.ERROR
                _logoDataApi.value = listOf()
            }
        }
    }

    /**
     * Gets VehicleInfo information from the Vehicle API Retrofit service and updates the
     * [_vehiclesInfo] [List] [LiveData].
     */
    fun getVehicleInfo() {
        viewModelScope.launch {
            _statusVehicleApi.value = VehicleApiStatus.LOADING
            try {
                _vehiclesDataApi.value = VehicleApi.retrofitService.getVehicleInfo()
                resetVehicleInfo()
                _statusVehicleApi.value = VehicleApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _statusVehicleApi.value = VehicleApiStatus.ERROR
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

    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        plate: String,
        model: String,
        km: Int
    ) {
        if (km >= 100000) {
            val dataComp = "$model ($plate)"
            val data = Data.Builder().putString(MyGarageReminderWorker.nameKey, dataComp).build()
            val mygarageBuilder = OneTimeWorkRequestBuilder<MyGarageReminderWorker>()
                .setInitialDelay(duration, unit)
                .setInputData(data)
                .build()

            workManager
                .enqueueUniqueWork(
                    model,
                    ExistingWorkPolicy.REPLACE,
                    mygarageBuilder
                )
        }
    }

}


/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class VehicleViewModelFactory(
    private val vehicleDao: VehicleDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return VehicleViewModel(vehicleDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
