package com.example.searchvehicleapp.database

import androidx.room.Query

interface VehicleDao {

    @Query("SELECT * from vehicle WHERE type")
}