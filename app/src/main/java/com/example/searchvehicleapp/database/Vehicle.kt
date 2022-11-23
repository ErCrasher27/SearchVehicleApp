package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle


@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "plate") val plate: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "line") val line: String,
    //LINE FROM JSON
    /*@ColumnInfo(name = "displacement") val displacement: String,
    @ColumnInfo(name = "type_of_fuel") val typeOfFuel: EnumTypeOfFuel,
    @ColumnInfo(name = "forward_gear_ratios") val forwardGearRatios: Int,
    @ColumnInfo(name = "type_of_transmission") val typeOfTransmission: EnumTypeOfTransmission,
    @ColumnInfo(name = "cv") val cv: Int,*/
    // END HERE
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,
    @ColumnInfo(name = "type_of_vehicle") val typeOfVehicle: EnumTypeOfVehicle,
)