package wordhack.gui.cells;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

/**
 * The {@code Cell} interface represents a fundamental unit within the hacking
 * mini-game grid. Each cell encapsulates a single ASCII character and can
 * contain various types of content, such as alphabetical ASCII characters or
 * special symbols. This interface provides comprehensive methods to access and
 * manipulate the cell's content, appearance, and state, making it central to
 * the game's interactive mechanics.
 *
 * <p>
 * Cells are arranged in a 2D array and can either function independently or as
 * part of a cluster. They do not manage their own state; instead, this is
 * overseen by the {@code CellManager} class which instantiates the cells, and
 * the {@code CellPanel} class which manages the {@code Cell}s rendering and
 * event management. In the context of the hacking mini-game, cells form the
 * interactive grid that players engage with to uncover hidden passwords or
 * discard incorrect guesses. The game's objective involves selecting cells to
 * reveal the correct password, making the behaviour and appearance of each cell
 * crucial to the gameplay experience.
 * </p>
 *
 * <p>
 * Implementations of this interface are expected to define the visual
 * representation and interactive behaviour of a cell within the game grid. This
 * includes handling the cell's coordinates, size, content, fonts, and colours,
 * as well as its state (active or inactive). Cells may also belong to one or
 * more clusters, which are groups of cells treated as a unit within the game
 * logic. The main cluster, a special concept, refers to a primary cluster each
 * cell may belong to, with specific rules depending on whether the cell
 * contains a letter or a symbol.
 * </p>
 *
 * <p>
 * The {@code Cell} interface, along with its implementing classes such as
 * {@code AbCell}, {@code LetterCell}, and {@code SymbolCell}, forms the
 * backbone of the mini-game's grid system. It ensures a consistent API for cell
 * manipulation and state management, providing a robust foundation for the
 * game's dynamic and engaging gameplay.
 * </p>
 *
 * @see AbCell
 * @see LetterCell
 * @see SymbolCell
 * @see wordhack.gui.CellPanel
 * @see CellCluster
 * @see CellManager
 *
 * @version 3.2
 * @since   2024-06-26
 * @author  Kheagen Haskins
 */
public interface Cell {

    /**
     * The default background colour for cells. This colour is used as the
     * initial background colour for all cells unless explicitly changed.
     */
    static final Color DEFAULT_BACKGROUND = Color.GREEN;

    /**
     * The default foreground colour for cells when they are active. This colour
     * is used to display the content of the cell when it is in an active state
     * (e.g., when hovered over or selected). Should be the same colour as the
     * application background
     */
    static final Color DEFAULT_FOREGROUND_ON = Color.BLACK;

    /**
     * The default foreground colour for cells when they are inactive. This
     * colour is used to display the content of the cell when it is in an
     * inactive state.
     */
    static final Color DEFAULT_FOREGROUND_OFF = Color.GREEN;

    /**
     * The default font used for displaying the content of cells. This font is
     * used to render the cell's content unless explicitly changed.
     */
    static final Font DEFAULT_FONT = new Font("Monospace", Font.PLAIN, 18);

    // ------------------------------ Getters ------------------------------- //
    /**
     * Gets the x-coordinate of the cell within the grid.
     *
     * @return the x-coordinate of the cell.
     */
    int getX();

    /**
     * Gets the y-coordinate of the cell within the grid.
     *
     * @return the y-coordinate of the cell.
     */
    int getY();

    /**
     * Gets the width of the cell. The width is typically uniform for all cells
     * within the grid.
     *
     * @return the width of the cell.
     */
    int getWidth();

    /**
     * Gets the height of the cell. The height is typically uniform for all
     * cells within the grid.
     *
     * @return the height of the cell.
     */
    int getHeight();

    /**
     * Retrieves the row index of the cell within the grid. The row index
     * indicates the vertical position of the cell.
     *
     * @return the row index of the cell.
     */
    int getRowIndex();

    /**
     * Retrieves the column index of the cell within the grid. The column index
     * indicates the horizontal position of the cell.
     *
     * @return the column index of the cell.
     */
    int getColumnIndex();

    /**
     * Retrieves the background colour of the cell. This colour defines the
     * visual appearance of the cell's background.
     *
     * @return the current background colour of the cell.
     */
    Color getBackground();

    /**
     * Retrieves the foreground colour of the cell for when it is active. This
     * colour defines the visual appearance of the cell's content when the cell
     * is active.
     *
     * @return the foreground colour of the cell.
     */
    Color getForegroundOn();

    /**
     * Retrieves the foreground colour of the cell for when it is inactive. This
     * colour defines the visual appearance of the cell's content when the cell
     * is off.
     *
     * @return the foreground colour of the cell.
     */
    Color getForegroundOff();

    /**
     * Retrieves the content of the cell. This content can be an ASCII character
     * or a special symbol used in the mini-game.
     *
     * @return the character content of the cell.
     */
    char getContent();

    /**
     * Retrieves the font style of the cell's content.
     *
     * @return the current font style of the cell's content.
     */
    Font getFont();

    /**
     * Checks if the cell is currently active. In the context of the game, an
     * active cell is one that the mouse is currently hovered over, highlighting
     * it for potential interaction.
     *
     * @return {@code true} if the cell is active, {@code false} otherwise.
     */
    boolean isActive();

    /**
     * Checks if the cell is part of an active cluster. Active clusters might
     * have special behaviour or visual indications in the game.
     *
     * @return {@code true} if the cell is part of an active cluster,
     *         {@code false} otherwise.
     */
    boolean inActiveCluster();

