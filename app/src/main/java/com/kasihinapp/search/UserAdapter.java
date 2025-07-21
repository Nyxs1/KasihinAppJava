// UserAdapter.java

package com.kasihinapp.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kasihinapp.R; // <-- Tambahkan import untuk R
import com.kasihinapp.models.User; // <-- PERBAIKAN: Sesuaikan dengan package User.java

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private List<User> filteredList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
        this.filteredList = new ArrayList<>(userList);
    }

    public void filter(String keyword) {
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(userList);
        } else {
            for (User user : userList) {
                // PENYESUAIAN: Menggunakan getNama()
                if (user.getNama().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))) {
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = filteredList.get(position);

        // --- PENYESUAIAN DATA DENGAN MODEL USER ---
        holder.name.setText(user.getNama()); // Menggunakan getNama()
        holder.role.setText(user.getRole()); // Menggunakan getRole()
        holder.lastActive.setText(user.getCreatedAt()); // Menggunakan getCreatedAt() untuk last active

        // Catatan: Model User tidak punya data gambar. Baris ini bisa diaktifkan jika model diperbarui.
        // holder.image.setImageResource(R.mipmap.ic_launcher);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra("name", user.getNama()); // Menggunakan getNama()
            intent.putExtra("role", user.getRole()); // Menggunakan getRole()
            // intent.putExtra("image", R.mipmap.ic_launcher); // Sesuaikan jika perlu
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, role, lastActive;
        ImageView image;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            role = itemView.findViewById(R.id.tvRole);
            lastActive = itemView.findViewById(R.id.tvLastActive);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}