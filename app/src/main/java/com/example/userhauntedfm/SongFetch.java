package com.example.userhauntedfm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.userhauntedfm.Playlist;
import com.example.userhauntedfm.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class SongFetch extends AppCompatActivity {

    private ImageView imageViewPlaylist;
    private TextView textViewName;
    private TextView textViewDescription;
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    private List<AudioItem> audioItems;
    private FirebaseFirestore firebaseFirestore;

    private String selectedPlaylistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_fetch);

        imageViewPlaylist = findViewById(R.id.songImageView);
        textViewName = findViewById(R.id.songNameTextView);
        textViewDescription = findViewById(R.id.songDescriptionTextView);

        selectedPlaylistId = getIntent().getStringExtra("playlistId");
        recyclerView = findViewById(R.id.playlistRecyclerView2);
        audioItems = new ArrayList<>();
        audioAdapter = new AudioAdapter(this, audioItems, selectedPlaylistId); // Pass the selectedPlaylistId
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(audioAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fetchAudioItems();
        Playlist playlist = getIntent().getParcelableExtra("playlist");
        if (playlist != null) {
            Glide.with(this).load(playlist.getImageUrl()).into(imageViewPlaylist);
            textViewName.setText(playlist.getName());
            textViewDescription.setText(playlist.getDescription());
        }
    }

    private void fetchAudioItems() {
        if (selectedPlaylistId != null) {
            firebaseFirestore.collection("playlists")
                    .document(selectedPlaylistId)
                    .collection("audioFiles")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        audioItems.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AudioItem audioItem = documentSnapshot.toObject(AudioItem.class);
                            audioItems.add(audioItem);
                        }
                        audioAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }
}
