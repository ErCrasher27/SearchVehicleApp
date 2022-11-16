package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle


@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: EnumTypeOfVehicle
)