package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle


@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "plate") val plate: String,

    //@ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,

    @ColumnInfo(name = "model") val model: String,

    //@ColumnInfo(name = "logo_brand", typeAffinity = ColumnInfo.BLOB) val logoBrand: ByteArray,

    @ColumnInfo(name = "brand") val brand: String,

    //@ColumnInfo(name = "year") val year: Int,

    //@ColumnInfo(name = "logo_fuel", typeAffinity = ColumnInfo.BLOB) val logoFuel: ByteArray,

    //@ColumnInfo(name = "type_fuel") val typeFuel: String,

    //@ColumnInfo(name = "kw") val kW: Int,

    //@ColumnInfo(name = "cv") val cV: Int,

    @ColumnInfo(name = "type") val type: EnumTypeOfVehicle
)