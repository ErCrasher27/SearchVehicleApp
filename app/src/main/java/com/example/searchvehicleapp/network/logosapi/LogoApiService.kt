package com.example.searchvehicleapp.network.logosapi

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL =
    "https://raw.githubusercontent.com/ErCrasher27/carl-maker-logos.json/main/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
var gson: Gson = GsonBuilder()
    .setLenient()
    .create()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL)
        .build()

/**
 * A public interface that exposes the [getLogo] method
 */
interface LogoApiService {
    /**
     * Returns a [List] of [Logo] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "year" endpoint will be requested with the GET
     * HTTP method
     */

    @Headers("Content-Type: application/json")
    @GET("carl-maker-logos.json")
    suspend fun getLogo(): List<Logo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object LogoApi {
    val retrofitService: LogoApiService by lazy { retrofit.create(LogoApiService::class.java) }
}
