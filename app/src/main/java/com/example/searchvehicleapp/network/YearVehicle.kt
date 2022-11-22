package com.example.searchvehicleapp.network

import com.squareup.moshi.Json

data class YearVehicle(
    @Json(name = "data") val year: String,
    @Json(name = "message") val message: String
)
