package ui.graphic;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.artist.ArtistMain;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;

// Abstract class with methods relevant for all windows that require user's input of song and artist
public abstract class EnterSong extends Window {
    protected JFrame window;

    protected JPanel panelLeft;
    protected JPanel panelRight;

    protected JButton enter;
    protected JButton exit;

    protected JLabel songNameLabel;
    protected JLabel songArtistLabel;

    protected JTextField textFieldSongName;
    protected JTextField textFieldSongArtist;

    protected Queue queue;

    protected Artist artist;

    protected Audience audience;

    protected LinkedList<Song> songQueue;
    protected LinkedList<Song> songRequests;
    protected LinkedList<Song> songsPlayed;

    protected String songName;
    protected String songArtist;

    // Constructor
    // EFFECTS: Creates a new instance of the EnterSong class, adjusting all required settings
    //          Assigns all passed parameters from previous window to variables
    //          Gets queue's songQueue, songRequests and artist's played and assigns all lists to variables
    //          Sets panelRight to a new GridLayout
    //          Calls helper methods to create all required components
    public EnterSong(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                     Audience audience) {
        this.window = window;

        this.panelLeft = panelLeft;
        this.panelRight = panelRight;

        this.queue = queue;
        this.artist = artist;
        this.audience = audience;

        this.songQueue = queue.getSongQueue();
        this.songRequests = queue.getSongRequests();
        this.songsPlayed = artist.getPlayed();

        panelRight.setLayout(new GridLayout(11, 1));

        createTextFields();

        createEnterButton();

        createExitButton();
    }

    // MODIFIES: panelRight
    // EFFECTS: Adds all components to panelRight in adequate order
    public void show() {
        panelRight.add(songNameLabel);
        panelRight.add(textFieldSongName);
        panelRight.add(songArtistLabel);
        panelRight.add(textFieldSongArtist);
        panelRight.add(enter);
        panelRight.add(exit);

        panelRight.repaint();
        panelRight.revalidate();

        window.revalidate();
    }

    // EFFECTS: Creates all three text fields required for the EnterSong windows and adjusts all required settings
    protected void createTextFields() {
        this.songNameLabel = new JLabel("Enter song name");
        this.songNameLabel.setHorizontalAlignment(JLabel.CENTER);

        this.textFieldSongName = new JTextField(20);

        this.songArtistLabel = new JLabel("Enter song artist");
        this.songArtistLabel.setHorizontalAlignment(JLabel.CENTER);

        this.textFieldSongArtist = new JTextField(20);
    }

    // EFFECTS: checks if the user filled out all required fields and if so, entered a valid song name and artist
    //          If didNotFillOutAllFields, calls checkLabel with appropriate message
    //          Else, calls checkIfContains
    protected void checkValid(Song s) {
        if (didNotFillOutAllFields()) {
            checkLabel(panelRight, "Please enter a valid input: fill out all fields.");
        } else {
            checkIfContains(s);
        }
    }

    // EFFECTS: returns true if songName text field and/or songArtist text field is empty
    protected boolean didNotFillOutAllFields() {
        return (Objects.equals(songName, "") || Objects.equals(songArtist, ""));
    }

    // EFFECTS: Creates exit button that returns to ArtistMain
    protected void createExitButton() {
        this.exit = new JButton("Exit");
        this.exit.addActionListener(e -> {
            window.remove(panelLeft);
            window.remove(panelRight);
            ArtistMain artistMain
                    = new ArtistMain(window, queue, artist, audience);
            artistMain.show();
        });
    }

    // MODIFIES: textFieldSongName, textFieldSongArtist, panel
    // EFFECTS: Adds a JLabel with labelText and sets all text fields to blank if doesNotContainJLabel
    @Override
    protected void checkLabel(JPanel panel, String labelText) {
        if (doesNotContainJLabel(panel, labelText)) {
            panel.add(new JLabel(labelText));
            textFieldSongName.setText("");
            textFieldSongArtist.setText("");
            panel.revalidate();
        }
    }

    // EFFECTS: Creates enter button with and adjusts all required settings
    //          ActionListener: Sets songName and songArtist to the text entered in their respective fields
    //                          Creates a new song with the songName and songArtist entered
    //                          Checks if the input is valid
    protected void createEnterButton() {
        this.enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(100, enter.getPreferredSize().height));
        this.enter.setFocusPainted(false);
        enter.addActionListener(e -> {
            removeAllLabels(panelRight);

            this.songName = textFieldSongName.getText();
            this.songArtist = textFieldSongArtist.getText();

            Song s = new Song(songName, songArtist);

            checkValid(s);
        });
    }

    // EFFECTS: returns true if a song with the same name and same artist was found in a list
    //          returns false otherwise
    protected boolean foundInList(LinkedList<Song> list, Song song) {
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

    // EFFECTS: checks if class' respective list contains Song s and takes adequate measures
    protected abstract void checkIfContains(Song s);

    // EFFECTS: completes desired action if input is a successful one
    protected abstract void success(Song s);
}
