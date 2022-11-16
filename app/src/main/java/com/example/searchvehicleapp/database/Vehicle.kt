package com.example.searchvehicleapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicle",
    foreignKeys = [
        ForeignKey(
            entity = TypeOfVehicle::class,
            parentColumns = ["type"],
            childColumns = ["id_type"],
            onDelete = CASCADE
        )]
)
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "id_type")
    val id_type: Int
)