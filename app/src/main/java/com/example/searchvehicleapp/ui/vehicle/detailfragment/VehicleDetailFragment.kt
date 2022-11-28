package com.example.searchvehicleapp.ui.vehicle.detailfragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentVehicleDetailBinding
import com.example.searchvehicleapp.network.logosapi.Logo
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.utils.AddOrEdit.EDIT
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.example.searchvehicleapp.utils.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFloatingActionButtons()
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
            plate.text = vehicle.plate
            brand.text = vehicle.brand
            model.text = vehicle.model
            year.text =
                getString(R.string.year_format, vehicle.year.toString())
            fuelLogo.setImageResource(getIconFromTypeOfFuel(vehicle.typeOfFuel))
            fuelType.text = vehicle.typeOfFuel.name
            line.text = vehicle.line
            if (vehicle.image != null) {
                val bmp = BitmapFactory.decodeByteArray(vehicle.image, 0, vehicle.image.size)
                image.setImageBitmap(bmp)
            } else {
                image.setImageResource(R.drawable.ic_baseline_directions_car_24)
            }
            setAndGetUriByBrandParsingListOfLogoAndImageView(
                vehicleViewModel.logoDataApi.value,
                vehicle.brand,
                binding.brandLogo
            )
        }
    }

    /**
     * private fun goToEdit()
     */
    private fun goToEdit() {
        val action =
            VehicleDetailFragmentDirections.actionVehicleDetailFragmentToAddEditFragment(
                EDIT,
                navigationArgs.vehicleId
            )
        findNavController().navigate(action)
    }

    /**
     * private fun goToListAfterDelete()
     */
    private fun goToListAfterDelete() {
        vehicleViewModel.deleteVehicle(vehicle)
        val action =
            VehicleDetailFragmentDirections.actionVehicleDetailFragmentToViewPagerFragment()
        findNavController().navigate(action)
    }

    /**
     * private fun getIconFromTypeOfFuel(typeOfFuel: EnumTypeOfFuel): Int
     */
    private fun getIconFromTypeOfFuel(typeOfFuel: EnumTypeOfFuel): Int {
        return when (typeOfFuel) {
            EnumTypeOfFuel.ELECTRIC -> R.drawable.ic_baseline_electric_bolt_24
            else -> R.drawable.ic_baseline_local_gas_station_24
        }
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

    /**
     * private fun setFloatingActionButtons()
     */
    private fun setFloatingActionButtons() {
        val actionFab = binding.fabAction
        val editFab = binding.fabEdit
        val deleteFab = binding.fabDelete
        val editFabText = binding.fabEditText
        val deleteFabText = binding.fabDeleteText

        editFab.visibility = View.GONE
        deleteFab.visibility = View.GONE
        editFabText.visibility = View.GONE
        deleteFabText.visibility = View.GONE

        var allFabsVisibility = false

        actionFab.setOnClickListener {
            allFabsVisibility = if (!allFabsVisibility) {
                editFab.show()
                deleteFab.show()
                editFabText.visibility = View.VISIBLE
                deleteFabText.visibility = View.VISIBLE
                actionFab.setImageResource(R.drawable.ic_baseline_car_repair_24)
                true
            } else {
                editFab.hide()
                deleteFab.hide()
                editFabText.visibility = View.GONE
                deleteFabText.visibility = View.GONE
                actionFab.setImageResource(R.drawable.ic_baseline_directions_car_24)
                false
            }
        }
        editFab.setOnClickListener { goToEdit() }
        deleteFab.setOnClickListener { showConfirmationDialog() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}