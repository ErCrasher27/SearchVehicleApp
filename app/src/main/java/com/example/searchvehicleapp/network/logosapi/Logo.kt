package com.example.searchvehicleapp.network.logosapi

import com.google.gson.annotations.SerializedName

data class Logo(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
)
