package com.example.userhauntedfm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userhauntedfm.AudioItem;
import com.example.userhauntedfm.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private Context context;
    private List<AudioItem> audioItems;

    public AudioAdapter(Context context, List<AudioItem> audioItems) {
        this.context = context;
        this.audioItems = audioItems;
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
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView audioNameTextView;
        TextView audioDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioNameTextView = itemView.findViewById(R.id.audioNameTextView);
            audioDescriptionTextView = itemView.findViewById(R.id.audioDescriptionTextView);
        }
    }
}
