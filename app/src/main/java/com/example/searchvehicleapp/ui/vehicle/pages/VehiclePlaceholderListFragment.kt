package com.example.searchvehicleapp.ui.vehicle.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.searchvehicleapp.databinding.FragmentVehiclePlaceholderBinding


/**
 * A placeholder fragment containing a simple view.
 */
class VehiclePlaceholderListFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    private var _binding: FragmentVehiclePlaceholderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehiclePlaceholderBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vehiclePlaceHolderFragment = this@VehiclePlaceholderListFragment
        }

        pageViewModel.text.observe(viewLifecycleOwner) {
            binding.sectionLabel.text = it
        }

        return binding.root
    }


    private fun loadFragment(fragment: Fragment) {
        // create a FragmentManager
        val fm: FragmentManager? = fragmentManager
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction()
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(binding.fragmentPlaceholder.id, fragment)
        fragmentTransaction.commit() // save the changes
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): VehiclePlaceholderListFragment {
            return VehiclePlaceholderListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}