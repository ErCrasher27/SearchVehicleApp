package com.example.searchvehicleapp.ui.vehicle.detailfragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.searchvehicleapp.R

class VehicleDetailFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleDetailFragment()
    }

    private lateinit var viewModel: VehicleDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VehicleDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}