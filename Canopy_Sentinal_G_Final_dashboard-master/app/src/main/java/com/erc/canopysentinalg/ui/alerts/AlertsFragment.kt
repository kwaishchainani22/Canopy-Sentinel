package com.erc.canopysentinalg.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.erc.canopysentinalg.ui.adapter.AlertsAdapter
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel

class AlertsFragment : Fragment() {
    private val viewModel: ForestViewModel by viewModels({ requireActivity() })
    private lateinit var alertsRecyclerView: RecyclerView
    private lateinit var alertsAdapter: AlertsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView)
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        alertsAdapter = AlertsAdapter { alert ->
            // Navigate to map with alert location
            findNavController().navigate(R.id.navigation_map)
        }
        alertsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alertsRecyclerView.adapter = alertsAdapter
    }
    
    private fun observeViewModel() {
        viewModel.alerts.observe(viewLifecycleOwner, Observer { alerts ->
            alertsAdapter.submitList(alerts)
        })
    }
}
