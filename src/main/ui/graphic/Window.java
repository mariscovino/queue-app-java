package ui.graphic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Class with methods relevant for all windows, except ArtistMain and AudienceMain
public class Window {

    // MODIFIES: panel
    // EFFECTS: removes all possible labels in panel
    protected void removeAllLabels(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                ArrayList<String> labels = new ArrayList<>();
                labels.add("Enter Concert Name");
                labels.add("Enter song name");
                labels.add("Enter song artist");
                labels.add("Enter feedback");

                if (!labels.contains(((JLabel) component).getText())) {
                    panel.remove(component);
                    panel.revalidate();
                }
            }
        }
    }

    // MODIFIES: panel
    // EFFECTS: adds a new JLabel with labelText to panel, if it does not contain a JLabel with labelText
    protected void checkLabel(JPanel panel, String labelText) {
        if (doesNotContainJLabel(panel, labelText)) {
            panel.add(new JLabel(labelText));
            panel.revalidate();
        }
    }

    // EFFECTS: returns true if panel does not contain a JLabel with labelText
    protected boolean doesNotContainJLabel(JPanel panel, String labelText) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getText().equals(labelText)) {
                    return false;
                }
            }
        }
        return true;
    }
}
