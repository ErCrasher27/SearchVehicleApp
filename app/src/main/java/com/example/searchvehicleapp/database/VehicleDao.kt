package com.example.searchvehicleapp.database

import androidx.room.Dao
import androidx.room.Query
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicle WHERE type == :type ORDER BY model")
    fun getAllVehiclesByTypeOrderedByName(type: EnumTypeOfVehicle): Flow<List<Vehicle>>
}