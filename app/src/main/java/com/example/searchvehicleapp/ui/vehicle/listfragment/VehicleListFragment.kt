package com.example.searchvehicleapp.ui.vehicle.listfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentVehicleListBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle

/**
 * A placeholder fragment containing a simple view.
 */
class VehicleListFragment(
    private val enumTypeOfVehicle: EnumTypeOfVehicle,
    private val onVehicleClicked: (Vehicle) -> Unit
) : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            vehicleDao = (activity?.application as VehicleApplication).database.vehicleDao(),
            application = requireActivity().application
        )
    }

    private var _binding: FragmentVehicleListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleListBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VehicleListAdapter(
            onVehicleClicked = onVehicleClicked,
            logoDataApi = vehicleViewModel.logoDataApi
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        vehicleViewModel.getAllVehiclesByTypeOrderedByName(enumTypeOfVehicle)
            .observe(this.viewLifecycleOwner)
            { vehicles ->
                vehicles.let {
                    adapter.submitList(it)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}