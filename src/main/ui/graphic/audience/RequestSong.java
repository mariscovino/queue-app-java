package ui.graphic.audience;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.EnterSong;

import javax.swing.*;

// Class for window shown when an audience requests a song
public class RequestSong extends EnterSong {

    // Constructor
    // EFFECTS: creates a new instance of RequestSong class with the superclass' constructor
    public RequestSong(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                       Audience audience) {
        super(window, panelLeft, panelRight, queue, artist, audience);
    }

    // EFFECTS: Checks if all lists, songsPlayed, songRequests and songQueue contain Song s
    //          If foundInList in any of the mentioned lists, calls checkLabel with the appropriate message
    //          Else, if song is not found in any of the three lists, calls success method
    @Override
    protected void checkIfContains(Song s) {
        if (!songRequests.isEmpty()) {
            if (foundInList(songRequests, s)) {
                checkLabel(panelRight, "This song was already requested.");
                return;
            }
        }

        if (!songsPlayed.isEmpty()) {
            if (foundInList(songsPlayed, s)) {
                checkLabel(panelRight, "This song was already played.");
                return;
            }
        }

        if (!songQueue.isEmpty()) {
            if (foundInList(songQueue, s)) {
                checkLabel(panelRight, "This song is already in the queue.");
                return;
            }
        }
        
        success(s);
    }

    // MODIFIES: songRequests, feedbacks
    // EFFECTS: adds Song s to songRequests by calling audience's addSongRequest
    //          Calls checkLabel method with the appropriate message
    @Override
    protected void success(Song s) {
        audience.addSongRequest(s, songsPlayed);

        checkLabel(panelRight, "Your song has been added to the requests.");
    }

    // EFFECTS: Creates exit button that returns to AudienceMain
    @Override
    protected void createExitButton() {
        this.exit = new JButton("Exit");
        this.exit.addActionListener(e -> {
            window.remove(panelLeft);
            window.remove(panelRight);
            AudienceMain audienceMain
                    = new AudienceMain(window, queue, artist, audience);
            audienceMain.show();
        });
    }
}
