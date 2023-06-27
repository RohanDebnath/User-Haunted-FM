package com.example.userhauntedfm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<Playlist> playlistList;

    public PlaylistAdapter(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_user, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        holder.playlistNameTextView.setText(playlist.getName());
        holder.playlistDescriptionTextView.setText(playlist.getDescription());

        // Load the playlist image using Glide library
        Glide.with(holder.itemView.getContext())
                .load(playlist.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image while loading
                .error(R.drawable.ic_launcher_foreground) // Error image if loading fails
                .into(holder.playlistImageView);
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {

        public ImageView playlistImageView;
        public TextView playlistNameTextView;
        public TextView playlistDescriptionTextView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistImageView = itemView.findViewById(R.id.playlistImageView);
            playlistNameTextView = itemView.findViewById(R.id.playlistNameTextView);
            playlistDescriptionTextView = itemView.findViewById(R.id.playlistDescriptionTextView);
        }
    }
}
