package com.example.searchvehicleapp.ui.vehicle.addeditfragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.database.Vehicle
import com.example.searchvehicleapp.databinding.FragmentAddEditBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.ui.vehicle.detailfragment.VehicleDetailFragmentArgs
import com.example.searchvehicleapp.utils.EnumTypeOfFuel
import java.io.ByteArrayOutputStream


class AddEditFragment : Fragment() {

    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database.vehicleDao()
        )
    }

    lateinit var vehicle: Vehicle

    private val vehicleDetailNavigationArgs: VehicleDetailFragmentArgs by navArgs()

    private var vehicleInfoField: MutableList<String>? = null

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
        vehicleViewModel.resetVehicleInfo()
        loadTypeOfFuel(binding.typeOfFuel)
        binding.previewImage.setOnClickListener { imageChooser() }
        setInputEditText(binding.year)
        setInputEditText(binding.brand)
        setInputEditText(binding.model)
        setInputEditText(binding.line)

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
            typeOfFuel.setText(vehicle.typeOfFuel.name, TextView.BufferType.SPANNABLE)
            loadTypeOfFuel(binding.typeOfFuel)
            if (vehicle.image != null) {
                val bmp = BitmapFactory.decodeByteArray(vehicle.image, 0, vehicle.image.size)
                previewImage.setImageBitmap(bmp)

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
                typeOfFuel = getEnumByAutoCompleteViewOfTypeOfFuel(binding.typeOfFuel.text.toString()),
                image = checkIfInsertIsNull(createBitmapFromView(binding.previewImage)?.toByteArray()),
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
                typeOfFuel = getEnumByAutoCompleteViewOfTypeOfFuel(binding.typeOfFuel.text.toString()),
                image = checkIfInsertIsNull(createBitmapFromView(binding.previewImage)?.toByteArray()),
                typeOfVehicle = vehicleViewModel.currentTypeOfVehicle.value!!,
            )
            val action = AddEditFragmentDirections.actionAddEditFragmentToVehicleDetailFragment(
                vehicleDetailNavigationArgs.vehicleId
            )
            findNavController().navigate(action)
        }
    }

    /**
     * This function is triggered when the Select Image Button is clicked
     */
    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200)
    }

    /**
     * This function is triggered when user selects the image from the imageChooser
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                val selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    binding.previewImage.setImageURI(selectedImageUri)
                    binding.previewImage.tag = "is_not_null"
                }
            }
        }
    }

    /**
     * private fun createBitmapFromView(view: View): Bitmap
     */
    private fun createBitmapFromView(view: View): Bitmap? {
        view.buildDrawingCache()
        return view.drawingCache
    }

    /**
     * private fun Bitmap.toByteArray(): ByteArray
     */
    private fun Bitmap.toByteArray(): ByteArray {
        val stream =
            ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    /**
     * private fun checkIfInsertIsNull(image: Bitmap): Bitmap?
     */
    private fun checkIfInsertIsNull(image: ByteArray?): ByteArray? {
        return if (binding.previewImage.tag == "is_not_null") {
            image
        } else {
            null
        }
    }


    /**
     * private fun loadTypeOfFuel(typeOfFuel: AutoCompleteTextView)
     */
    private fun loadTypeOfFuel(typeOfFuel: AutoCompleteTextView) {
        typeOfFuel.setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.dropdown_list_item, EnumTypeOfFuel.values()
            )
        )
    }

    /**
     * private fun getEnumByAutoCompleteViewOfTypeOfFuel(typeOfFuel: String): EnumTypeOfFuel
     */
    private fun getEnumByAutoCompleteViewOfTypeOfFuel(typeOfFuel: String): EnumTypeOfFuel {
        return when (typeOfFuel) {
            "GASOLINE" -> EnumTypeOfFuel.GASOLINE
            "DIESEL" -> EnumTypeOfFuel.DIESEL
            "GAS" -> EnumTypeOfFuel.GAS
            "BEV" -> EnumTypeOfFuel.BEV
            "HEV" -> EnumTypeOfFuel.HEV
            "MHEV" -> EnumTypeOfFuel.MHEV
            else -> EnumTypeOfFuel.PHEV
        }
    }

    /**
     * private fun setInputEditText(inputEditText: AutoCompleteTextView)
     */
    private fun setInputEditText(
        inputEditText: AutoCompleteTextView
    ) {
        inputEditText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            vehicleInfoField?.clear()
            when (inputEditText.tag.toString()) {
                getString(R.string.year_tag) -> {
                    vehicleInfoField =
                        vehicleViewModel.vehiclesInfo.value?.map { it.year } as MutableList<String>?
                }
                getString(R.string.maker_tag) -> {
                    vehicleViewModel.setVehicleInfoFilteredForYear(binding.year.text.toString())
                    vehicleInfoField =
                        vehicleViewModel.vehiclesInfo.value?.map { it.maker } as MutableList<String>?
                }
                getString(R.string.model_tag) -> {
                    vehicleViewModel.setVehicleInfoFilteredForBrand(binding.brand.text.toString())
                    vehicleInfoField =
                        vehicleViewModel.vehiclesInfo.value?.map { it.model } as MutableList<String>?
                }
                else -> {
                    vehicleViewModel.setVehicleInfoFilteredForModel(binding.model.text.toString())
                    vehicleInfoField =
                        vehicleViewModel.vehiclesInfo.value?.map { it.fullModelName } as MutableList<String>?
                }
            }
        }
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                loadAutoCompleteView(inputEditText)
            }
        })
    }

    /**
     * private fun loadAutoCompleteView(autoCompleteTextView: AutoCompleteTextView,)
     */
    private fun loadAutoCompleteView(
        autoCompleteTextView: AutoCompleteTextView,
    ) {
        if (autoCompleteTextView.text.isNotBlank()) {
            val vehicleInfoFieldFiltered = vehicleInfoField?.filter {
                it.contains(
                    autoCompleteTextView.text, ignoreCase = true
                )
            }?.distinct()?.sorted()

            val arrayAdapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_list_item,
                vehicleInfoFieldFiltered ?: listOf("loading...")
            )
            autoCompleteTextView.setAdapter(arrayAdapter)
        } else {
            autoCompleteTextView.setAdapter(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}