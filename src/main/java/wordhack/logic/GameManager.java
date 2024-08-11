package wordhack.logic;

import wordhack.InputHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import wordhack.gui.CellPanel;
import wordhack.gui.GameGrid;
import wordhack.gui.GameHeader;
import wordhack.gui.GamePanel;
import static wordhack.InputHandler.*;
import static wordhack.gui.GameHeader.*;

/**
 * The {@code GameManager} class is responsible for managing the core logic and
 * user interactions within the WordHack mini-game. It initializes the game
 * components, handles user guesses, and updates the game state accordingly.
 *
 * <p>
 * This class orchestrates the main gameplay elements, including the grid of
 * cells, the game header, and the main display panel. It uses a {@code WordSet}
 * to manage the set of words used in the game and interacts with the
 * {@code InputHandler} to process user input and manage game state
 * transitions.</p>
 *
 * <p>
 * The {@code GameManager} class ensures the game flow by handling user guesses,
 * updating the display, and managing the remaining guesses. It also removes
 * incorrect words (duds) from the game grid and resets guesses as needed.</p>
 *
 * @version 2.0
 * @since   2024-07-04
 * @author  Kheagen Haskins
 */
public class GameManager {

    // ------------------------------ Fields -------------------------------- //
    /**
     * The initial number of guesses the player has at the start of the game.
     */
    private static final int STARTING_GUESSES = 4;

    /**
     * The set of words used in the game.
     */
    private WordSet wordSet;

    /**
     * The game grid containing the cells.
     */
    private GameGrid gameGrid;

    /**
     * The game header displaying the current game status.
     */
    private GameHeader header;

    /**
     * The main display panel containing all game components.
     */
    private JPanel mainDisplay;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code GameManager} with the specified number of rows
     * and columns. Initializes the game components and sets up the game grid
     * and header.
     *
     * @param rows the number of rows in the game grid.
     * @param cols the number of columns in the game grid.
     */
    public GameManager(int rows, int cols) {
        int totalRows = rows * CellPanel.count();
        this.wordSet = WordBank.getWordList(Difficulty.MASTER);
        InputHandler.correctWord = wordSet.getCorrectWord();
        InputHandler.guesses = STARTING_GUESSES;

        initGUIComponents(totalRows, cols, wordSet.jumble(totalRows * cols).toCharArray());
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Retrieves the main game display panel.
     *
     * @return the main game display panel.
     */
    public JPanel getMainGameDisplay() {
        return mainDisplay;
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Handles the user's guess by processing the input text, updating the game
     * state, and managing the display and remaining guesses.
     *
     * <p>
     * This method processes the user's guess, updates the game grid, and
     * manages the number of remaining guesses. It also handles the removal of
     * incorrect words (duds) and updates the game header as needed.</p>
     *
     * @param text the text of the user's guess.
     */
    public void handleUserGuess(String text) {
        int guesses = InputHandler.guesses;
        // Reset InputHandler flags
        InputHandler.removeDud = false;
        InputHandler.resetGuesses = false;

        processTextSelected(text); // should manipulate guesses

        if (InputHandler.removeDud) {
            if (gameGrid.removeDud(correctWord) == null) {
                InputHandler.resetGuesses = true;
            }

            gameGrid.repaint();
        }

        if (InputHandler.resetGuesses) {
            header.update(UPDATE_RESET_GUESSES);
        }

        if (guesses > InputHandler.guesses) {
            header.update(UPDATE_SUBTRACT_GUESS);
        }

    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initializes the graphical user interface components, including the game
     * grid, game header, and main display panel.
     *
     * <p>
     * This method sets up the game grid and header, and adds them to the main
     * display panel. It also configures the visual properties of the main
     * display panel.</p>
     *
     * @param totalRows the total number of rows in the game grid.
     * @param cols the number of columns in the game grid.
     * @param characters the array of characters to initialize the game grid.
     */
    private void initGUIComponents(int totalRows, int cols, char[] characters) {
        gameGrid = new GameGrid(totalRows, cols, characters);
        gameGrid.setObserver(this); // Leaking 'this' - IT'S FINE!
        header = new GameHeader(STARTING_GUESSES);

        // Must be last call in method constructor
        mainDisplay = new GamePanel();
        mainDisplay.setLayout(new BorderLayout());

        mainDisplay.add(header, BorderLayout.NORTH);
        mainDisplay.add(gameGrid, BorderLayout.CENTER);

        // Must call after adding components so that these changes propagate through
        mainDisplay.setForeground(new Color(0x44FF44));
        mainDisplay.setFont(new Font("Courier Prime", Font.PLAIN, 16));
    }
}