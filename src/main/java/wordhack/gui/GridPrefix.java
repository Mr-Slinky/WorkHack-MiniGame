package wordhack.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The {@code GridPrefix} class extends {@link javax.swing.JLabel} and is used
 * to create a column of {@link javax.swing.JLabel} components, each displaying
 * a unique hexadecimal value.
 * <p>
 * This class uses a {@link java.awt.GridLayout} to arrange the labels in a
 * single column. The initial hexadecimal value is randomly generated within a
 * specific range to ensure a 4-digit hex value.
 * </p>
 * <p>
 * The class is designed to dynamically adjust the maximum size of the column
 * based on the width of the hexadecimal text and its corresponding font.
 * </p>
 *
 *
 * @version 1.0
 * @since 2024-06-28
 * @author Kheagen Haskins
 * @see GameGrid
 */
final class GridPrefix extends JPanel {

    /**
     * A template string representing the maximum width of the hexadecimal
     * value.
     */
    private static final String JLABEL_TEMPLATE_TEXT = "0xFFFF";

    // ------------------------------ Fields -------------------------------- //
    /**
     * The current hexadecimal value to be displayed.
     */
    private int hexValue;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code PrefixColumn} with the specified number of rows
     * and a starting hexadecimal value.
     * <p>
     * This constructor initializes the column with the provided starting
     * hexadecimal value, sets up the layout and maximum size of the panel, and
     * calls {@code initComponents} to populate the column with labels.
     * </p>
     *
     * @param rows the number of rows (labels) to be added to the column.
     * @param startingHexValue the initial hexadecimal value to be displayed.
     */
    GridPrefix(int rows, int startingHexValue) {
        super(new GridLayout(rows, 1, 0, 0), true);
        hexValue = startingHexValue;

        setOpaque(false);
        setMaximumSize(new Dimension(getTextWidth(), Integer.MAX_VALUE));
        initComponents(rows);
    }

    /**
     * Constructs a new {@code PrefixColumn} with the specified number of rows.
     * <p>
     * This constructor initializes the column with a random starting
     * hexadecimal value within a specific range and sets up the layout and
     * maximum size of the panel. It then calls {@code initComponents} to
     * populate the column with labels.
     * </p>
     *
     * @param rows the number of rows (labels) to be added to the column.
     */
    GridPrefix(int rows) {
        this(rows, current().nextInt(pow16(3), pow16(3.5f)));
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * The last / current hex value, in base 10 notation.
     *
     * @return The current hex value in base 10 notation.
     */
    public int getLastHexValue() {
        return hexValue;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the font of the panel and updates the maximum size based on the new
     * font.
     * <p>
     * This method overrides {@link javax.swing.JComponent#setFont(Font)} to
     * ensure that the maximum size of the column is adjusted according to the
     * width of the hexadecimal text in the new font.
     * </p>
     *
     * @param font the new font to be set.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        for (Component comp : getComponents()) {
            comp.setFont(font);
        }
        setMaximumSize(new Dimension(getTextWidth(), Integer.MAX_VALUE));
        setMinimumSize(new Dimension(getTextWidth(), font.getSize()));
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        for (Component comp : getComponents()) {
            comp.setForeground(fg);
        }
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initializes the components of the panel with the specified number of
     * rows.
     * <p>
     * This method creates a {@link javax.swing.JLabel} for each row, displaying
     * a unique hexadecimal value, and adds it to the panel.
     * </p>
     *
     * @param rows the number of rows (labels) to add to the panel.
     */
    private void initComponents(int rows) {
        for (int i = 0; i < rows; i++) {
            add(getNextJLabel(getHexValueAsString()));
            hexValue++;
        }
    }

    /**
     * Factory method to create and configure a JLabel.
     *
     * @param text The text to instantiate the JLabel with
     * @return A uniformly configured JLabel
     */
    private JLabel getNextJLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setVerticalAlignment(JLabel.CENTER);
        return lbl;
    }

    /**
     * Converts the current hexadecimal value to a string.
     *
     * @return the hexadecimal string representation of the current value.
     */
    private String getHexValueAsString() {
        return "0x" + Integer.toHexString(hexValue).toUpperCase();
    }

    /**
     * Calculates the width of the template text using the current font metrics.
     *
     * @return the width of the template text.
     */
    private int getTextWidth() {
        return getFontMetrics(getFont()).stringWidth(JLABEL_TEMPLATE_TEXT);
    }

    /**
     * Calculates 16 raised to the power of the given exponent.
     *
     * @param exp the exponent.
     * @return the result of 16 raised to the power of the exponent.
     */
    private static int pow16(float exp) {
        return (int) Math.pow(16, exp);
    }

}
