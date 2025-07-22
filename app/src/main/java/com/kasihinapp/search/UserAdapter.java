package com.kasihinapp.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kasihinapp.model.User;
import com.kasihinapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList; // Sekarang ini adalah daftar yang sudah di-filter/rekomendasi dari Activity
    private OnItemClickListener listener; // Deklarasi listener

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    // Metode filter tidak lagi dibutuhkan di adapter karena filtering dilakukan di Activity
    /*
    public void filter(String keyword) {
        // Logic filtering ini dipindahkan ke SearchActivity
    }
    */

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = userList.get(position); // Gunakan userList langsung

        holder.name.setText(user.getNama());
        holder.role.setText(user.getRole());

        // Anda bisa menambahkan logika untuk menampilkan gambar profil jika ada URL di objek User
        // Contoh: Glide.with(holder.itemView.getContext()).load(user.getImageUrl()).into(holder.image);
        // Untuk saat ini, menggunakan gambar default:
        holder.image.setImageResource(R.mipmap.ic_launcher); // Menggunakan mipmap karena ic_launcher berada disana


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user); // Panggil onItemClick saat item diklik
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, role;
        ImageView image;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            role = itemView.findViewById(R.id.tvRole);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}