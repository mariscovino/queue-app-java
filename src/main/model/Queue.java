package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;
import java.util.Objects;

// Class for the both the song queue and song requests queue
// Methods and fields in Queue class are shared between audience and artist actions
public class Queue implements Writable {
    protected LinkedList<Song> songQueue;
    protected LinkedList<Song> songRequests;
    protected LinkedList<Feedback> feedbacks;
    protected String concertName;

    // Constructor
    // EFFECTS: creates a new instance of Queue class, with empty songQueue, songRequests and feedbacks
    public Queue() {
        this.songQueue = new LinkedList<>();
        this.songRequests = new LinkedList<>();
        this.feedbacks = new LinkedList<>();
        this.concertName = "";
    }

    // Getters
    public LinkedList<Song> getSongQueue() {
        return songQueue;
    }

    public LinkedList<Song> getSongRequests() {
        return songRequests;
    }

    public LinkedList<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public String getConcertName() {
        return concertName;
    }

    public boolean isEmptySongRequests() {
        return songRequests.isEmpty();
    }

    public void setSongQueue(LinkedList<Song> songQueue) {
        this.songQueue = songQueue;
    }

    public void setSongRequests(LinkedList<Song> songRequests) {
        this.songRequests = songRequests;
    }

    public void setFeedbacks(LinkedList<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    // EFFECTS: prints songQueue in order and in the format of a list
    public void viewQueue() {
        if (!songQueue.isEmpty()) {
            for (int i = 0; i < songQueue.size(); i++) {
                System.out.println((i + 1) + ". " + songQueue.get(i).getName() + " by "
                        + songQueue.get(i).getArtist());
            }
            return;
        }
        System.out.println("Cannot view song queue: queue is empty");
    }

    // EFFECTS: prints songRequests in order and in the format of a list
    public void viewAllRequests() {
        if (!songRequests.isEmpty()) {
            for (int i = 0; i < songRequests.size(); i++) {
                System.out.println((i + 1) + ". " + songRequests.get(i).getName() + " by "
                        + songRequests.get(i).getArtist());
            }
            return;
        }
        System.out.println("Cannot view song requests: there are no requests");
    }

    // EFFECTS: finds song in feedbacks and sets new feedback
    //          if song is not found, prints message
    public void findSongInFeedback(Song s, String f) {
        for (Feedback feedback : feedbacks) {
            if (Objects.equals(feedback.getSong().getName(), s.getName())
                    && Objects.equals(feedback.getSong().getArtist(), s.getArtist())) {
                feedback.setFeedback(f);
                return;
            }
        }
        System.out.println("Song was not found in the feedbacks");
    }

    // EFFECTS: removes Song s from songRequests
    //          if Song s is not found, prints line
    public void removeSongRequest(Song s) {
        for (Song songRequest : songRequests) {
            if (Objects.equals(songRequest.getName(), s.getName())
                    && Objects.equals(songRequest.getArtist(), s.getArtist())) {
                songRequests.remove(songRequest);
                EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                        + " by " + s.getArtist() + " was removed from songRequests."));
                return;
            }
        }
        System.out.println("Song cannot be removed from the requests: song not found");
    }

    // EFFECTS: removes Song s from songQueue
    //          if Song s is not found, prints line
    public void removeSongQueue(Song s) {
        for (Song song : songQueue) {
            if (Objects.equals(song.getName(), s.getName())
                    && Objects.equals(song.getArtist(), s.getArtist())) {
                songQueue.remove(song);
                EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                        + " by " + s.getArtist() + " was removed from songQueue."));
                return;
            }
        }
        System.out.println("Song cannot be removed from the queue: song not found");
    }

    // EFFECTS: returns true if a song with the same name and same artist was found in a list
    //          returns false otherwise
    public boolean foundInList(LinkedList<Song> list, Song song) {
        if (list != null) {
            for (Song s : list) {
                if (Objects.equals(s.getName(), song.getName())
                        && Objects.equals(s.getArtist(), song.getArtist())) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    // EFFECTS: returns things in this Queue's SongQueue as a JSON array
    protected JSONArray songQueueToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Song s : songQueue) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns things in this Queue's SongRequests as a JSON array
    protected JSONArray songRequestsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Song s : songRequests) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns things in this Queue's feedbacks as a JSON array
    protected JSONArray feedbacksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Feedback f : feedbacks) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("concertName", concertName);
        json.put("songQueue", songQueueToJson());
        json.put("songRequests", songRequestsToJson());
        json.put("feedbacks", feedbacksToJson());
        return json;
    }
}

