package ui.graphic.audience;

import model.Artist;
import model.Audience;
import model.EventLog;
import model.Queue;
import ui.LogPrinter;
import ui.graphic.QueueWindows;
import ui.graphic.artist.ArtistMain;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Class for Audience's main window
public class AudienceMain extends QueueWindows {
    private JButton requestSong;
    private JButton goTo;

    private JTable feedbacks;

    // Constructor
    // EFFECTS: Creates a new instance of the AudienceMain class with the superclass' constructor
    public AudienceMain(JFrame audienceWindow, Queue queue, Artist artist, Audience audience) {
        super(audienceWindow, queue, artist, audience);
    }

    // EFFECTS: Sets artistWindow parameter to window variable
    //          Sets window's layout to BorderLayout
    //          Sets window's title to adequate title
    //          Sets window's action listener to save the session upon closure and dispose on close
    @Override
    public void setWindowSettings(JFrame audienceWindow) {
        this.window = audienceWindow;
        this.window.setTitle("Audience Queue");
        audienceWindow.setLayout(new BorderLayout());
        audienceWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveConcert();
                audienceWindow.dispose();
            }
        });
    }

    // EFFECTS: calls helper methods to create all buttons necessary for window
    @Override
    public void createButtons() {
        createRequestSong();

        createGoTo();
    }

    // EFFECTS: Creates requestSong JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display RequestSong window
    private void createRequestSong() {
        requestSong = new JButton("Request Song");
        requestSong.setFocusPainted(false);
        requestSong.addActionListener(e -> {
            panelRight.removeAll();
            panelRight.revalidate();
            RequestSong requestSongButton
                    = new RequestSong(window, panelLeft, panelRight, queue, artist, audience);
            requestSongButton.show();
        });
    }

    // EFFECTS: calls helper methods to create all tables necessary for window
    @Override
    public void createTables() {
        createSongQueue();

        createSongRequests();

        createFeedbacks();
    }

    // EFFECTS: Creates feedbacks JTable
    //          Assigns JTable's column names to columnNames
    //          If feedbacks is not empty, adds information to feedbacksData in adequate order
    //          Creates a new tableModel with columNames and 0 rows and adds rows to new tableModel with feedbacksData
    //          Else, if list is empty, creates a new tableModel with colNames and 0 rows
    //          Creates a feedbacks JTable with adequate tableModel
    private void createFeedbacks() {
        String[] columnNames = {"Number", "Song", "Artist", "Feedback"};

        DefaultTableModel defaultTableModelFeedbacks;
        if (!queue.getFeedbacks().isEmpty()) {
            for (int i = 0; i < queue.getFeedbacks().size(); i++) {
                feedbacksData.add(new String[]{Integer.toString(i + 1),
                        queue.getFeedbacks().get(i).getSong().getName(),
                        queue.getFeedbacks().get(i).getSong().getArtist(),
                        queue.getFeedbacks().get(i).getFeedback()});
            }

            defaultTableModelFeedbacks = new DefaultTableModel(columnNames, 0);
            for (String[] data : feedbacksData) {
                defaultTableModelFeedbacks.addRow(data);
            }
        } else {
            defaultTableModelFeedbacks = new DefaultTableModel(columnNames, 0);
        }

        feedbacks = new JTable(defaultTableModelFeedbacks);
    }

    // MODIFIES: panelLeft
    // EFFECTS: Creates a new JPanel, panelLeft and adjusts the necessary settings using superclass' method
    //          Adds all necessary extra components to panelLeft
    @Override
    public void setPanelLeft() {
        super.setPanelLeft();

        panelLeft.add(requestSong);
        panelLeft.add(goTo);
    }

    // MODIFIES: panelRight
    // EFFECTS: Creates a new JPanel, panelRight and adjusts the necessary settings using superclass' method
    //          Adds all necessary extra components to panelRight
    @Override
    public void setPanelRight() {
        super.setPanelRight();

        JScrollPane scrollPaneFeedbacks = new JScrollPane(feedbacks);
        scrollPaneFeedbacks.setBorder(BorderFactory.createTitledBorder("Feedbacks"));
        panelRight.add(scrollPaneFeedbacks);
    }

    // EFFECTS: Creates goTo JButton and adds adequate text
    //          Sets all necessary button settings
    //          Sets ActionListener to display ArtistMain window
    @Override
    protected void createGoTo() {
        goTo = new JButton("Go to artist page");
        goTo.setFocusPainted(false);
        goTo.addActionListener(e -> {
            window.remove(panelLeft);
            window.remove(panelRight);
            ArtistMain artistMain
                    = new ArtistMain(window, queue, artist, audience);
            artistMain.show();
        });
    }
}
