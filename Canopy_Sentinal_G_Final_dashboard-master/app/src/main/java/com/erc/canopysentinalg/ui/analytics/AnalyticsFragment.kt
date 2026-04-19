package com.erc.canopysentinalg.ui.analytics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.erc.canopysentinalg.databinding.FragmentAnalyticsBinding
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForestViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        // Setup the PDF download trigger
        binding.btnDownloadPdf.setOnClickListener {
            generateEnvironmentalReport()
        }
    }

    private fun generateEnvironmentalReport() {
        // 1. Create PDF Document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val paint = Paint()
        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            color = Color.BLACK
        }

        // 2. Write Text Data
        canvas.drawText("CANOPY SENTINEL: ENVIRONMENTAL REPORT", 50f, 50f, titlePaint)

        paint.textSize = 14f
        canvas.drawText("Generated on: ${Date()}", 50f, 80f, paint)
        canvas.drawText("Current Stats:", 50f, 120f, titlePaint)
        canvas.drawText("Carbon Sequestered: ${binding.tvCarbonTons.text}", 50f, 150f, paint)
        canvas.drawText("Estimated Value: ${binding.tvCarbonValue.text}", 50f, 175f, paint)

        // 3. Add Charts as Bitmaps
        try {
            // Get Health Chart Bitmap
            val healthBitmap = binding.healthLineChart.chartBitmap
            val scaledHealth = Bitmap.createScaledBitmap(healthBitmap, 500, 250, true)
            canvas.drawText("Forest Health Trend:", 50f, 220f, titlePaint)
            canvas.drawBitmap(scaledHealth, 50f, 240f, paint)

            // Get Deforestation Chart Bitmap
            val lossBitmap = binding.deforestationBarChart.chartBitmap
            val scaledLoss = Bitmap.createScaledBitmap(lossBitmap, 500, 250, true)
            canvas.drawText("Monthly Area Loss (ha):", 50f, 520f, titlePaint)
            canvas.drawBitmap(scaledLoss, 50f, 540f, paint)
        } catch (e: Exception) {
            canvas.drawText("Chart data currently unavailable in PDF", 50f, 220f, paint)
        }

        pdfDocument.finishPage(page)

        // 4. Save to Public Downloads Directory
        val fileName = "Canopy_Sentinel_Report_${System.currentTimeMillis()}.pdf"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "Report Saved to Downloads: $fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Download Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    private fun observeData() {
        viewModel.forestStats.observe(viewLifecycleOwner) { stats ->
            stats?.let {
                setupHealthChart(it.forestHealthPercentage)
                setupDeforestationChart(it.deforestedArea)

                val (tons, financialValue) = viewModel.getCarbonEstimation(
                    it.totalArea,
                    it.forestHealthPercentage
                )

                binding.tvCarbonTons.text = String.format("%,.2f MT CO2e", tons)
                binding.tvCarbonValue.text = String.format("Estimated Value: $%,.2f", financialValue)
            }
        }
    }

    private fun setupHealthChart(currentHealth: Double) {
        val entries = ArrayList<Entry>()
        entries.add(Entry(0f, (currentHealth + 1.2).toFloat()))
        entries.add(Entry(1f, (currentHealth + 0.8).toFloat()))
        entries.add(Entry(2f, (currentHealth + 0.3).toFloat()))
        entries.add(Entry(3f, currentHealth.toFloat()))

        val labels = arrayOf("Oct", "Nov", "Dec", "Jan")

        val dataSet = LineDataSet(entries, "Health %").apply {
            color = Color.parseColor("#4CAF50")
            setCircleColor(Color.parseColor("#2E7D32"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(true)
            valueTextSize = 10f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        binding.healthLineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            animateX(1000)
            invalidate()
        }
    }

    private fun setupDeforestationChart(currentLoss: Double) {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, (currentLoss * 0.7).toFloat()))
        entries.add(BarEntry(1f, (currentLoss * 1.1).toFloat()))
        entries.add(BarEntry(2f, currentLoss.toFloat()))

        val labels = arrayOf("Nov", "Dec", "Jan")

        val dataSet = BarDataSet(entries, "Area Loss").apply {
            color = Color.parseColor("#F44336")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        binding.deforestationBarChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            animateY(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}