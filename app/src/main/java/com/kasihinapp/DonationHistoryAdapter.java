package com.kasihinapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kasihinapp.model.DonationHistory;
import java.util.List;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder> {
    private List<DonationHistory> historyList;
    public DonationHistoryAdapter(List<DonationHistory> historyList) { this.historyList = historyList; }
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home_donasi, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationHistory item = historyList.get(position);
        holder.tvInfo.setText(item.getDariNama() + " mengirim ke " + item.getKeNama());
        holder.tvAmount.setText(item.getJumlah() + " Poin");
    }
    @Override
    public int getItemCount() { return historyList.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo, tvAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tvDonationInfo);
            tvAmount = itemView.findViewById(R.id.tvDonationAmount);
        }
    }
}