package com.example.searchvehicleapp.database

import androidx.room.Query
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import kotlinx.coroutines.flow.Flow

interface VehicleDao {

    @Query(
        "SELECT * " +
                "FROM vehicle " +
                "INNER JOIN type_of_vehicle " +
                "ON vehicle.id_type == type_of_vehicle.id " +
                "WHERE type == :type " +
                "ORDER BY vehicle.name"
    )
    fun getAllVehiclesByType(type: EnumTypeOfVehicle): Flow<List<Vehicle>>

}