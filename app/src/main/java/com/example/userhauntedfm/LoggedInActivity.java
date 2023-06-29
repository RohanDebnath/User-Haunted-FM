package com.example.userhauntedfm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LoggedInActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    private List<Playlist> filteredList; // List to store filtered results
    private FirebaseFirestore db;
    private EditText searchEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private TextView welcomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        fetchPlaylists();
        setupSearch();

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        welcomeTextView = findViewById(R.id.welcomeTextView);

        Button logoutButton = findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login activity
                Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersRef.child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.getValue(String.class);
                    if (name != null) {
                        String welcomeMessage = name;
                        welcomeTextView.setText(welcomeMessage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });
        }
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

            Intent intent = new Intent(LoggedInActivity.this, SongFetch.class);
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