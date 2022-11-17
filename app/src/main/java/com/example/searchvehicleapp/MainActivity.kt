package com.example.searchvehicleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import com.example.searchvehicleapp.databinding.ActivityMainBinding
import com.example.searchvehicleapp.ui.vehicle.listfragment.VehicleListFragment
import com.example.searchvehicleapp.ui.vehicle.pageradapter.SectionsPagerAdapter
import com.example.searchvehicleapp.utils.EnumTypeOfVehicle
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the action bar for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)

        //Set SectionsPagerAdapter with ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.container
        setupViewPager(sectionsPagerAdapter, viewPager)


        // Set FloatingActionButton
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    /**
     * Responsible for adding the 3 tabs: Motorcycle, Car, Truck
     */
    private fun setupViewPager(sectionsPagerAdapter: SectionsPagerAdapter, viewPager: ViewPager) {
        sectionsPagerAdapter.addFragment(VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.MOTORCYCLE)) //index 0
        sectionsPagerAdapter.addFragment(VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.CAR)) //index 1
        sectionsPagerAdapter.addFragment(VehicleListFragment(enumTypeOfVehicle = EnumTypeOfVehicle.TRUCK)) //index 2
        viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(viewPager)
        binding.tabs.getTabAt(0)?.icon = getDrawable(R.drawable.ic_baseline_sports_motorsports_24)
        binding.tabs.getTabAt(1)?.icon = getDrawable(R.drawable.ic_baseline_directions_car_24)
        binding.tabs.getTabAt(2)?.icon = getDrawable(R.drawable.ic_baseline_directions_bus_24)
    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}