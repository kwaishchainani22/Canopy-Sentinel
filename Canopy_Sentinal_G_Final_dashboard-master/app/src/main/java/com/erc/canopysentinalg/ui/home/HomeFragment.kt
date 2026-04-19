package com.erc.canopysentinalg.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.data.model.ForestStats
import com.erc.canopysentinalg.databinding.FragmentHomeBinding
import com.erc.canopysentinalg.ui.adapter.AlertsAdapter
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForestViewModel by viewModels({ requireActivity() })
    private lateinit var alertsAdapter: AlertsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCountryDropdown()
        observeViewModel()
    }

    private fun setupCountryDropdown() {
        // Observe countries from ViewModel to populate the dropdown
        viewModel.countries.observe(viewLifecycleOwner) { countries ->
            val countryNames = countries.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countryNames)
            binding.countryAutoComplete.setAdapter(adapter)

            binding.countryAutoComplete.setOnItemClickListener { _, _, position, _ ->
                val selectedCountry = countries[position]
                // Update ViewModel and navigate to Map
                viewModel.selectCountry(selectedCountry)
                findNavController().navigate(R.id.action_navigation_home_to_navigation_map)
            }
        }
    }

    private fun setupRecyclerView() {
        alertsAdapter = AlertsAdapter { alert ->
            findNavController().navigate(R.id.navigation_map)
        }
        binding.alertsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.alertsRecyclerView.adapter = alertsAdapter
    }

    private fun observeViewModel() {
        viewModel.forestStats.observe(viewLifecycleOwner) { stats ->
            stats?.let { updateStatistics(it) }
        }

        viewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            alertsAdapter.submitList(alerts.take(5))
        }
    }

    private fun updateStatistics(stats: ForestStats) {
        animateTextChange(binding.totalAreaValue, String.format(Locale.getDefault(), "%.1f", stats.totalArea))
        animateTextChange(binding.deforestedAreaValue, String.format(Locale.getDefault(), "%.1f", stats.deforestedArea))
        animateTextChange(binding.forestHealthValue, String.format(Locale.getDefault(), "%.1f%%", stats.forestHealthPercentage))
    }

    private fun animateTextChange(textView: TextView, newValue: String) {
        textView.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                textView.text = newValue
                textView.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}