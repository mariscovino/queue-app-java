package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;
import java.util.Objects;

// Class for Audience
// Methods and fields in Audience class are only used for Audience actions
public class Audience extends Queue {

    // Constructor
    // EFFECTS: creates a new instance of Audience class
    //          points to the same songQueue, songRequests and feedbacks created in superclass
    public Audience(LinkedList<Song> songQueue, LinkedList<Song> songRequests, LinkedList<Feedback> feedbacks,
                    String concertName) {
        this.songQueue = songQueue;
        this.songRequests = songRequests;
        this.feedbacks = feedbacks;
        this.concertName = concertName;
    }

    // MODIFIES: songRequests
    // EFFECTS: adds Song s to the end of songRequest queue
    //          if song was not played and if song was not previously requested
    //          sets feedback as pending
    //          prints out corresponding messages to each case
    public void addSongRequest(Song s, LinkedList<Song> played) {
        if (!songRequests.isEmpty()) {
            if (foundInList(songRequests, s)) {
                System.out.println("Sorry. This song was already requested.");
                return;
            }

        }

        if (!played.isEmpty()) {
            if (foundInList(played, s)) {
                System.out.println("Sorry. This song was already played.");
                return;
            }

        }

        if (!songQueue.isEmpty()) {
            if (foundInList(songQueue, s)) {
                System.out.println("Sorry. This song is already in the queue.");
                return;
            }
        }

        songRequests.add(s);

        feedbacks.add(new Feedback("Pending approval", s));

        System.out.println("Your song has been added to the requests.");
        EventLog.getInstance().logEvent(new Event("Song " + s.getName()
                + " by " + s.getArtist() + " was added to songRequests and feedbacks."));
    }

    // EFFECTS: prints out feedbacks in order in the format of a list with songs and corresponding feedback
    //          prints out message if feedbacks is empty
    public void receiveFeedback() {
        if (!feedbacks.isEmpty()) {
            for (Feedback feedback : feedbacks) {
                System.out.println("Feedback for " + feedback.getSong().getName() + " by "
                        + feedback.getSong().getArtist()
                        + ": " + feedback.getFeedback());
            }
            return;
        }
        System.out.println("Cannot view feedbacks: you have no feedbacks.");
    }
}