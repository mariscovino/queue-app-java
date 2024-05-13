package ui.graphic.artist;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.EnterSong;

import javax.swing.*;

// Class for window shown when an artist accepts song requests
public class AcceptSong extends EnterSong {

    // Constructor
    // EFFECTS: Creates a new instance of the AcceptSong class with the superclass' constructor
    public AcceptSong(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                      Audience audience) {
        super(window, panelLeft, panelRight, queue, artist, audience);
    }

    // EFFECTS: Check if songRequests contains the entered song if it is not empty
    //          If foundInList, success method is called. Else, checkLabel method is called with the appropriate message
    //          If songRequests is empty, checkLabel method is called with the appropriate message
    @Override
    protected void checkIfContains(Song s) {
        if (!songRequests.isEmpty()) {
            if (foundInList(songRequests, s)) {
                success(s);
            } else {
                checkLabel(panelRight, "Song cannot be accepted: song not found.");
            }
        } else {
            checkLabel(panelRight, "Cannot accept songs: there are no requests.");
        }
    }

    // MODIFIES: songRequests, songQueue
    // EFFECTS: makes artist accept Song s and calls checkLabel method with the appropriate message
    @Override
    protected void success(Song s) {
        artist.acceptSong(s);

        checkLabel(panelRight, "The song was accepted.");
        
    }
}
