package com.example.searchvehicleapp.ui.vehicle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.databinding.FragmentVehicleListBinding
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleListAdapter
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleViewModelFactory

/**
 * A placeholder fragment containing a simple view.
 */
class VehicleListFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val pageVehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database
                .vehicleDao()
        )
    }

    private var _binding: FragmentVehicleListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleListBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vehicleListFragment = this@VehicleListFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = VehicleListAdapter {
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        /*pageVehicleViewModel.getAllVehiclesByType(EnumTypeOfVehicle.CAR)
            .observe(this.viewLifecycleOwner) { vehicles ->
                vehicles.let {
                    adapter.submitList(it)
                }
            }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}