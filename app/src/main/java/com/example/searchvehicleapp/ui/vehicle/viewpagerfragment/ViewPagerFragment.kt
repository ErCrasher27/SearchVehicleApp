package com.example.searchvehicleapp.ui.vehicle.viewpagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.databinding.FragmentViewPagerBinding
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleListFragment
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleViewModelFactory
import com.example.searchvehicleapp.utils.AddOrEdit
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            (activity?.application as VehicleApplication).database.vehicleDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(
            sectionsPagerAdapter = SectionsPagerAdapter(context, parentFragmentManager),
            viewPager = binding.container
        )

        // Set FloatingActionButton
        binding.fab.setOnClickListener {
            setCurrentTabOpened()
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToAddEditFragment(
                AddOrEdit.ADD
            )
            this.findNavController().navigate(action)
        }
    }

    private fun setCurrentTabOpened() {
        when (binding.tabs.selectedTabPosition) {
            0 -> vehicleViewModel.setCurrentTypeOfVehicle(
                enumTypeOfVehicle = EnumTypeOfVehicle.MOTORCYCLE
            )
            1 -> vehicleViewModel.setCurrentTypeOfVehicle(
                enumTypeOfVehicle = EnumTypeOfVehicle.CAR
            )
            2 -> vehicleViewModel.setCurrentTypeOfVehicle(
                enumTypeOfVehicle = EnumTypeOfVehicle.TRUCK
            )
        }
    }

    /**
     * Responsible for adding the 3 tabs: Motorcycle, Car, Truck
     */
    private fun setupViewPager(sectionsPagerAdapter: SectionsPagerAdapter, viewPager: ViewPager) {
        //index 0
        sectionsPagerAdapter.addFragment(
            VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.MOTORCYCLE,
                onVehicleClicked = {
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToVehicleDetailFragment(
                            it.id
                        )
                    findNavController().navigate(action)
                })
        )
        //index 1
        sectionsPagerAdapter.addFragment(
            VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.CAR, onVehicleClicked = {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToVehicleDetailFragment(
                        it.id
                    )
                findNavController().navigate(action)
            })
        )
        //index 2
        sectionsPagerAdapter.addFragment(
            VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.TRUCK, onVehicleClicked = {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToVehicleDetailFragment(
                        it.id
                    )
                findNavController().navigate(action)
            })
        )
        viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(viewPager)
        binding.tabs.getTabAt(0)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_sports_motorsports_24)
        binding.tabs.getTabAt(1)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_directions_car_24)
        binding.tabs.getTabAt(2)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_directions_bus_24)
        binding.tabs.getTabAt(0)?.select()
        binding.tabs.getTabAt(1)?.select()
        binding.tabs.getTabAt(2)?.select()

    }
}