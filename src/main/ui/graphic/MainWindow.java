package ui.graphic;

import com.formdev.flatlaf.FlatDarculaLaf;
import model.EventLog;
import ui.LogPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Class for the first window that shows up when the program is launched
public class MainWindow extends Window {
    private JFrame mainWindow;
    private final JPanel panel;
    private JLabel welcomeLabel;
    private JLabel viewLabel;
    private JButton artistButton;
    private JButton audienceButton;
    private ImageIcon image;

    // Constructor
    // EFFECTS: Creates a new instance of the MainWindow class
    //          sets the look and feel to FlatDarculaLaf
    //          creates a new JPanel with appropriate layout
    //          creates an image and resizes it with helper function
    //          calls helper methods to create all JLabels and JButtons necessary
    public MainWindow() {
        FlatDarculaLaf.setup();

        setWindowSettings();

        this.panel = new JPanel(new GridLayout(5, 1));

        this.image = new ImageIcon("lib/note.jpeg");

        this.image = resizeImageIcon(image);

        setFont();

        createLabels();

        createButtons();
    }

    // MODIFIES: window
    // EFFECTS: Adds all required components to window
    public void show() {
        mainWindow.add(panel);
        panel.add(new JLabel(image));
        panel.add(welcomeLabel);
        panel.add(viewLabel);
        panel.add(artistButton);
        panel.add(audienceButton);
    }

    // EFFECTS: Creates all JLabels necessary and adjusts all required settings
    public void createLabels() {
        this.welcomeLabel = new JLabel("Welcome!");
        this.welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        this.viewLabel = new JLabel("Click to select view");
        this.viewLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    // EFFECTS: Sets the necessary JFrame settings
    private void setWindowSettings() {
        mainWindow = new JFrame();
        mainWindow.setTitle("Performance Queue");
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LogPrinter lp = new LogPrinter();
                lp.printLog(EventLog.getInstance());
                mainWindow.dispose();
            }
        });
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.setSize(800, 500);
        mainWindow.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    // EFFECTS: Sets the font of JLabels and JButtons to NotoSans
    //          Sets the adequate font colour for JButtons and JLabels
    private void setFont() {
        UiFont uiFont = new UiFont();

        uiFont.loadFont();

        Font font = new Font("NotoSans", Font.PLAIN, 14);
        Font fontBold = new Font("NotoSans", Font.BOLD, 14);

        UIManager.put("Label.font", fontBold);
        UIManager.put("Label.foreground", new Color(214, 216, 218));

        UIManager.put("Button.font", font);
        UIManager.put("Button.foreground", Color.WHITE);
    }

    // EFFECTS: Calls helper methods to create all JButtons necessary
    protected void createButtons() {
        createArtistButton();

        createAudienceButton();
    }

    // EFFECTS: Creates artistButton and adjusts all required settings
    //          Sets ActionListener to redirect user to LoadWindow with artist view
    private void createArtistButton() {
        this.artistButton = new JButton("Artist");
        artistButton.setPreferredSize(new Dimension(200, artistButton.getPreferredSize().height));
        this.artistButton.setFocusPainted(false);

        artistButton.addActionListener(e -> {
            panel.removeAll();
            LoadWindow loadWindow = new LoadWindow(mainWindow, panel, "Artist");
            loadWindow.show();
        });
    }

    // EFFECTS: Creates audienceButton and adjusts all required settings
    //          Sets ActionListener to redirect user to LoadWindow with audience view
    private void createAudienceButton() {
        this.audienceButton = new JButton("Audience");
        audienceButton.setPreferredSize(new Dimension(100, audienceButton.getPreferredSize().height));
        this.audienceButton.setFocusPainted(false);

        audienceButton.addActionListener(e -> {
            panel.removeAll();
            LoadWindow loadWindow = new LoadWindow(mainWindow, panel, "Audience");
            loadWindow.show();
        });
    }

    // EFFECTS: resizes ImageIcon icon to with 150 and height 100 and returns new ImageIcon
    private ImageIcon resizeImageIcon(ImageIcon icon) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
