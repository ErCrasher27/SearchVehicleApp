package com.example.searchvehicleapp.ui.vehicle.detailfragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentVehicleDetailBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.utils.AddOrEdit.EDIT
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        binding.fabDelete.setOnClickListener { showConfirmationDialog() }

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
            //TODO DETAILS AND LEAVE CASE
            model.text = vehicle.model.uppercase()
            brand.text = vehicle.brand.lowercase()
            plate.text = vehicle.plate.uppercase()
            year.text = getString(
                R.string.year_format, vehicle.year.toString()
            )
            /*cV.text = getString(
                R.string.cv_format, vehiclecV.toString()
            )
            kW.text = getString(
                R.string.kw_format, vehicle.kW.toString()
            )
            line.text = vehicle.line.uppercase()*/
            //fuelLogo.setImageResource(getIconFromTypeOfFuel(vehicle.typeOfFuel))

            if (vehicle.image != null) {
                image.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            vehicle.image, 0, vehicle.image.size
                        ), 200, 200, false
                    )
                )
            } else {
                image.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }
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

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the vehicle.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                goToListAfterDelete()
            }
            .show()
    }

    private fun getIconFromTypeOfFuel(typeOfFuel: EnumTypeOfFuel): Int {
        return when (typeOfFuel) {
            EnumTypeOfFuel.ELECTRIC -> R.drawable.ic_baseline_electric_bolt_24
            else -> R.drawable.ic_baseline_local_gas_station_24
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}