package com.example.userhauntedfm;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userhauntedfm.AudioItem;
import com.example.userhauntedfm.R;
import com.example.userhauntedfm.AudioPlayerActivity;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private Context context;
    private List<AudioItem> audioItems;
    private String selectedPlaylistId;

    public AudioAdapter(Context context, List<AudioItem> audioItems, String selectedPlaylistId) {
        this.context = context;
        this.audioItems = audioItems;
        this.selectedPlaylistId = selectedPlaylistId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AudioItem audioItem = audioItems.get(position);
        holder.audioNameTextView.setText(audioItem.getAudioName());
        holder.audioDescriptionTextView.setText(audioItem.getAudioDescription());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AudioPlayerActivity and pass the audio name and description
                Intent intent = new Intent(context, AudioPlayerActivity.class);
                intent.putExtra("audioName", audioItem.getAudioName());
                intent.putExtra("audioDescription", audioItem.getAudioDescription());
                intent.putExtra("audioItemId", audioItem.getId());
                intent.putExtra("playlistId", selectedPlaylistId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView audioNameTextView;
        TextView audioDescriptionTextView;
        Button playButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioNameTextView = itemView.findViewById(R.id.audioNameTextView);
            audioDescriptionTextView = itemView.findViewById(R.id.audioDescriptionTextView);
            playButton = itemView.findViewById(R.id.playsection);
        }
    }
}
