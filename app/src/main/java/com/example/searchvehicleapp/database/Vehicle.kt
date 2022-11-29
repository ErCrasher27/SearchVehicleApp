package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle


@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "plate") val plate: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "line") val line: String,
    @ColumnInfo(name = "type_of_fuel") val typeOfFuel: EnumTypeOfFuel,
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,
    @ColumnInfo(name = "type_of_vehicle") val typeOfVehicle: EnumTypeOfVehicle,
    @ColumnInfo(name = "km") val km: Int,
)