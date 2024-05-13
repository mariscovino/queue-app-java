package ui.graphic;

import model.Artist;
import model.Audience;
import model.EventLog;
import model.Queue;
import persistence.JsonReader;
import ui.graphic.artist.ArtistMain;
import ui.graphic.audience.AudienceMain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

// Class for window that makes user select loading or creating a new concert
public class LoadWindow extends Window {
    private final JFrame loadWindow;

    private final JPanel panel;

    private JButton loadButton;
    private JButton loadNewButton;
    private JButton enterButton;

    private JLabel actionLabel;
    private JTextField textField;

    private final String view;

    private Queue queue;
    private Artist artist;
    private Audience audience;
    private String concertName;

    protected JsonReader jsonReaderQueue;
    protected JsonReader jsonReaderAudience;
    protected JsonReader jsonReaderArtist;

    private static final String JSON_STORE_QUEUE = "./data/queue/";
    private static final String JSON_STORE_ARTIST = "./data/artist/";

    // Constructor
    // EFFECTS: Creates a new instance of the LoadWindow class
    //          Assigns all passed parameters from previous window to variables
    //          calls helper methods to adjust all required settings and create all required components
    public LoadWindow(JFrame loadWindow, JPanel panel, String view) {
        this.view = view;
        this.loadWindow = loadWindow;
        this.panel = panel;
        panel.setLayout(new GridLayout(7, 1));

        setFont();

        createLabels();

        createButtons();
    }

    // MODIFIES: window
    // EFFECTS: Adds all required components to window
    public void show() {
        panel.add(actionLabel);
        panel.add(loadButton);
        panel.add(loadNewButton);
        panel.revalidate();
    }

    // EFFECTS: Sets the font of JTextFields to NotoSans
    //          Sets the adequate font colour and background for JTextFields
    protected void setFont() {

        Font font = new Font("NotoSans", Font.PLAIN, 14);

        UIManager.put("TextField.font", font);
        UIManager.put("TextField.background", new Color(30, 31, 34, 255));
        UIManager.put("TextField.foreground", new Color(214, 216, 218));
    }

    // EFFECTS: Creates all JLabels necessary and adjusts all required settings
    public void createLabels() {
        this.actionLabel = new JLabel("Click to select action");
        this.actionLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    // EFFECTS: Calls helper methods to create all JButtons necessary
    protected void createButtons() {
        createLoadButton();

        createLoadNewButton();
    }

    // EFFECTS: Creates loadButton and adjusts all required settings
    //          Sets ActionListener to redirect user to enterConcertName "load" window type
    private void createLoadButton() {
        this.loadButton = new JButton("Load existing concert");
        loadButton.setPreferredSize(new Dimension(200, loadButton.getPreferredSize().height));
        this.loadButton.setFocusPainted(false);

        loadButton.addActionListener(e -> {
            panel.removeAll();
            enterConcertName("load");
        });
    }

    // EFFECTS: Creates loadNewButton and adjusts all required settings
    //          Sets ActionListener to redirect user to enterConcertName "new" window type
    private void createLoadNewButton() {
        this.loadNewButton = new JButton("Create a new concert");
        loadNewButton.setPreferredSize(new Dimension(200, loadNewButton.getPreferredSize().height));
        this.loadNewButton.setFocusPainted(false);

        loadNewButton.addActionListener(e -> {
            panel.removeAll();
            enterConcertName("new");
        });
    }

    // MODIFIES: panel
    // EFFECTS: Creates all required components for the enterConcertName window type
    //          Adds all component tp panel
    private void enterConcertName(String buttonType) {
        JLabel concertNameLabel = new JLabel("Enter Concert Name");
        concertNameLabel.setHorizontalAlignment(JLabel.CENTER);

        this.textField = new JTextField(20);

        createEnterButton(buttonType);

        panel.add(concertNameLabel);
        panel.add(textField);
        panel.add(enterButton);
        panel.revalidate();
    }

    // EFFECTS: Creates the enter button for enterConcertName window type and adjusts all required settings
    //          Sets ActionListener to depend on buttonType("load" or "new")
    //          If "new", calls helper method to create a new concert
    //          If "load", calls helper method to check if user trying to load a valid concert
    public void createEnterButton(String buttonType) {
        this.enterButton = new JButton("Enter");
        enterButton.setPreferredSize(new Dimension(100, enterButton.getPreferredSize().height));
        this.enterButton.setFocusPainted(false);
        enterButton.addActionListener(e -> {
            ArrayList<String> labels = new ArrayList<>();
            labels.add("Please enter a concert name.");
            labels.add("Unable to read file.");

            removeAllLabels(panel);

            concertName = textField.getText();

            switch (buttonType) {
                case "new":
                    createNew();
                    redirect(view);
                    break;
                case "load":
                    checkValid(concertName);
                    break;
            }
        });
    }

    // EFFECTS: checks if user input of concertName is valid
    //          If concertName is empty, calls checkLabel with appropriate message
    //          Else, calls loadConcert method and redirects
    //          If exception is thrown, calls checkLabel with appropriate message
    private void checkValid(String concertName) {
        if (Objects.equals(concertName, "")) {
            checkLabel(panel, "Please enter a concert name.");
        } else {
            try {
                loadConcert();
                EventLog.getInstance().clear();
                redirect(view);
            } catch (IOException ex) {
                checkLabel(panel, "Unable to read file.");
            }
        }
    }

    // REQUIRES: a unique concertName
    // EFFECTS: Creates a new queue, artist and audience from user input
    public void createNew() {
        queue = new Queue();
        queue.setConcertName(this.concertName);

        audience = new Audience(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                queue.getConcertName());
        artist = new Artist(queue.getSongQueue(), queue.getSongRequests(), queue.getFeedbacks(),
                queue.getConcertName());
    }

    // EFFECTS: loads a concert with file name concertName
    private void loadConcert() throws IOException {
        jsonReaderQueue = new JsonReader(JSON_STORE_QUEUE + concertName);
        jsonReaderAudience = new JsonReader(JSON_STORE_QUEUE + concertName);
        jsonReaderArtist = new JsonReader(JSON_STORE_ARTIST + concertName);

        queue = jsonReaderQueue.readQueue();

        artist = jsonReaderArtist.readArtist(queue);

        audience = jsonReaderAudience.readAudience(queue);
    }

    // MODIFIES: window
    // EFFECTS: redirects user to ArtistMain or AudienceMain depending on view selected in MainWindow
    private void redirect(String view) {
        switch (view) {
            case "Artist":
                loadWindow.remove(panel);
                ArtistMain artistMain
                        = new ArtistMain(loadWindow, queue, artist, audience);
                artistMain.show();
                break;
            case "Audience":
                loadWindow.remove(panel);
                AudienceMain audienceMain
                        = new AudienceMain(loadWindow, queue, artist, audience);
                audienceMain.show();
                break;
        }
    }
}
