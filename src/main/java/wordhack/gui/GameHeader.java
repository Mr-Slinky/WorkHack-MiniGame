package wordhack.gui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * A simple JPanel that contains the game's heading information, which includes
 * arbitrary company names and a dynamic JPanel class that displays the number
 * of guesses left to the user.
 * <p>
 * This class extends {@link GamePanel} and serves as the header of the game,
 * displaying important information such as the company name, a prompt to enter
 * a password, and a visual representation of the remaining guesses.
 * </p>
 * <p>
 * The layout is managed using {@link BoxLayout} with two main sections: the top
 * section displaying static text and the bottom section displaying the
 * remaining guesses and a label showing the number of attempts left.
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
public class GameHeader extends GamePanel {

    // ------------------------------ Fields -------------------------------- //
    /**
     * Update code indicating no action.
     */
    public static final int UPDATE_DO_NOTHING = 0;

    /**
     * Update code indicating a guess should be subtracted.
     */
    public static final int UPDATE_SUBTRACT_GUESS = 1;

    /**
     * Update code indicating guesses should be reset.
     */
    public static final int UPDATE_RESET_GUESSES = 2;

    /**
     * The default number of guesses.
     */
    private static final int DEFAULT_GUESS_AMOUNT = 4;

    /**
     * The margin size for the border.
     */
    private static final int MARGIN = 5;

    /**
     * The GuessBar component displaying the remaining guesses.
     */
    private GuessBar guessBar;

    /**
     * The top half panel containing static text.
     */
    private GamePanel topHalf;

    /**
     * The bottom half panel containing the guess bar and attempts label.
     */
    private GamePanel bottomHalf;

    /**
     * The label displaying the number of attempts left.
     */
    private JLabel attemptsLabel;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a GameHeader with the specified starting number of guesses.
     *
     * @param startingGuessAmount the initial number of guesses
     */
    public GameHeader(int startingGuessAmount) {
        guessBar = new GuessBar(startingGuessAmount);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        initComponents();
    }

    /**
     * Constructs a GameHeader with the default number of guesses.
     */
    public GameHeader() {
        this(DEFAULT_GUESS_AMOUNT);
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Updates the GameHeader based on the specified update code.
     *
     * @param updateCode the code indicating the update action
     * @throws IllegalArgumentException if the update code is invalid
     */
    public void update(int updateCode) {
        switch (updateCode) {
            case UPDATE_DO_NOTHING:     // Do nothing
                break;
            case UPDATE_SUBTRACT_GUESS: // Subtract a guess
                guessBar.subtractGuess();
                attemptsLabel.setText(getAttemptsString());
                break;
            case UPDATE_RESET_GUESSES:  // Reset guesses 
                guessBar.reset();
                attemptsLabel.setText(getAttemptsString());
                break;
            default:
                throw new IllegalArgumentException("Invalid update code: " + updateCode);
        }
        repaint();
    }

    /**
     * Returns the number of guesses remaining.
     *
     * @return the number of guesses remaining
     */
    public int getGuessesRemaining() {
        return guessBar.getGuessesLeft();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initializes the components of the GameHeader.
     */
    private void initComponents() {
        topHalf = getSection(BoxLayout.Y_AXIS);
        bottomHalf = getSection(BoxLayout.X_AXIS);

        attemptsLabel = createJLabel(getAttemptsString());

        topHalf.add(createJLabel("ROBCO INDUSTRIES (TM) TERMLINK PROTOCOL"));
        topHalf.add(createJLabel("Enter Password"));
        topHalf.add(Box.createVerticalStrut(10)); // Arbitrary spacing

        bottomHalf.add(attemptsLabel);
        bottomHalf.add(Box.createHorizontalStrut(10));
        bottomHalf.add(guessBar);

        topHalf.setAlignmentX(0);
        bottomHalf.setAlignmentX(0);

        add(topHalf);
        add(bottomHalf);
    }

    /**
     * Creates a section panel with the specified layout direction.
     *
     * @param direction the layout direction for the section
     * @return a GamePanel configured with the specified layout direction
     */
    private GamePanel getSection(int direction) {
        GamePanel panel = new GamePanel();
        panel.setLayout(new BoxLayout(panel, direction));
        return panel;
    }

    /**
     * Creates a JLabel with the specified text.
     *
     * @param text the text to be displayed on the label
     * @return a JLabel configured with the specified text
     */
    private JLabel createJLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setVerticalAlignment(JLabel.CENTER);
        lbl.setHorizontalAlignment(JLabel.LEFT);
        return lbl;
    }

    /**
     * Returns a string representing the number of attempts left.
     *
     * @return a string indicating the number of attempts left
     */
    private String getAttemptsString() {
        return guessBar.getGuessesLeft() + " Attempt(s) Left:";
    }
}
