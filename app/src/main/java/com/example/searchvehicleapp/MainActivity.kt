package com.example.searchvehicleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.viewpager.widget.ViewPager
import com.example.searchvehicleapp.databinding.ActivityMainBinding
import com.example.searchvehicleapp.ui.vehicle.pages.SectionsPagerAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        binding.tabs.setupWithViewPager(viewPager)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        /*  // Retrieve NavController from the NavHostFragment
          val navHostFragment = supportFragmentManager
              .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
          navController = navHostFragment.navController
          // Set up the action bar for use with the NavController
          NavigationUI.setupActionBarWithNavController(this, navController)*/

    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}