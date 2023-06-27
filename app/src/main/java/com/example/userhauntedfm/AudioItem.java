package com.example.userhauntedfm;

public class AudioItem {
    private String id;
    private String audioName;
    private String audioDescription;
    private String audioFileUrl;

    // Default constructor (required for Firestore deserialization)
    public AudioItem() {
    }

    public AudioItem(String audioName, String audioDescription, String audioFileUrl) {
        this.audioName = audioName;
        this.audioDescription = audioDescription;
        this.audioFileUrl = audioFileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioDescription() {
        return audioDescription;
    }

    public void setAudioDescription(String audioDescription) {
        this.audioDescription = audioDescription;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }

    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }
}
