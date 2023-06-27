package com.example.userhauntedfm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.userhauntedfm.Playlist;
import com.example.userhauntedfm.R;

public class SongFetch extends AppCompatActivity {

    private ImageView imageViewPlaylist;
    private TextView textViewName;
    private TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_fetch);

        imageViewPlaylist = findViewById(R.id.songImageView);
        textViewName = findViewById(R.id.songNameTextView);
        textViewDescription = findViewById(R.id.songDescriptionTextView);

        Playlist playlist = getIntent().getParcelableExtra("playlist");
        if (playlist != null) {
            Glide.with(this).load(playlist.getImageUrl()).into(imageViewPlaylist);
            textViewName.setText(playlist.getName());
            textViewDescription.setText(playlist.getDescription());
        }
    }
}
