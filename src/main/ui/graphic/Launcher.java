package ui.graphic;

import model.EventLog;

import javax.swing.*;

// Class for the program's Launcher
public class Launcher {

    // EFFECTS: Launches program by creating and showing a new MainWindow
    public static void main(String[] args) {
        EventLog.getInstance().clear();

        SwingUtilities.invokeLater(() -> {
            MainWindow main = new MainWindow();
            main.show();
        });
    }
}
