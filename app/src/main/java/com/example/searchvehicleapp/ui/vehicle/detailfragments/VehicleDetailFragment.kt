package com.example.searchvehicleapp.ui.vehicle.detailfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentVehicleDetailBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.utils.AddOrEdit.EDIT

class VehicleDetailFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database.vehicleDao()
        )
    }

    lateinit var vehicle: Vehicle

    private val navigationArgs: VehicleDetailFragmentArgs by navArgs()

    private var _binding: FragmentVehicleDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleDetailBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }


        binding.fabEdit.setOnClickListener { goToEdit() }
        binding.fabDelete.setOnClickListener { goToListAfterDelete() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.vehicleId

        // Retrieve the item details using the vehicleId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        vehicleViewModel.getVehicleById(id).observe(this.viewLifecycleOwner) { vehicleSelected ->
            vehicle = vehicleSelected
            bind(vehicle)
        }
    }

    /**
     * Binds views with the passed in item data.
     */
    private fun bind(vehicle: Vehicle) {
        binding.apply {
            model.text = vehicle.model
            brand.text = vehicle.brand
        }
    }

    private fun goToEdit() {
        val action =
            VehicleDetailFragmentDirections.actionVehicleDetailFragmentToAddEditFragment(
                EDIT,
                navigationArgs.vehicleId
            )
        findNavController().navigate(action)
    }

    private fun goToListAfterDelete() {
        vehicleViewModel.deleteVehicle(vehicle)
        val action =
            VehicleDetailFragmentDirections.actionVehicleDetailFragmentToViewPagerFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}