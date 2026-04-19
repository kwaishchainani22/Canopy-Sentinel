package com.erc.canopysentinalg.ui.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.erc.canopysentinalg.data.model.Country
import com.erc.canopysentinalg.databinding.FragmentMapBinding
import com.erc.canopysentinalg.ui.auth.GroundTruthActivity
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForestViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext().applicationContext
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        Configuration.getInstance().load(context, prefs)
        Configuration.getInstance().userAgentValue = context.packageName

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()
        setupUIListeners()
        observeViewModel()
    }

    private fun setupMap() {
        binding.map.apply {
            setMultiTouchControls(true)
            controller.setZoom(4.0)
            controller.setCenter(GeoPoint(-14.2350, -51.9253))
        }
    }

    private fun setupUIListeners() {
        binding.timeSlider.addOnChangeListener { _, value, _ ->
            val monthsAgo = value.toInt()
            binding.timeRangeLabel.text = "Vegetation History: $monthsAgo Months Ago"
            viewModel.loadHistoricalStats(monthsAgo)
        }
    }

    private fun observeViewModel() {
        viewModel.countries.observe(viewLifecycleOwner) {
            updateMapPolygons()
        }

        viewModel.forestStats.observe(viewLifecycleOwner) {
            updateMapPolygons()
        }

        viewModel.selectedCountry.observe(viewLifecycleOwner) { country ->
            country?.let {
                val targetLocation = GeoPoint(it.latitude, it.longitude)
                binding.map.controller.animateTo(targetLocation)
                binding.map.controller.setZoom(6.0)
            }
        }
    }

    private fun updateMapPolygons() {
        binding.map.overlays.clear()
        val countries = viewModel.countries.value ?: return
        val currentStats = viewModel.forestStats.value

        countries.forEach { country ->
            val healthToShow = if (country.code == viewModel.selectedCountry.value?.code) {
                currentStats?.forestHealthPercentage ?: country.forestHealthPercentage
            } else {
                country.forestHealthPercentage
            }

            drawCountryHealthBox(country, healthToShow)
        }
        binding.map.invalidate()
    }

    private fun drawCountryHealthBox(country: Country, health: Double) {
        val color = when {
            health > 80 -> Color.argb(120, 0, 255, 0)
            health > 60 -> Color.argb(120, 255, 255, 0)
            else -> Color.argb(120, 255, 0, 0)
        }

        val polygon = Polygon(binding.map)
        val rectPoints = listOf(
            GeoPoint(country.latitude + 2.0, country.longitude - 2.0),
            GeoPoint(country.latitude + 2.0, country.longitude + 2.0),
            GeoPoint(country.latitude - 2.0, country.longitude + 2.0),
            GeoPoint(country.latitude - 2.0, country.longitude - 2.0)
        )

        polygon.apply {
            points = rectPoints
            fillPaint.color = color
            outlinePaint.color = Color.BLACK
            outlinePaint.strokeWidth = 2f
            title = "${country.name}: ${health.toInt()}% Health"

            // Trigger AR Ground Truth Activity on Click
            setOnClickListener { _, _, _ ->
                val intent = Intent(requireContext(), GroundTruthActivity::class.java).apply {
                    putExtra("EXTRA_NAME", country.name)
                    putExtra("EXTRA_HEALTH", health)
                }
                startActivity(intent)
                true
            }
        }
        binding.map.overlays.add(polygon)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}