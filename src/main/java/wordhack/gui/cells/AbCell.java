package wordhack.gui.cells;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import wordhack.logic.GameUtility;

/**
 * The {@code AbCell} class provides a skeletal implementation of the
 * {@code Cell} interface, designed to minimise the effort required to implement
 * this interface in the hacking mini-game.
 *
 * <p>
 * This abstract class manages the basic active state of the cell and provides
 * default implementations for retrieving and setting the cell content. It also
 * handles visual updates related to the active state, making it easier for
 * subclasses to focus on game-specific behaviour without duplicating common
 * functionality.
 * </p>
 *
 * <p>
 * The class is intended to be extended by specific types of cells within the
 * hacking mini-game, such as {@code LetterCell} and {@code SymbolCell}.
 * Subclasses should provide concrete implementations for any additional
 * behaviour or properties they require. This approach promotes the DRY (Don't
 * Repeat Yourself) principle by centralising common logic and ensuring
 * consistency across different cell types.
 * </p>
 *
 * <p>
 * Moreover, {@code AbCell} integrates seamlessly with the {@code CellManager}
 * class, which handles the state and interactions of the cells. This design
 * ensures that validation and state management are efficiently coordinated,
 * reducing redundancy and potential errors.
 * </p>
 *
 * <p>
 * By providing a robust and reusable foundation, {@code AbCell} allows
 * developers to implement new cell types with minimal boilerplate code,
 * facilitating easier maintenance and extension of the game's functionality.
 * </p>
 *
 * @see Cell
 * @see LetterCell
 * @see SymbolCell
 * @see wordhack.gui.CellPanel
 * @see CellCluster
 * @see CellManager
 * @see javax.swing.JLabel
 *
 * @version 3.2
 * @since   2024-06-27
 * @author  Kheagen Haskins
 */
abstract class AbCell extends JLabel implements Cell {

    // ------------------------------ Fields -------------------------------- //
    /**
     * The vertical index of the Cell within the grid.
     */
    private int rowIndex;

    /**
     * The horizontal index of the Cell within the grid.
     */
    private int columnIndex;

    /**
     * The active state of the cell, indicating if it is currently highlighted
     * or interactable.
     */
    private boolean active;

    /**
     * The content of the cell, represented as a single ASCII character.
     */
    private char content;

    /**
     * The list of clusters that this cell belongs to.
     */
    private List<CellCluster> clusters;

    /**
     * The foreground colour of the cell when it is active.
     */
    private Color foregroundOn;

