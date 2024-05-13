package ui.graphic;

import java.awt.*;
import java.io.File;

// Class for the UI's font
public class UiFont {

    // EFFECTS: loads NotoSans font from directory and creates a bold font
    public void loadFont() {
        try {
            File fontFileRegular = new File(".idea/libraries/NotoSans-Regular.ttf");
            Font fontRegular = Font.createFont(Font.TRUETYPE_FONT, fontFileRegular);


            Font fontBold = fontRegular.deriveFont(Font.BOLD, 16);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fontRegular);
            ge.registerFont(fontBold);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
