package model;

import org.json.JSONObject;
import persistence.Writable;

// Class for Song
public class Song implements Writable {
    String name;
    String artist;

    // Constructor
    // EFFECTS: creates a new instance of Song class with a name and an artist
    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("artist", artist);
        return json;
    }
}
