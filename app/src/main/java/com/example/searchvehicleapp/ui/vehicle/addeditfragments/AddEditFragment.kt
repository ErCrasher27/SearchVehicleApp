package com.example.searchvehicleapp.ui.vehicle.addeditfragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentAddEditBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.ui.vehicle.detailfragments.VehicleDetailFragmentArgs


class AddEditFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database.vehicleDao()
        )
    }

    lateinit var vehicle: Vehicle

    private val vehicleDetailNavigationArgs: VehicleDetailFragmentArgs by navArgs()

    private var _binding: FragmentAddEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.importImage.setOnClickListener { imageChooser() }
        val id = vehicleDetailNavigationArgs.vehicleId
        if (id > 0) {
            vehicleViewModel.getVehicleById(id)
                .observe(this.viewLifecycleOwner) { vehicleSelected ->
                    vehicle = vehicleSelected
                    bindForUpdate(vehicle)
                }
        } else {
            binding.fabSave.setOnClickListener {
                addNewItem()
            }
        }
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return vehicleViewModel.isEntryValid(
            binding.plate.text.toString(),
            binding.brand.text.toString(),
            binding.model.text.toString(),
            binding.year.text.toString()
        )
    }

    /**
     * Binds views with the passed in [Vehicle] information.
     */
    private fun bindForUpdate(vehicle: Vehicle) {
        binding.apply {
            plate.setText(vehicle.plate, TextView.BufferType.SPANNABLE)
            brand.setText(vehicle.brand, TextView.BufferType.SPANNABLE)
            model.setText(vehicle.model, TextView.BufferType.SPANNABLE)
            if (vehicle.image != null) {
                binding.previewImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            vehicle.image, 0, vehicle.image.size
                        ), binding.previewImage.width, binding.previewImage.height, false
                    )
                )
            } else {
                binding.previewImage.setImageResource(com.example.searchvehicleapp.R.drawable.ic_baseline_directions_car_24)
            }
            fabSave.setOnClickListener { updateItem() }
        }
    }

    /**
     * Inserts the new vehicle into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            vehicleViewModel.addNewVehicle(
                plate = binding.plate.text.toString(),
                brand = binding.brand.text.toString(),
                model = binding.model.text.toString(),
                typeOfVehicle = vehicleViewModel.currentTypeOfVehicle.value!!,
                year = binding.year.text.toString().toInt(),
                image = createBitmapFromView(binding.previewImage)
            )
            val action = AddEditFragmentDirections.actionAddEditFragmentToViewPagerFragment()
            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing vehicle in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            vehicleViewModel.updateVehicle(
                id = this.vehicleDetailNavigationArgs.vehicleId,
                plate = this.binding.plate.text.toString(),
                brand = this.binding.brand.text.toString(),
                model = this.binding.model.text.toString(),
                typeOfVehicle = vehicleViewModel.currentTypeOfVehicle.value!!,
                year = binding.year.text.toString().toInt(),
                image = createBitmapFromView(binding.previewImage)
            )
            val action = AddEditFragmentDirections.actionAddEditFragmentToVehicleDetailFragment(
                vehicleDetailNavigationArgs.vehicleId
            )
            findNavController().navigate(action)
        }
    }

    // this function is triggered when
    // the Select Image Button is clicked
    private fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                val selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    binding.previewImage.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}