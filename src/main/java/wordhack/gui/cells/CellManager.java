package wordhack.gui.cells;

/**
 * The {@code CellManager} interface defines the methods required for managing a
 * grid of cells in the hacking mini-game. Implementations of this interface are
 * responsible for <b>initialising</b>, accessing, and manipulating the cells
 * within the grid.
 *
 * <p>
 * This interface provides a flexible framework for cell management, allowing
 * different strategies or rules to be applied as needed. It abstracts the
 * complexities of grid operations, enabling the easy retrieval and navigation
 * of cells within the grid. By defining these methods, {@code CellManager}
 * ensures a consistent API for handling cells, which facilitates the
 * implementation of various game mechanics.
 * </p>
 *
 * <p>
 * Given that most of the heavy lifting is done in the implementing classes,
 * this interface focuses on specifying the essential operations for cell
 * management. This includes methods to retrieve the grid dimensions, access
 * specific cells, and navigate through the grid in a logical manner. By
 * adhering to this interface, different implementations can provide customised
 * behaviour while maintaining a standardised approach to cell management.
 * </p>
 *
 * @version 3.0
 * @since   2024-07-01 
 * @author  Kheagen Haskins
 */
public interface CellManager {

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the number of rows in the grid.
     *
     * @return the number of rows in the grid.
     */
    public int getRowCount();

    /**
     * Returns the number of columns in the grid.
     *
     * @return the number of columns in the grid.
     */
    public int getColumnCount();

    /**
     * Returns the 2D array of cells representing the grid.
     *
     * @return the 2D array of cells representing the grid.
     */
    public Cell[][] getAllCells();

    /**
     * Returns the cell at the specified row and column.
     *
     * @param  row the row index of the cell.
     * @param  col the column index of the cell.
     * @return the cell at the specified row and column.
     */
    public Cell getCellAt(int row, int col);

    /**
     * Returns the previous cell in the grid relative to the specified cell. The
     * previous cell is the one to the left, wrapping to the end of the previous
     * row if necessary.
     *
     * @param  cell the reference cell.
     * @return the previous cell in the grid, or {@code null} if the specified
     *         cell is the first cell.
     */
    public Cell getPrevious(Cell cell);

    /**
     * Returns the next cell in the grid relative to the specified cell. The
     * next cell is the one to the right, wrapping to the beginning of the next
     * row if necessary.
     *
     * @param  cell the reference cell.
     * @return the next cell in the grid, or {@code null} if the specified cell
     *         is the last cell.
     */
    public Cell getNext(Cell cell);

    /**
     * Searches through all words in each {@code LetterCluster} instance and
     * removes a random cluster that does not match the {@code correctWord}
     * argument.
     *
     * <p>
     * This method iterates over all letter clusters and identifies clusters
     * whose content does not match the specified correct word. It then removes
     * one of these "dud" clusters randomly. This is useful for gameplay
     * mechanics where incorrect guesses (duds) need to be eliminated while
     * preserving the correct word.
     * </p>
     *
     * @param  correctWord the correct word to ensure it is not removed.
     * @return a string holding the dud that was removed, or {@code null} if no dud was removed.
     */
    public String removeDud(String correctWord);
    
}