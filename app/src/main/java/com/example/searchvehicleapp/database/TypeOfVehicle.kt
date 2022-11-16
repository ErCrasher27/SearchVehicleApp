package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle

@Entity(tableName = "type_of_vehicle")
data class TypeOfVehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "type") val type: Enum<EnumTypeOfVehicle>
)
