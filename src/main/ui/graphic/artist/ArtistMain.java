package ui.graphic.artist;

import model.Artist;
import model.Audience;
import model.EventLog;
import model.Queue;
import ui.LogPrinter;
import ui.graphic.QueueWindows;
import ui.graphic.audience.AudienceMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Class for Artist's main window
public class ArtistMain extends QueueWindows {
    private JTable songsPlayed;

    private JButton addSongToQueue;
    private JButton acceptSong;
    private JButton denySong;
    private JButton playSong;
    private JButton goTo;

    // Constructor
    // EFFECTS: Creates a new instance of the ArtistMain class with the superclass' constructor
    public ArtistMain(JFrame artistWindow, Queue queue, Artist artist, Audience audience) {
        super(artistWindow, queue, artist, audience);
    }

    // EFFECTS: Sets artistWindow parameter to window variable
    //          Sets window's layout to BorderLayout
    //          Sets window's title to adequate title
    //          Sets window's action listener to save the session upon closure and dispose on close
    @Override
    public void setWindowSettings(JFrame artistWindow) {
        this.window = artistWindow;
        this.window.setLayout(new BorderLayout());
        this.window.setTitle("Artist Queue");
        artistWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveConcert();
                artistWindow.dispose();
            }
        });
    }

    // EFFECTS: calls helper methods to create all buttons necessary for window
    @Override
    public void createButtons() {
        createAddToSongQueue();

        createAcceptSong();

        createDenySong();

        createPlaySong();

        createGoTo();
    }

    // EFFECTS: Creates addSongToQueue JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display AddSongToQueue window
    private void createAddToSongQueue() {
        addSongToQueue = new JButton("Add song to queue");
        addSongToQueue.setFocusPainted(false);
        addSongToQueue.addActionListener(e -> {
            panelRight.removeAll();
            panelRight.revalidate();
            AddSongToQueue addSongToQueueButton
                    = new AddSongToQueue(window, panelLeft, panelRight, queue, artist, audience);
            addSongToQueueButton.show();
        });
    }

    // EFFECTS: Creates acceptSong JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display AcceptSong window
    private void createAcceptSong() {
        acceptSong = new JButton("Accept song");
        acceptSong.setFocusPainted(false);
        acceptSong.addActionListener(e -> {
            panelRight.removeAll();
            panelRight.revalidate();
            AcceptSong acceptSongButton
                    = new AcceptSong(window, panelLeft, panelRight, queue, artist, audience);
            acceptSongButton.show();
        });
    }

    // EFFECTS: Creates denySong JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display DenySong window
    private void createDenySong() {
        denySong = new JButton("Deny song");
        denySong.setFocusPainted(false);
        denySong.addActionListener(e -> {
            panelRight.removeAll();
            panelRight.revalidate();
            DenySong denySongButton
                    = new DenySong(window, panelLeft, panelRight, queue, artist, audience);
            denySongButton.show();
        });
    }

    // EFFECTS: Creates playSong JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display PlaySong window
    private void createPlaySong() {
        playSong = new JButton("Play song");
        playSong.setFocusPainted(false);
        playSong.addActionListener(e -> {
            panelRight.removeAll();
            panelRight.revalidate();
            PlaySong playSongButton
                    = new PlaySong(window, panelLeft, panelRight, queue, artist, audience);
            playSongButton.show();
        });
    }

    // EFFECTS: calls helper methods to create all tables necessary for window
    @Override
    public void createTables() {
        createSongQueue();

        createSongRequests();

        createSongsPlayed();
    }

    // EFFECTS: Creates songsPlayed JTable by passing adequate data to createTable method
    private void createSongsPlayed() {
        String[] columnNames = {"Number", "Song", "Artist"};

        songsPlayed = createTable(columnNames, artist.getPlayed(), songsPlayedData);
    }

    // MODIFIES: panelLeft
    // EFFECTS: Creates a new JPanel, panelLeft and adjusts the necessary settings using superclass' method
    //          Adds all necessary extra components to panelLeft
    @Override
    public void setPanelLeft() {
        super.setPanelLeft();

        panelLeft.add(addSongToQueue);
        panelLeft.add(acceptSong);
        panelLeft.add(denySong);
        panelLeft.add(playSong);
        panelLeft.add(goTo);
    }

    // MODIFIES: panelRight
    // EFFECTS: Creates a new JPanel, panelRight and adjusts the necessary settings using superclass' method
    //          Adds all necessary extra components to panelRight
    @Override
    public void setPanelRight() {
        super.setPanelRight();

        JScrollPane scrollPaneSongsPlayed = new JScrollPane(songsPlayed);
        scrollPaneSongsPlayed.setBorder(BorderFactory.createTitledBorder("Songs Played"));
        panelRight.add(scrollPaneSongsPlayed);
    }

    // EFFECTS: Creates goTo JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display AudienceMain window
    @Override
    protected void createGoTo() {
        goTo = new JButton("Go to audience page");
        goTo.setFocusPainted(false);
        goTo.addActionListener(e -> {
            window.remove(panelLeft);
            window.remove(panelRight);
            AudienceMain audienceMain
                    = new AudienceMain(window, queue, artist, audience);
            audienceMain.show();
        });
    }
}