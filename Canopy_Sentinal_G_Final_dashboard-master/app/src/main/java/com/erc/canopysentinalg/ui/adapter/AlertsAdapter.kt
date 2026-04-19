package com.erc.canopysentinalg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class AlertsAdapter(
    private val onAlertClick: (DeforestationAlert) -> Unit
) : ListAdapter<DeforestationAlert, AlertsAdapter.AlertViewHolder>(AlertDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(getItem(position), onAlertClick)
    }

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val regionText: TextView = itemView.findViewById(R.id.locationText)
        private val areaText: TextView = itemView.findViewById(R.id.areaText)
        private val timestampText: TextView = itemView.findViewById(R.id.dateText)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardAlert)

        fun bind(alert: DeforestationAlert, onAlertClick: (DeforestationAlert) -> Unit) {
            regionText.text = alert.region
            areaText.text = String.format(Locale.getDefault(), "%.1f ha", alert.area)
            
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            timestampText.text = dateFormat.format(Date(alert.timestamp))

            // Set severity color
            val severityColor = when (alert.severity) {
                DeforestationAlert.AlertSeverity.CRITICAL -> R.color.error
                DeforestationAlert.AlertSeverity.HIGH -> R.color.warning
                DeforestationAlert.AlertSeverity.MEDIUM -> R.color.accent
                DeforestationAlert.AlertSeverity.LOW -> R.color.text_secondary
            }
            cardView.strokeColor = ContextCompat.getColor(itemView.context, severityColor)
            cardView.strokeWidth = 2

            itemView.setOnClickListener {
                onAlertClick(alert)
            }
        }
    }

    private class AlertDiffCallback : DiffUtil.ItemCallback<DeforestationAlert>() {
        override fun areItemsTheSame(
            oldItem: DeforestationAlert,
            newItem: DeforestationAlert
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DeforestationAlert,
            newItem: DeforestationAlert
        ): Boolean = oldItem == newItem
    }
}