    /**
     * Checks if the cell is part of a cluster.
     *
     * @return {@code true} if the cell is part of a cluster, {@code false}
     *         otherwise.
     */
    boolean inCluster();

    /**
     * Returns a list of all clusters this cell belongs to. A cluster is a group
     * of cells that are treated as a unit within the game logic.
     *
     * @return a list of {@code CellCluster} objects that this cell is part of.
     */
    List<CellCluster> getClusters();

    /**
     * Retrieves the main cluster associated with this cell.
     *
     * <p>
     * For cells belonging to letter clusters, the main cluster is the single
     * cluster the letter symbol can belong to. For cells in symbol clusters,
     * the main cluster is defined as the cluster in which this cell is the
     * first cell. If the cell does not belong to any cluster, or if it is a
     * symbol cell that is not the first cell in any of its clusters, this
     * method returns {@code null}.
     * </p>
     *
     * @return the main {@code CellCluster} this cell belongs to, or
     *         {@code null} if the cell is not part of any cluster or is a symbol cell
     *         that is not the first cell in its clusters.
     */
    CellCluster getMainCluster();

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the x and y-coordinate of the cell within the grid.
     *
     * @param x the new x-coordinate of the cell.
     * @param y the new y-coordinate of the cell.
     */
    void setLocation(int x, int y);

    /**
     * Sets the width and height of the cell. The width is typically uniform for
     * all cells within the grid.
     *
     * @param width  the new width of the cell.
     * @param height the new height of the cell.
     */
    void setSize(int width, int height);

    /**
     * Sets the content of the cell. This can be an ASCII character or any other
     * symbol used in the game.
     *
     * @param c the character to set as the content of the cell.
     */
    void setContent(char c);

    /**
     * Sets the active state of the cell. This method is used to highlight the
     * cell when the mouse hovers over it, indicating that it is selectable.
     *
     * @param newState {@code true} to activate the cell, {@code false} to
     *                 deactivate it.
     */
    void setState(boolean newState);

    /**
     * Sets the background colour of the cell. This method is used to change the
     * visual appearance of the cell's background, which can be useful for
     * indicating different states or selections in the game.
     *
     * @param colour the new background colour of the cell.
     */
    void setBackground(Color colour);

    /**
     * Sets the foreground colour of the cell for when it is active. This method
     * is used to change the visual appearance of the cell's content, which can
     * be useful for improving readability or indicating different states in the
     * game.
     *
     * @param colour the new foreground colour of the cell.
     */
    void setForegroundOn(Color colour);

    /**
     * Sets the foreground colour of the cell for when it is inactive. This
     * method is used to change the visual appearance of the cell's content,
     * which can be useful for improving readability or indicating different
     * states in the game.
     *
     * @param colour the new foreground colour of the cell.
     */
    void setForegroundOff(Color colour);

    /**
     * Sets the font style of the cell's content. This can be used to highlight
     * or distinguish certain cells.
     *
     * @param font the new font style of the cell's content.
     */
    void setFont(Font font);

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Adds the cell to a cluster. A cluster is a group of cells that are
     * treated as a unit within the game logic.
     *
     * @param cluster the {@code CellCluster} to add the cell to.
     */
    void addCluster(CellCluster cluster);

    /**
     * Removes the cell from a cluster. A cluster is a group of cells that are
     * treated as a unit within the game logic.
     *
     * @param cluster the {@code CellCluster} to remove the cell from.
     */
    void removeCluster(CellCluster cluster);

    /**
     * Determines if this cell shares at least one cluster with the specified
     * cell.
     *
     * <p>
     * This method checks if there is any cluster that both this cell and the
     * specified cell belong to. It returns {@code true} if such a cluster
     * exists and {@code false} otherwise.
     * </p>
     *
     * @param cell the cell to check for shared clusters.
     * @return {@code true} if this cell shares at least one cluster with the
     *         specified cell, {@code false} otherwise.
     */
    boolean sharesClusterWith(Cell cell);

    /**
     * Sets the state of this Cell's clusters to the given argument
     *
     * @param newState the new state of the clusters
     */
    void setClusterStateTo(boolean newState);

    /**
     * The {@code IllegalCharAddition} class is a specific type of
     * {@code IllegalArgumentException} that is thrown when an invalid character
     * is added to a cell. This exception indicates that the character being
     * added does not meet the required criteria for the cell.
     *
     * <p>
     * This class provides constructors to create an exception with a specific
     * message, a cause, or both. It is designed to be used within the context
     * of the {@code Cell} interface to enforce character validation rules.
     *
     * <p>
     * <b>Note to students:</b> This is an inner class, which means it is
     * defined within the {@code Cell} interface. An inner class is used here to
     * logically group the exception with the interface it is related to. This
     * helps keep the code organised and makes it clear that this exception is
     * specifically related to operations involving {@code Cell} objects.
     *
     * @version 1.0
     * @since   2024-07-05
     */
    public static class IllegalCharAddition extends IllegalArgumentException {

        /**
         * Constructs a new {@code IllegalCharAddition} with the specified
         * detail message.
         *
         * @param s the detail message explaining the reason for the exception.
         */
        public IllegalCharAddition(String s) {
            super(s);
        }

        /**
         * Constructs a new {@code IllegalCharAddition} with the specified
         * detail message and cause.
         *
         * @param message the detail message explaining the reason for the
         *                exception.
         * @param cause the cause of the exception.
         */
        public IllegalCharAddition(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new {@code IllegalCharAddition} with the specified
         * cause.
         *
         * @param cause the cause of the exception.
         */
        public IllegalCharAddition(Throwable cause) {
            super(cause);
        }
    }

}