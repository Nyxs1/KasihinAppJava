package com.kasihinapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kasihinapp.model.DonationHistory;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder> {
    private List<DonationHistory> historyList;

    public DonationHistoryAdapter(List<DonationHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // --- PERUBAHAN 1: Gunakan layout list_item_donasi.xml ---
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_donasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationHistory item = historyList.get(position);

        // --- PERUBAHAN 3: Isi data ke semua view yang ada di layout ---
        // Atur gambar profil (untuk saat ini pakai default)
        holder.profileImage.setImageResource(R.drawable.profil_jerome);

        // Tampilkan info pengirim dan penerima
        holder.nama.setText(item.getDariNama() + " ke " + item.getKeNama());
        holder.role.setText("Donations"); // Teks statis sesuai desain

        // Tampilkan jumlah poin
        holder.poin.setText("POIN " + item.getJumlah());

        // Tampilkan tanggal (perlu diformat agar lebih bagus)
        holder.tanggal.setText(item.getTanggal());

        // Tampilkan pesan donasi
        holder.comment.setText("Comment\n" + item.getPesan());
    }

    @Override
    public int getItemCount() {
        // Jika jumlah data lebih dari 3, kembalikan 3.
        // Jika kurang dari 3, kembalikan jumlah data yang sebenarnya.
        return Math.min(historyList.size(), 3);
    }

    // --- PERUBAHAN 2: Sesuaikan ViewHolder dengan semua ID di list_item_donasi.xml ---
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nama, role, poin, tanggal, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.donasi_profile_image);
            nama = itemView.findViewById(R.id.donasi_nama);
            role = itemView.findViewById(R.id.donasi_role);
            poin = itemView.findViewById(R.id.donasi_poin);
            tanggal = itemView.findViewById(R.id.donasi_tanggal);
            comment = itemView.findViewById(R.id.donasi_comment);
        }
    }
}