package com.example.searchvehicleapp.database

import androidx.room.*
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicle WHERE type = :type ORDER BY model")
    fun getAllVehiclesByTypeOrderedByName(type: EnumTypeOfVehicle): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicle WHERE id = :id")
    fun getVehicleById(id: Int): Flow<Vehicle>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vehicle: Vehicle)

    @Update
    suspend fun update(vehicle: Vehicle)

    @Delete
    suspend fun delete(vehicle: Vehicle)
}