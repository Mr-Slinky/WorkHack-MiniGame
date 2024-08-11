package wordhack.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JPanel;

/**
 *
 * @author Kheagen
 */
public class GamePanel extends JPanel {

    // --------------------------- Constructors ----------------------------- //
    public GamePanel() {
        super(true);
        setOpaque(false);
    }

    // ------------------------------ Setters ------------------------------- //
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        for (Component comp : getComponents()) {
            comp.setForeground(fg);
        }
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        for (Component comp : getComponents()) {
            comp.setFont(font);
        }
    }

}