    /**
     * The foreground colour of the cell when it is inactive.
     */
    private Color foregroundOff;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code AbCell} with the specified content and
     * dimensions. This constructor validates the content to ensure it is a
     * valid ASCII character within the range 33 to 127. It then sets the cell's
     * content, position, dimensions, and default visual properties. Does not
     * accept negative integers for any of the integer parameters.
     *
     * @param rowIndex    the vertical position of the Cell in a grid.
     * @param columnIndex the horizontal position of of the Cell in a grid.
     * @param content     the character content of the cell; must be a valid ASCII
     *                    character between 33 and 127.
     *
     * @throws IllegalArgumentException if the content is not a valid ASCII
     *                                  character.
     */
    AbCell(int rowIndex, int columnIndex, char content) {
        super(String.valueOf(content));
        this.active = false;
        this.clusters = new ArrayList<>();
        this.foregroundOn = Cell.DEFAULT_FOREGROUND_ON;
        this.foregroundOff = Cell.DEFAULT_FOREGROUND_OFF;

        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);

        setRowIndex(rowIndex);
        setColumnIndex(columnIndex);
        setBackground(Cell.DEFAULT_BACKGROUND);
        setOpaque(false);
        setFont(Cell.DEFAULT_FONT);
        setContent(content); // allow subclass override
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Retrieves the row index of the cell within the grid. The row index
     * indicates the vertical position of the cell.
     *
     * @return the row index of the cell.
     */
    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Retrieves the column index of the cell within the grid. The column index
     * indicates the horizontal position of the cell.
     *
     * @return the column index of the cell.
     */
    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Checks if the cell is currently active. An active cell is one that the
     * mouse is currently hovered over, highlighting it for potential
     * interaction.
     *
     * @return {@code true} if the cell is active, {@code false} otherwise.
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the cell is part of an active cluster. Active clusters might
     * have special behaviour or visual indications in the game.
     *
     * @return {@code true} if the cell is part of an active cluster,
     *         {@code false} otherwise.
     */
    @Override
    public boolean inActiveCluster() {
        if (clusters == null || clusters.isEmpty()) {
            return false;
        }

        for (CellCluster cluster : clusters) {
            if (cluster.isActive()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the cell is part of a cluster.
     *
     * @return {@code true} if the cell is part of a cluster, {@code false}
     *         otherwise.
     */
    @Override
    public boolean inCluster() {
        return !clusters.isEmpty();
    }

    /**
     * Retrieves the content of the cell. This content is represented by the
     * first character of the cell's text.
     *
     * @return the character content of the cell.
     */
    @Override
    public char getContent() {
        return content;
    }

    /**
     * Returns a list of all clusters this cell belongs to. A cluster is a group
     * of cells that are treated as a unit within the game logic.
     *
     * @return a list of {@code CellCluster} objects that this cell is part of.
     */
    @Override
    public List<CellCluster> getClusters() {
        return clusters;
    }

    /**
     * Retrieves the foreground colour of the cell for when it is active. This
     * colour defines the visual appearance of the cell's content when the cell
     * is active.
     *
     * @return the foreground colour of the cell.
     */
    @Override
    public Color getForegroundOn() {
        return foregroundOn;
    }

    /**
     * Retrieves the foreground colour of the cell for when it is inactive. This
     * colour defines the visual appearance of the cell's content when the cell
     * is off.
     *
     * @return the foreground colour of the cell.
     */
    @Override
    public Color getForegroundOff() {
        return foregroundOff;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the active state of the cell. This method is used to highlight the
     * cell when the mouse hovers over it, indicating that it is selectable.
     *
     * @param newState {@code true} to activate the cell, {@code false} to
     *                 deactivate it.
     */
    @Override
    public void setState(boolean newState) {
        if (active == newState) {
            return;
        }

        active = newState;

        if (active) {
            setOpaque(true);
            setForeground(foregroundOn);
        } else {
            setOpaque(false); // No background
            setForeground(foregroundOff);
        }
    }

    /**
     * Sets the foreground colour of the cell for when it is active. This method
     * is used to change the visual appearance of the cell's content, which can
     * be useful for improving readability or indicating different states in the
     * game.
     *
     * @param color the new foreground colour of the cell.
     */
    @Override
    public void setForegroundOn(Color color) {
        this.foregroundOn = color;
    }

    /**
     * Sets the foreground colour of the cell for when it is inactive. This
     * method is used to change the visual appearance of the cell's content,
     * which can be useful for improving readability or indicating different
     * states in the game.
     *
     * @param color the new foreground colour of the cell.
     */
    @Override
    public void setForegroundOff(Color color) {
        this.foregroundOff = color;
        foregroundOn = GameUtility.getBestContrast(color);
    }

    /**
     * Sets the content of the cell. This can be an ASCII character or any other
     * symbol used in the game.
     *
     * @param c the character to set as the content of the cell.
     */
    @Override
    public void setContent(char c) {
        if (c < 33 || c >= 127) {
            throw new IllegalCharAddition("Character must be a valid ASCII value (unicode range 33 - 127)");
        }

        content = c;
        super.setText(String.valueOf(c));
    }

    /**
     * Caller must ensure the text is not null or empty.
     *
     * @param text the character to set this cell to.
     */
    @Override
    public void setText(String text) {
        super.setText(String.valueOf(text.charAt(0)));
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Adds the cell to a cluster. A cluster is a group of cells that are
     * treated as a unit within the game logic.
     *
     * @param cluster the {@code CellCluster} to add the cell to.
     */
    @Override
    public void addCluster(CellCluster cluster) {
        cluster.add(this);
        clusters.add(cluster);
    }

    /**
     * Removes the cell from a cluster. A cluster is a group of cells that are
     * treated as a unit within the game logic.
     *
     * @param cluster the {@code CellCluster} to remove the cell from.
     */
    @Override
    public void removeCluster(CellCluster cluster) {
        clusters.remove(cluster);
    }

    /**
     * Determines if this cell shares at least one cluster with the specified
     * cell.
     *
     * <p>
     * This method checks if there is any cluster that both this cell and the
     * specified cell belong to. It returns {@code true} if such a cluster
     * exists and {@code false} otherwise.
     *
     * @param  possibleNeighbourCell the cell to check for shared clusters.
     * @return {@code true} if this cell shares at least one cluster with the
     *         specified cell, {@code false} otherwise.
     */
    public boolean sharesClusterWith(Cell possibleNeighbourCell) {
        if (clusters.isEmpty()) {
            return false;
        }

        for (CellCluster cluster : clusters) {
            if (cluster.contains(possibleNeighbourCell)) {
                return true;
            }
        }

        return false;
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Sets the row index of the cell within the grid. The row index indicates
     * the vertical position of the cell. This method validates that the row
     * index is non-negative.
     *
     * @param  rowIndex the new row index of the cell.
     * @throws IllegalArgumentException if the row index is negative.
     */
    private void setRowIndex(int rowIndex) {
        if (rowIndex < 0) {
            throw new IllegalArgumentException("Row index cannot be negative");
        }

        this.rowIndex = rowIndex;
    }

    /**
     * Sets the column index of the cell within the grid. The column index
     * indicates the horizontal position of the cell. This method validates that
     * the column index is non-negative.
     *
     * @param  columnIndex the new column index of the cell.
     * @throws IllegalArgumentException if the column index is negative.
     */
    private void setColumnIndex(int columnIndex) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column index cannot be negative");
        }

        this.columnIndex = columnIndex;
    }

}