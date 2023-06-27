package com.example.userhauntedfm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        fetchPlaylists();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.playlistRecyclerView);
        playlistList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(playlistList);

        // Set the RecyclerView with a GridLayoutManager to display two columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(playlistAdapter);
    }

    private void fetchPlaylists() {
        db.collection("playlists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        playlistList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Playlist playlist = document.toObject(Playlist.class);
                            if (playlist != null) {
                                playlistList.add(playlist);
                            }
                        }
                        playlistAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("MainActivity", "Error fetching playlists: " + task.getException());
                    }
                });

        playlistAdapter.setOnItemClickListener(position -> {
            Playlist playlist = playlistList.get(position);

            Intent intent = new Intent(MainActivity.this, SongFetch.class);
            intent.putExtra("playlist", playlist);
            intent.putExtra("playlistId", playlist.getId());
            startActivity(intent);
        });
    }

}
