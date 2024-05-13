package ui.graphic.artist;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.EnterSong;

import javax.swing.*;

// Class for window shown when an artist adds song to queue
public class AddSongToQueue extends EnterSong {

    // Constructor
    // EFFECTS: Creates a new instance of the AddSongToQueue class with the superclass' constructor
    public AddSongToQueue(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                          Audience audience) {
        super(window, panelLeft, panelRight, queue, artist, audience);
    }

    // EFFECTS: Check if songRequests contains the entered song if it is not empty
    //          If foundInList, checkLabel method is called with the appropriate message. Else, success method is called
    //          If songRequests is empty, success method is called
    @Override
    protected void checkIfContains(Song s) {
        if (!songQueue.isEmpty()) {
            if (foundInList(songQueue, s)) {
                checkLabel(panelRight, "This song is already in the queue.");
            } else {
                success(s);
            }
        } else {
            success(s);
        }
    }

    // MODIFIES: songQueue
    // EFFECTS: Makes artist add Song s to songQueue and calls checkLabel method with the appropriate message
    @Override
    protected void success(Song s) {
        artist.addSongToQueue(s);

        checkLabel(panelRight, "Your song has been added to the queue.");
        panelRight.revalidate();
    }
}
