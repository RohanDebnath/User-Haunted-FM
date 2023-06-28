package com.example.userhauntedfm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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
    private List<Playlist> filteredList; // List to store filtered results
    private FirebaseFirestore db;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        fetchPlaylists();
        setupSearch();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.playlistRecyclerView);
        playlistList = new ArrayList<>();
        filteredList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(filteredList);

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
                        applyFilter(""); // Apply initial filter with empty search query
                    } else {
                        Log.e("MainActivity", "Error fetching playlists: " + task.getException());
                    }
                });

        playlistAdapter.setOnItemClickListener(position -> {
            Playlist playlist = filteredList.get(position);

            Intent intent = new Intent(MainActivity.this, SongFetch.class);
            intent.putExtra("playlist", playlist);
            intent.putExtra("playlistId", playlist.getId());
            startActivity(intent);
        });
    }

    private void setupSearch() {
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Apply filter whenever the search text changes
                applyFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
    }

    private void applyFilter(String query) {
        filteredList.clear();
        for (Playlist playlist : playlistList) {
            // Filter based on playlist name or any other criteria you prefer
            if (playlist.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(playlist);
            }
        }
        playlistAdapter.notifyDataSetChanged();
    }
}
