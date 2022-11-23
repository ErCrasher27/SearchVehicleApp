package com.example.searchvehicleapp.ui.vehicle.addeditfragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentAddEditBinding
import com.example.searchvehicleapp.network.VehicleInfo
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.ui.vehicle.detailfragment.VehicleDetailFragmentArgs
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import com.google.android.material.textfield.TextInputLayout


class AddEditFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database.vehicleDao()
        )
    }

    lateinit var vehicle: Vehicle

    private var vehicleInfo: List<VehicleInfo>? = null

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
        vehicleViewModel.getVehicleInfo()
        vehicleInfo = vehicleViewModel.vehiclesInfo.value
        binding.previewImage.setOnClickListener { imageChooser() }
        val orderInputEditText: List<EditText> = listOf(
            binding.year, binding.brand, binding.model, binding.line
        )
        setInputEditTextEnableInOrderAndValidation(
            orderInputEditText = orderInputEditText, inputEditText = binding.year
        )
        setInputEditTextEnableInOrderAndValidation(
            orderInputEditText = orderInputEditText, inputEditText = binding.brand
        )
        setInputEditTextEnableInOrderAndValidation(
            orderInputEditText = orderInputEditText, inputEditText = binding.model
        )
        setInputEditTextEnableInOrderAndValidation(
            orderInputEditText = orderInputEditText, inputEditText = binding.line
        )

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
            plate = binding.plate.text.toString(),
            year = binding.year.text.toString(),
            brand = binding.brand.text.toString(),
            model = binding.model.text.toString(),
            line = binding.line.text.toString(),
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
            year.setText(vehicle.year.toString(), TextView.BufferType.SPANNABLE)
            line.setText(vehicle.line, TextView.BufferType.SPANNABLE)
            //line.setText()
            if (vehicle.image != null) {
                previewImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        BitmapFactory.decodeByteArray(
                            vehicle.image, 0, vehicle.image.size
                        ), 100, 100, false
                    )
                )
            } else {
                previewImage.setImageResource(R.drawable.ic_baseline_directions_car_24)
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
                year = binding.year.text.toString().toInt(),
                brand = binding.brand.text.toString(),
                model = binding.model.text.toString(),
                line = binding.line.text.toString(),
                image = checkIfInsertIsNull(createBitmapFromView(binding.previewImage)),
                typeOfVehicle = vehicleViewModel.currentTypeOfVehicle.value!!,
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
                plate = binding.plate.text.toString(),
                year = binding.year.text.toString().toInt(),
                brand = binding.brand.text.toString(),
                model = binding.model.text.toString(),
                line = binding.line.text.toString(),
                image = checkIfInsertIsNull(createBitmapFromView(binding.previewImage)),
                typeOfVehicle = vehicleViewModel.currentTypeOfVehicle.value!!,
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
    @Deprecated("Deprecated in Java")
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
                    binding.previewImage.tag = "is_not_null"
                }
            }
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }

    private fun checkIfInsertIsNull(image: Bitmap): Bitmap? {
        return if (binding.previewImage.tag == "is_not_null") {
            image
        } else {
            null
        }
    }

    private fun getEnumBySpinnerOfTypeOfFuel(typeOfFuel: String): EnumTypeOfFuel {
        return when (typeOfFuel) {
            "GAS" -> EnumTypeOfFuel.GAS
            "DIESEL" -> EnumTypeOfFuel.DIESEL
            "ELECTRIC" -> EnumTypeOfFuel.ELECTRIC
            else -> {
                EnumTypeOfFuel.GAS
            }
        }
    }

    private fun loadSpinner(spinner: Spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(), R.array.type_of_fuel, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
        }
    }

    private fun setSpinnerToValue(spinner: Spinner, value: String) {
        var index = 0
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == value) {
                index = i
                break // terminate loop
            }
        }
        spinner.setSelection(index)
    }

    private fun loadAutoCompleteView(
        autoCompleteTextView: AutoCompleteTextView,
    ) {
        val vehicleInfoField = vehicleViewModel.vehiclesInfo.value?.map {
            when (autoCompleteTextView.tag.toString()) {
                getString(R.string.year_tag) -> it.year
                getString(R.string.maker_tag) -> it.maker
                getString(R.string.model_tag) -> it.model
                getString(R.string.full_model_name_tag) -> it.fullModelName
                else -> {
                    null
                }
            }
        }

        val vehicleFiltered =
            vehicleInfoField?.filter { it!!.contains(autoCompleteTextView.text) }?.distinct()
        val arrayAdapter = ArrayAdapter(
            requireContext(), R.layout.dropdown_list_item, vehicleFiltered ?: listOf("loading...")
        )
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setInputEditTextEnableInOrderAndValidation(
        orderInputEditText: List<EditText>, inputEditText: AutoCompleteTextView
    ) {
        inputEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                loadAutoCompleteView(inputEditText)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (inputEditText.tag.toString() != getString(R.string.full_model_name_tag)) {
                    val index = orderInputEditText.indexOf(inputEditText)
                    val parent = orderInputEditText[index + 1].parent.parent as TextInputLayout
                    if (inputEditText.text?.isNotEmpty()!!) {
                        parent.isEnabled = true
                    } else {
                        parent.isEnabled = false
                        orderInputEditText[index + 1].text = null
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}