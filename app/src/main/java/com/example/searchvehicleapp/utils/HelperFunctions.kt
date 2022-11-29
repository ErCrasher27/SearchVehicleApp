package com.example.searchvehicleapp.utils

import android.widget.ImageView
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.network.logosapi.Logo
import com.squareup.picasso.Picasso

/**
 * fun setAndGetUriByBrandParsingListOfLogoAndImageView(logoDataApi: List<Logo>?,brand: String,logo: ImageView)
 */
fun setAndGetUriByBrandParsingListOfLogoAndImageView(
    logoDataApi: List<Logo>?,
    brand: String,
    logo: ImageView
) {
    val logoDataApiMap = logoDataApi?.associate { it.name.lowercase() to it.logo }
    if (logoDataApiMap?.get(brand.lowercase()) != null) {
        Picasso.get().load(logoDataApiMap[brand.lowercase()]).into(logo)
    } else {
        logo.setImageResource(R.drawable.ic_baseline_scatter_plot_24)
    }
}