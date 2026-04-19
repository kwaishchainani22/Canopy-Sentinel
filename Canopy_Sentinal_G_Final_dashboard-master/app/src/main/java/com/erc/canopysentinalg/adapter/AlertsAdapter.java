package com.erc.canopysentinalg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.erc.canopysentinalg.R;
import com.erc.canopysentinalg.model.DeforestationAlert;
import java.util.ArrayList;
import java.util.List;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertViewHolder> {
    private List<DeforestationAlert> alerts = new ArrayList<>();
    private OnAlertClickListener listener;

    public interface OnAlertClickListener {
        void onAlertClick(DeforestationAlert alert);
    }

    public AlertsAdapter(OnAlertClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        DeforestationAlert alert = alerts.get(position);
        holder.bind(alert);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public void setAlerts(List<DeforestationAlert> alerts) {
        this.alerts = alerts;
        notifyDataSetChanged();
    }

    class AlertViewHolder extends RecyclerView.ViewHolder {
        private TextView regionText;
        private TextView areaText;
        private TextView timestampText;

        AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            regionText = itemView.findViewById(R.id.locationText);
            areaText = itemView.findViewById(R.id.areaText);
            timestampText = itemView.findViewById(R.id.dateText);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onAlertClick(alerts.get(getAdapterPosition()));
                }
            });
        }

        void bind(DeforestationAlert alert) {
            regionText.setText(alert.getRegion());
            areaText.setText(String.format("%.1f ha", alert.getArea()));
            timestampText.setText(alert.getTimestamp());
        }
    }
} 