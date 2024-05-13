package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// Class for artist
// Methods and fields in Artist class are only used for artist actions
public class Artist extends Queue implements Writable {
    protected LinkedList<Song> played;

    // Constructor
    // EFFECTS: creates a new instance of Artist class with empty played
    //          points to the same songQueue, songRequests and feedbacks created in superclass
    public Artist(LinkedList<Song> songQueue, LinkedList<Song> songRequests, LinkedList<Feedback> feedbacks,
                  String concertName) {
        this.songQueue = songQueue;
        this.songRequests = songRequests;
        this.feedbacks = feedbacks;
        this.played = new LinkedList<>();
        this.concertName = concertName;
    }

    // Getters
    public LinkedList<Song> getPlayed() {
        return played;
    }

    // MODIFIES: songQueue
    // EFFECTS: adds Song s to songQueue and prints out appropriate message
    public void addSongToQueue(Song s) {
        songQueue.addLast(s);
        System.out.println("Your song has been added to the queue.");
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " was added to songQueue"));
    }

    // EFFECTS: prints out played in order in the format of a list
    //          prints out message if played is empty
    public void viewPlayed() {
        if (!played.isEmpty()) {
            for (int i = 0; i < played.size(); i++) {
                System.out.println((i + 1) + ". " + played.get(i).getName() + " by " + played.get(i).getArtist());
            }
            return;
        }
        System.out.println("You have no played songs.");
    }

    // EFFECTS: accepts song from songRequests
    //          removes song from song requests
    //          updates feedback
    public void acceptSong(Song s) {
        findSongInFeedback(s, "Thank you for your request! Your Song has been added to the queue!");
        removeSongRequest(s);
        songQueue.addLast(s);
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " was accepted by artist."));
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " had its feedback updated in feedbacks."));

    }

    // EFFECTS: denies song from songRequests
    //          removes song from song requests
    //          updates feedback
    public void denySong(Song s, String f) {
        findSongInFeedback(s, f);
        removeSongRequest(s);
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " was denied by artist."));
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " had its feedback updated in feedbacks."));
    }

    // EFFECTS: plays song from song queue
    //          adds song to played
    //          removes song from songQueue if it contains it
    public void playSong(Song s) {
        played.addLast(s);
        if (foundInList(songQueue, s)) {
            removeSongQueue(s);
        }
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " was added to played."));
    }

    // MODIFIES: this
    // EFFECTS: returns songs in this Artist's played as a JSON array
    private JSONArray playedToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Song s : played) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: stores the played JSONArray in a file-
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("played", playedToJson());
        return json;
    }
}
