package com.example.searchvehicleapp.utils

import android.widget.ImageView
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.network.logosapi.Logo
import com.squareup.picasso.Picasso

/**
 * fun setAndGetUriByBrandParsingListOfLogoAndImageView(logoDataApi: List<Logo>?,brand: String,logo: ImageView)
 */
fun setAndGetUriByBrandParsingListOfLogoAndImageView(
    brand: String,
    logoData: List<Logo>?,
    logoView: ImageView
) {
    val logoDataApiMap = logoData?.associate { it.name.lowercase() to it.logo }
    if (logoDataApiMap?.get(brand.lowercase()) != null) {
        Picasso.get().load(logoDataApiMap[brand.lowercase()]).into(logoView)
    } else {
        logoView.setImageResource(R.drawable.ic_baseline_scatter_plot_24)
    }
}