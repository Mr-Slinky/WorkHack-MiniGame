package wordhack.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The main panel that composites all the GUI elements together.
 *
 * @author Kheagen Haskins
 */
public class AnimationPanel extends JPanel {

    /**
     * Main constructor. All other constructors call this one.
     *
     * @param width the width of the Panel
     * @param height the height of the Panel
     */
    public AnimationPanel(int width, int height) {
        super(new BorderLayout(), true);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }
    
}