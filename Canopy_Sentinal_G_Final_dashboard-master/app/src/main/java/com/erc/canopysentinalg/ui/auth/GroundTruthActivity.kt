package com.erc.canopysentinalg.ui.auth

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.databinding.ActivityGroundTruthBinding

class GroundTruthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroundTruthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroundTruthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Get Data from Intent passed by MapFragment
        val name = intent.getStringExtra("EXTRA_NAME") ?: "Rainforest"
        val health = intent.getDoubleExtra("EXTRA_HEALTH", 0.0)

        // 2. Set UI Text for the AR Lens
        binding.tvArRegion.text = "Region: $name"
        binding.tvArHealth.text = "Health: ${String.format("%.1f", health)}%"

        // 3. Mapping based on your provided list and Congo Basin
        val imageRes = when {
            // Your custom mappings
            name.contains("Malaysia", ignoreCase = true) -> R.drawable.sumatra_ground
            name.contains("Papua New Guinea", ignoreCase = true) -> R.drawable.guinea_ground
            name.contains("Peru", ignoreCase = true) -> R.drawable.cerrado_ground
            name.contains("Indonesia", ignoreCase = true) -> R.drawable.borneo_ground // Match your 'borrneo' typo
            name.contains("Russia", ignoreCase = true) -> R.drawable.boreal_ground
            name.contains("Brazil", ignoreCase = true) -> R.drawable.atlantic_ground
            name.contains("Canada", ignoreCase = true) -> R.drawable.canada_ground

            // Previous and added mappings
            name.contains("Colombia", ignoreCase = true) ||
                    name.contains("United States", ignoreCase = true) ||
                    name.contains("USA", ignoreCase = true) -> R.drawable.amazon_ground

            name.contains("Congo", ignoreCase = true) ||
                    name.contains("Africa", ignoreCase = true) -> R.drawable.congo_ground

            // Fallback for any other regions
            else -> R.drawable.whatsapp_image_2026_02_07_at_11_17_13_pm_removebg_preview
        }

        binding.ivGroundView.setImageResource(imageRes)

        // 4. Immersive Zoom Animation
        binding.ivGroundView.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(12000)
            .start()

        // 5. Exit Button Logic
        binding.fabExitAR.setOnClickListener {
            finish()
        }

        // 6. Start the AI Scan Line Animation
        startScanningEffect()
    }

    private fun startScanningEffect() {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f
        ).apply {
            duration = 3500
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }
        // Link to the scan line in your XML
        binding.viewScanLine.startAnimation(animation)
    }
}