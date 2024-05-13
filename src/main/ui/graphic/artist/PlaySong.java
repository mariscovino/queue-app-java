package ui.graphic.artist;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.EnterSong;

import javax.swing.*;

// Class for window shown when an artist plays a song
public class PlaySong extends EnterSong {

    // Constructor
    // EFFECTS: creates a new instance of PlaySong class with the superclass' constructor
    public PlaySong(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                    Audience audience) {
        super(window, panelLeft, panelRight, queue, artist, audience);
    }

    // EFFECTS: If songsPlayed is not empty, checks if songsPlayed contains Song s
    //          If foundInList, calls checkLabel with the appropriate message
    //          If songRequests does not contain Song s, calls success method
    //          If songRequests is empty, calls success method
    @Override
    protected void checkIfContains(Song s) {
        if (!songsPlayed.isEmpty()) {
            if (foundInList(songsPlayed, s)) {
                checkLabel(panelRight, "This song was already played.");
            } else {
                success(s);
            }
        } else {
            success(s);
        }
    }

    // MODIFIES: songsPlayed, songQueue
    // EFFECTS: makes artist play Song s and calls checkLabel method with the appropriate message
    @Override
    protected void success(Song s) {
        artist.playSong(s);

        checkLabel(panelRight, "Your song has been added to list of songs played.");
    }
}
