package com.example.searchvehicleapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

//----------------------------------------------- MOVE IT DOWN -----------------------------------------------
private const val AUTHORIZATION_KEY = "MDU0ZGM4MTUtYjI2ZC00NjQ0LThmYTktN2ZiN2EwMTE4Njg0"
private const val PARTNER_TOKEN = "15c442a807a54ad3a1a08a11777163b7"
//----------------------------------------------- MOVE IT UP -----------------------------------------------

private const val BASE_URL = "http://api.carmd.com/v3.0/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL)
        .build()


/**
 * A public interface that exposes the [getYearVehicle] method
 */
interface CarMDApiService {
    /**
     * Returns a [List] of [YearVehicle] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "year" endpoint will be requested with the GET
     * HTTP method
     */
    @Headers(
        "content-type : application/json",
        "authorization : $AUTHORIZATION_KEY",
        "partner-token : $PARTNER_TOKEN"
    )
    @GET("year")
    suspend fun getYearVehicle(): List<YearVehicle>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object CarMDApi {
    val retrofitService: CarMDApiService by lazy { retrofit.create(CarMDApiService::class.java) }
}
