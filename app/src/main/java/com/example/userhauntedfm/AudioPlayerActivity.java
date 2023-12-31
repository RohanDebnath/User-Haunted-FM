package com.example.userhauntedfm;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;

    private String audioItemId;
    private String audioName;
    private String audioDescription;
    private String playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        playPauseButton = findViewById(R.id.playPauseButton);
        seekBar = findViewById(R.id.seekBar);
        TextView audioNameTextView = findViewById(R.id.audioNameTextView);
        TextView audioDescriptionTextView = findViewById(R.id.audioDescriptionTextView);

        audioItemId = getIntent().getStringExtra("audioItemId");
        audioName = getIntent().getStringExtra("audioName");
        audioDescription = getIntent().getStringExtra("audioDescription");
        playlistId = getIntent().getStringExtra("playlistId");

        audioNameTextView.setText(audioName);
        audioDescriptionTextView.setText(audioDescription);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        handler = new Handler();

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle_24);
                updateSeekBar();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No action needed
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAudioUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(runnable);
    }

    private void fetchAudioUrl() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("playlists")
                .document(playlistId)
                .collection("audioFiles")
                .document(audioItemId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String audioUrl = documentSnapshot.getString("audioFileUrl");
                    if (audioUrl != null) {
                        try {
                            mediaPlayer.setDataSource(audioUrl);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> {
                                // Set the seek bar duration based on the media player
                                seekBar.setMax(mp.getDuration());
                                updateSeekBar(); // Start updating seek bar progress
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }
}
