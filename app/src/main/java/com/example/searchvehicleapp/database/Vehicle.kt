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

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,

    @ColumnInfo(name = "model") val model: String,

    //@ColumnInfo(name = "logo_brand", typeAffinity = ColumnInfo.BLOB) val logoBrand: ByteArray,

    @ColumnInfo(name = "brand") val brand: String,

    @ColumnInfo(name = "year") val year: Int,

    //@ColumnInfo(name = "logo_fuel", typeAffinity = ColumnInfo.BLOB) val logoFuel: ByteArray,

    //@ColumnInfo(name = "type_fuel") val typeFuel: String,

    @ColumnInfo(name = "kw") val kW: Int,

    @ColumnInfo(name = "cv") val cV: Int,

    //pattern example: 1.9 TDI Stylance
    @ColumnInfo(name = "line") val line: String,

    @ColumnInfo(name = "type_of_vehicle") val typeOfVehicle: EnumTypeOfVehicle,

    @ColumnInfo(name = "type_of_fuel") val typeOfFuel: EnumTypeOfFuel

)