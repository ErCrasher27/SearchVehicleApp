package com.example.searchvehicleapp.utils

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import coil.load
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.network.logosapi.Logo
import com.example.searchvehicleapp.ui.vehicle.LogoApiStatus
import com.squareup.picasso.Picasso

/**
 * fun setAndGetUriByBrandParsingListOfLogoAndImageView(logoDataApi: List<Logo>?,brand: String,logo: ImageView)
 */
fun setAndGetUriByBrandParsingListOfLogoAndImageView(
    brand: String,
    logoData: LiveData<List<Logo>>,
    logoView: ImageView,
    statusData: LogoApiStatus?,
    statusView: ImageView,
) {
    val logoDataApiMap = logoData.value?.associate { it.name.lowercase() to it.logo }
    Picasso.get()
        .load(logoDataApiMap?.get(brand.lowercase()))
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_broken_image)
}

fun bindImage(
    logoData: String?,
    logoView: ImageView,
    statusData: LogoApiStatus?,
    statusView: ImageView
) {
    bindStatus(statusData = statusData, statusView = statusView)
    logoData?.let {
        val imgUri = logoData.toUri().buildUpon().scheme("https").build()
        logoView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

fun bindStatus(statusData: LogoApiStatus?, statusView: ImageView) {
    when (statusData) {
        LogoApiStatus.LOADING -> {
            statusView.visibility = View.VISIBLE
            statusView.setImageResource(R.drawable.loading_animation)
        }
        LogoApiStatus.ERROR -> {
            statusView.visibility = View.VISIBLE
            statusView.setImageResource(R.drawable.ic_connection_error)
        }
        else -> {
            statusView.visibility = View.GONE
        }
    }
}