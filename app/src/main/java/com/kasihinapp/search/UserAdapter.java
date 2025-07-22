package com.kasihinapp.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// --- UBAH IMPORT INI ---
import com.kasihinapp.model.User; // Ganti dari com.google.firebase...
// -----------------------

import com.kasihinapp.R;

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

        holder.name.setText(user.getNama());
        holder.role.setText(user.getRole());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            // --- PERUBAHAN: Kirim ID pengguna ---
            intent.putExtra("user_id", user.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
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