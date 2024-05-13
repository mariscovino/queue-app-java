package model;

import org.json.JSONObject;
import persistence.Writable;

// Class for Feedback
public class Feedback implements Writable {
    String feedback;
    Song song;

    // Constructor
    // EFFECTS: creates a new instance of Feedback class with a feedback and a corresponding song
    public Feedback(String feedback, Song song) {
        this.feedback = feedback;
        this.song = song;
    }

    // Getters
    public String getFeedback() {
        return feedback;
    }

    public Song getSong() {
        return song;
    }

    // Setters
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("feedback", feedback);
        json.put("name", song.getName());
        json.put("artist", song.getArtist());
        return json;
    }
}
