package wordhack.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;

/**
 * A simple container of a small single-row grid with JLabels represented as
 * small squares.
 * <p>
 * This class extends {@link GamePanel} and is used to display a visual
 * representation of the remaining guesses in the game. Each guess is
 * represented by a coloured square, which turns opaque when a guess is used.
 * </p>
 * <p>
 * The squares are initially set to a default colour, which can be changed using
 * the {@link #setForeground(Color)} method. The number of guesses is specified
 * at the time of instantiation.
 * </p>
 * <p>
 * The layout is managed using a {@link GridLayout} with a single row and a
 * specified number of columns based on the number of guesses.
 * </p>
 *
 * @see GamePanel
 * @see javax.swing.JLabel
 *
 * @version 1.0
 * @since 2024-07-03
 *
 * @autor Kheagen Haskins
 */
public class GuessBar extends GamePanel {

    // ------------------------------ Fields -------------------------------- //
    /**
     * The default colour for the squares.
     */
    public static Color DEFAULT_COLOR = Color.GREEN;

    /**
     * The gap size between squares.
     */
    private static final int GAP_SIZE = 10;

    /**
     * The size of each square.
     */
    private static final int SQUARE_SIZE = 20;

    /**
     * An array of JLabels representing the squares.
     */
    private JLabel[] squares;

    /**
     * The cursor indicating the current guess.
     */
    private int cursor;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a GuessBar with the specified number of guesses.
     *
     * @param guesses the number of guesses to be represented by squares
     */
    GuessBar(int guesses) {
        squares = new JLabel[guesses];
        cursor = guesses - 1;

        setLayout(new GridLayout(1, guesses, GAP_SIZE, 0));
        setMaximumSize(new Dimension(
                (guesses * SQUARE_SIZE) + (GAP_SIZE * (guesses - 1)), // width
                SQUARE_SIZE // height
        ));
        initComponents();
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the number of guesses left.
     *
     * @return the number of guesses left
     */
    int getGuessesLeft() {
        return cursor + 1;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the foreground colour of the squares.
     *
     * @param color the colour to be set as the foreground
     */
    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        if (squares != null) {
            for (int i = 0; i < squares.length; i++) {
                squares[i].setBackground(color);
            }
        }
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Subtracts a guess and updates the visual representation.
     *
     * @throws IllegalStateException if there are no more guesses left
     */
    void subtractGuess() {
        if (cursor < 0) {
            throw new IllegalStateException("Cannot invoke removeGuess when there are no more guesses left");
        }

        squares[cursor--].setOpaque(false);
        repaint();
    }

    /**
     * Resets the GuessBar to the initial state with all guesses available.
     */
    void reset() {
        cursor = squares.length - 1; // Reset cursor
        // Reset each square
        for (int i = 0; i <= cursor; i++) {
            squares[i].setOpaque(true);
        }
        repaint();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises the components by creating the squares and adding them to the
     * panel.
     */
    private void initComponents() {
        for (int i = 0; i < squares.length; i++) {
            squares[i] = createSquare();
            add(squares[i]);
        }
    }

    /**
     * Creates a single square JLabel with the default settings.
     *
     * @return a JLabel representing a square
     */
    private JLabel createSquare() {
        JLabel lbl = new JLabel();
        lbl.setOpaque(true);
        lbl.setBackground(DEFAULT_COLOR);
        return lbl;
    }
}
