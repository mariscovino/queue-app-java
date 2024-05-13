package ui.graphic.artist;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import ui.graphic.EnterSong;

import javax.swing.*;
import java.util.Objects;

// Class for window shown when an artist denies song requests
public class DenySong extends EnterSong {
    private JTextField textFieldFeedback;

    // Constructor
    // EFFECTS: creates a new instance of the DenySong class with the superclass' constructor
    public DenySong(JFrame window, JPanel panelLeft, JPanel panelRight, Queue queue, Artist artist,
                    Audience audience) {
        super(window, panelLeft, panelRight, queue, artist, audience);
    }

    // EFFECTS: Shows all components in the right panel by adding them to it
    //          Uses show from the superclass to add all components
    //          Calls createFeedbackTextField method to add feedback related components
    //          Removes and re-adds enter and exit buttons to change their order on panel
    @Override
    public void show() {
        super.show();
        createFeedbackTextField();
        panelRight.remove(enter);
        panelRight.remove(exit);
        panelRight.add(enter);
        panelRight.add(exit);
        panelRight.revalidate();
    }

    // EFFECTS: checks if the user filled out all required fields and if so, entered a valid song name and artist
    //          If didNotFillOutAllFields, calls checkLabel with appropriate message
    //          Else, calls checkIfContains
    @Override
    protected void checkValid(Song s) {
        if (didNotFillOutAllFields()) {
            checkLabel(panelRight, "Please enter a valid input: fill out all fields.");
        } else {
            checkIfContains(s);
        }
    }

    // EFFECTS: returns true if songName text field and/or songArtist text field and/or feedback text field is empty
    @Override
    protected boolean didNotFillOutAllFields() {
        return (Objects.equals(songName, "") || Objects.equals(songArtist, "")
                || Objects.equals(textFieldFeedback.getText(), ""));
    }

    // MODIFIES: textFieldSongName, textFieldSongArtist, textFieldFeedback, panel
    // EFFECTS: Adds a JLabel with labelText and sets all text fields to blank if doesNotContainJLabel
    @Override
    protected void checkLabel(JPanel panel, String labelText) {
        if (doesNotContainJLabel(panel, labelText)) {
            panel.add(new JLabel(labelText));
            textFieldSongName.setText("");
            textFieldSongArtist.setText("");
            textFieldFeedback.setText("");
            panel.revalidate();
        }
    }

    // EFFECTS: If songRequests is not empty, checks if songRequests contains Song s
    //          If foundInList, calls success method
    //          If songRequests does not contain Song s, calls checkLabel with the appropriate message
    //          If songRequests is empty, calls checkLabel with the appropriate message
    @Override
    protected void checkIfContains(Song s) {
        if (!songRequests.isEmpty()) {
            if (foundInList(songRequests, s)) {
                success(s);
            }

            checkLabel(panelRight, "Song cannot be denied: song not found.");
        } else {
            checkLabel(panelRight, "Cannot deny songs: there are no requests.");
        }
    }

    // MODIFIES: songRequests, feedbacks
    // EFFECTS: makes artist deny Song s and calls checkLabel method with the appropriate message
    @Override
    protected void success(Song s) {
        artist.denySong(s, textFieldFeedback.getText());

        checkLabel(panelRight, "The song was denied.");
    }

    // MODIFIES: panelRight
    // EFFECTS: Creates extra text field, textFieldFeedback, adjusts all required settings and adds it to panelRight
    private void createFeedbackTextField() {
        JLabel feedbackLabel = new JLabel("Enter feedback");
        feedbackLabel.setHorizontalAlignment(JLabel.CENTER);

        this.textFieldFeedback = new JTextField(20);

        panelRight.add(feedbackLabel);
        panelRight.add(textFieldFeedback);
    }
}
