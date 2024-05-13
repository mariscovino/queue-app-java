package ui.graphic;

import model.Artist;
import model.Audience;
import model.Queue;
import model.Song;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

// Abstract class with methods relevant for both ArtistMain and AudienceMain windows
public abstract class QueueWindows {
    protected JFrame window;

    protected JPanel panelLeft;
    protected JPanel panelRight;

    protected JTable songQueue;
    protected JTable songRequests;

    protected Queue queue;
    protected Artist artist;
    protected Audience audience;

    protected ArrayList<String[]> songQueueData;
    protected ArrayList<String[]> songRequestsData;
    protected ArrayList<String[]> songsPlayedData;
    protected ArrayList<String[]> feedbacksData;

    protected JsonWriter jsonWriterQueue;
    protected JsonWriter jsonWriterArtist;
    protected static final String JSON_STORE_QUEUE = "./data/queue/";
    protected static final String JSON_STORE_ARTIST = "./data/artist/";

    // Constructor
    // EFFECTS: Creates a new instance of the QueueWindows class
    //          Assigns all passed parameters from previous window to variables
    //          calls helper methods to adjust all required settings and create all required components
    public QueueWindows(JFrame window, Queue queue, Artist artist, Audience audience) {
        this.queue = queue;
        this.artist = artist;
        this.audience = audience;

        this.songQueueData = new ArrayList<>();
        this.songRequestsData = new ArrayList<>();
        this.songsPlayedData = new ArrayList<>();
        this.feedbacksData = new ArrayList<>();

        setWindowSettings(window);

        setFont();

        createButtons();

        createTables();

        setPanelLeft();

        setPanelRight();
    }

    // MODIFIES: window
    // EFFECTS: Adds panelLeft and panelRight to window in adequate order and with adequate layout
    public void show() {
        window.add(panelLeft, BorderLayout.WEST);

        window.add(panelRight, BorderLayout.CENTER);

        window.revalidate();

        panelRight.repaint();
        panelRight.revalidate();

        panelLeft.repaint();
        panelLeft.revalidate();
    }

    // EFFECTS: Sets the font of borders and tables to NotoSans and sets the table background to the adequate color
    private void setFont() {
        Font font = new Font("NotoSans", Font.PLAIN, 14);

        UIManager.put("Border.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("Table.background", new Color(30, 31, 34, 255));
    }

    // EFFECTS: Creates a JTable with column names columnNames and data
    //          If list is not empty, adds information to data in adequate order
    //          Creates a new tableModel with colNames and 0 rows and adds rows to new tableModel with data
    //          Else, if list is empty, creates a new tableModel with colNames and 0 rows
    //          Creates a new JTable with adequate tableModel
    protected JTable createTable(String[] columnNames, LinkedList<Song> list, ArrayList<String[]> data) {
        DefaultTableModel tableModel;

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                data.add(new String[]{Integer.toString(i + 1),
                        list.get(i).getName(),
                        list.get(i).getArtist()});
            }

            tableModel = new DefaultTableModel(columnNames, 0);
            for (String[] listData : data) {
                tableModel.addRow(listData);
            }
        } else {
            tableModel = new DefaultTableModel(columnNames, 0);
        }
        return new JTable(tableModel);
    }

    // EFFECTS: Creates songQueue JTable by passing adequate data to createTable method
    protected void createSongQueue() {
        String[] columnNames = {"Number", "Song", "Artist"};

        songQueue = createTable(columnNames, queue.getSongQueue(), songQueueData);
    }

    // EFFECTS: Creates songRequests JTable by passing adequate data to createTable method
    protected void createSongRequests() {
        String[] columnNames = {"Number", "Song", "Artist"};

        songRequests = createTable(columnNames, queue.getSongRequests(), songRequestsData);
    }

    // MODIFIES: panelLeft
    // EFFECTS: Creates a new JPanel, panelLeft
    //          Adjusts the necessary settings
    //          Adds JLabel with adequate description and settings
    public void setPanelLeft() {
        this.panelLeft = new JPanel();
        panelLeft.setLayout(new GridLayout(7, 1));

        JLabel panelLabel = new JLabel("Select Action");
        panelLabel.setHorizontalAlignment(JLabel.CENTER);

        panelLeft.add(panelLabel);
    }

    // MODIFIES: panelRight
    // EFFECTS: Creates a new JPanel, panelRight
    //          Adjusts the necessary settings
    //          Adds songQueue and songRequests tables with adequate border descriptions
    public void setPanelRight() {
        this.panelRight = new JPanel();
        panelRight.setLayout(new GridLayout(1, 3));

        JScrollPane scrollPaneSongQueue = new JScrollPane(songQueue);
        scrollPaneSongQueue.setBorder(BorderFactory.createTitledBorder("Song Queue"));
        panelRight.add(scrollPaneSongQueue);

        JScrollPane scrollPaneSongRequests = new JScrollPane(songRequests);
        scrollPaneSongRequests.setBorder(BorderFactory.createTitledBorder("Song Requests"));
        panelRight.add(scrollPaneSongRequests);
    }

    // EFFECTS: Saves concert with file name concertName
    protected void saveConcert() {
        jsonWriterQueue = new JsonWriter(JSON_STORE_QUEUE + queue.getConcertName());
        jsonWriterArtist = new JsonWriter(JSON_STORE_ARTIST + artist.getConcertName());

        try {
            jsonWriterQueue.open();
            jsonWriterQueue.writeQueue(queue);
            jsonWriterQueue.close();

            jsonWriterArtist.open();
            jsonWriterArtist.writeArtist(artist);
            jsonWriterArtist.close();

            System.out.println("Saved " + queue.getConcertName() + " to " + JSON_STORE_QUEUE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_QUEUE);
        }
    }

    // EFFECTS: Sets the necessary JFrame settings
    protected abstract void setWindowSettings(JFrame window);

    // EFFECTS: Creates all buttons necessary for window
    protected abstract void createButtons();

    // EFFECTS: Creates all table necessary for window
    protected abstract void createTables();

    // EFFECTS: Creates button that will either go to ArtistMain or AudienceMain
    protected abstract void createGoTo();
}
