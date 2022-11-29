package com.example.searchvehicleapp.ui.vehicle.viewpagerfragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.searchvehicleapp.R
import com.example.searchvehicleapp.application.VehicleApplication
import com.example.searchvehicleapp.databinding.FragmentViewPagerBinding
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModel
import com.example.searchvehicleapp.ui.vehicle.VehicleViewModelFactory
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleListFragment
import com.example.searchvehicleapp.utils.AddOrEdit
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val vehicleViewModel: VehicleViewModel by activityViewModels {
        VehicleViewModelFactory(
            vehicleDao = (activity?.application as VehicleApplication).database.vehicleDao(),
            application = requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(
            sectionsPagerAdapter = SectionsPagerAdapter(context, childFragmentManager),
            viewPager = binding.container
        )

        listenerViewPagerAndTabs()

        // Set FloatingActionButton
        binding.fabAdd.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToAddEditFragment(
                AddOrEdit.ADD, 0
            )
            this.findNavController().navigate(action)
        }
    }

    /**
     * Responsible for adding the 3 tabs: Motorcycle, Car, Truck
     */
    private fun setupViewPager(sectionsPagerAdapter: SectionsPagerAdapter, viewPager: ViewPager) {
        //index 0
        sectionsPagerAdapter.addFragment(
            VehicleListFragment(
                enumTypeOfVehicle = EnumTypeOfVehicle.MOTORCYCLE,
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
        setupTabIcons(binding.tabs)

        //if values are not null, the tab selected will be as last shown
        if (vehicleViewModel.currentTypeOfVehicle.value == null) {
            //if null, set to motorcycle (first page)
            vehicleViewModel.setCurrentTypeOfVehicle(EnumTypeOfVehicle.MOTORCYCLE)
            binding.tabs.getTabAt(0)
        } else {
            binding.tabs.getTabAt(getIndexByEnumVehicleType(vehicleViewModel.currentTypeOfVehicle.value!!))
                ?.select()
        }
    }

    /**
     * Responsible for retrieve methods on change tab selected
     */
    private fun listenerViewPagerAndTabs() {
        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.container.currentItem = tab.position
                setCurrentTabOpened()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    /**
     * Responsible for setting enumTypeOfVehicle on [vehicleViewModel] instance
     */
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
     * Responsible for setting icon of tabs with colors appropriate
     */
    private fun setupTabIcons(tabs: TabLayout) {
        tabs.getTabAt(0)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_sports_motorsports_24)
        tabs.getTabAt(1)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_directions_car_24)
        tabs.getTabAt(2)?.icon =
            resources.getDrawable(com.example.searchvehicleapp.R.drawable.ic_baseline_directions_bus_24)
        tabs.getTabAt(0)?.icon?.setColorFilter(
            resources.getColor(R.color.secondaryColor), PorterDuff.Mode.SRC_IN
        )
        tabs.getTabAt(1)?.icon?.setColorFilter(
            resources.getColor(R.color.primaryColor), PorterDuff.Mode.SRC_IN
        )
        tabs.getTabAt(2)?.icon?.setColorFilter(
            resources.getColor(R.color.primaryColor), PorterDuff.Mode.SRC_IN
        )
        tabs.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon!!.setColorFilter(
                    resources.getColor(R.color.secondaryColor), PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon!!.setColorFilter(
                    resources.getColor(R.color.primaryColor), PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    /**
     * private fun getIndexByEnumVehicleType(enumTypeOfVehicle: EnumTypeOfVehicle): Int
     */
    private fun getIndexByEnumVehicleType(enumTypeOfVehicle: EnumTypeOfVehicle): Int {
        return when (enumTypeOfVehicle) {
            EnumTypeOfVehicle.MOTORCYCLE -> 0
            EnumTypeOfVehicle.CAR -> 1
            EnumTypeOfVehicle.TRUCK -> 2
        }
    }
}