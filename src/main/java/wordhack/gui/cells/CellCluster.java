package wordhack.gui.cells;

/**
 * The {@code CellCluster} interface represents a group of cells that are
 * treated as a single entity within the hacking mini-game grid. Clusters can
 * contain either letter sets (forming words) or symbol sets (matching open and
 * close brackets of the same type on the same row). This interface defines
 * methods to manage and interact with such clusters, facilitating cohesive
 * gameplay mechanics.
 *
 * <p>
 * In the context of the hacking mini-game, clusters are essential for managing
 * groups of cells that players interact with to guess passwords or identify
 * matching brackets. Handling these groups as clusters simplifies game logic by
 * allowing operations on sets of cells rather than individual ones.
 * </p>
 *
 * <p>
 * Implementations of this interface are expected to define the specifics of
 * cluster formation, modification, and interaction within the game grid. By
 * standardising cluster behaviour through this interface, the game ensures
 * consistent handling of cell groups, improving maintainability and reducing
 * potential errors.
 * </p>
 *
 * <p>
 * Clusters provide a way to encapsulate collective cell behaviour, such as
 * activating or deactivating all cells in the cluster, managing their content,
 * and ensuring that only valid operations are performed on them. This
 * abstraction is crucial for maintaining the integrity and functionality of the
 * game's core mechanics.
 * </p>
 *
 * @see Cell
 * @see CellManager
 * @see AbCluster
 * @see SymbolCluster
 * @see LetterCluster
 *
 * @version 3.1
 * @since   2024-06-27
 * @author  Kheagen Haskins
 */
public interface CellCluster {

    // ------------------------------ Getters ------------------------------- //
    /**
     * Retrieves the size of the cluster, which is the number of cells it
     * contains.
     *
     * @return the size of the cluster.
     */
    public int getSize();

    /**
     * Checks if the cluster is currently active. In the context of the game, an
     * active cluster means that all its cells are treated as active.
     *
     * @return {@code true} if the cluster is active, {@code false} otherwise.
     */
    public boolean isActive();

    /**
     * Checks if the cluster is closed. A closed cluster cannot be modified
     * further, meaning cells cannot be added, removed, or cleared.
     *
     * @return {@code true} if the cluster is closed, {@code false} otherwise.
     */
    public boolean isClosed();

    /**
     * Checks if the cluster is empty, i.e., contains no cells.
     *
     * @return {@code true} if the cluster is empty, {@code false} otherwise.
     */
    public boolean isEmpty();

    /**
     * Retrieves the index of a specific cell within the cluster.
     *
     * @param cell the cell whose index is to be found.
     * @return the index of the cell within the cluster, or -1 if the cell is
     * not found.
     */
    public int getIndexOf(Cell cell);

    /**
     * Retrieves the Cell at the specified index.
     *
     * @param index The index of the cell to return
     * @return the Cell at the specified index.
     */
    public Cell getCellAt(int index);

    /**
     * Concatenates each cell's content into a single string and returns it.
     * This is vital for guessing passwords and removing duds in the game.
     *
     * @return a string representing the concatenated content of the cells in
     * the cluster.
     */
    public String getText();

    /**
     * Retrieves the first cell in the cluster. This is particularly important
     * for symbol sets, where the first cell must be an open bracket type.
     *
     * @return the first cell in the cluster.
     */
    public Cell getFirstCell();

    /**
     * Retrieves the last cell in the cluster. This is particularly important
     * for symbol sets, where the last cell must be a close bracket type.
     *
     * @return the last cell in the cluster.
     */
    public Cell getLastCell();

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the active state of the cluster, activating all cells in the cluster
     * or deactivating them
     *
     * @param newState the new state of the cluster
     */
    public void setState(boolean newState);

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Adds a cell to the cluster. If the cluster is closed, no cells can be
     * added.
     *
     * @param cell the cell to be added to the cluster.
     */
    public void add(Cell cell);

    /**
     * Removes a cell from the cluster. If the cluster is closed, no cells can
     * be removed.
     *
     * @param cell the cell to be removed from the cluster.
     */
    public void remove(Cell cell);

    /**
     * Checks if the cluster contains a specific cell.
     *
     * @param cell the cell to check for.
     * @return {@code true} if the cell is in the cluster, {@code false}
     *         otherwise.
     */
    public boolean contains(Cell cell);

    /**
     * Closes and validates the cluster, meaning no more cells can be added,
     * removed, or cleared. The content of the cells can still be modified.
     *
     * @return false if the cell did not close for any reason, indicating an
     *         invalid cluster.
     */
    public boolean close();

    /**
     * Clears all cells from the cluster. If the cluster is closed, it cannot be
     * cleared unless {@code bypass} is set to true.
     *
     * @param bypass a flag to control whether to bypass the closed flag
     */
    public void clear(boolean bypass);

    /**
     * Sets the content of each cell in the cluster to the specified character.
     *
     * @param c the character to set as the content of each cell.
     */
    public void flood(char c);

    /**
     * The {@code ClusterCloseException} class represents an exception that is
     * thrown when there is an issue closing a cell cluster within the hacking
     * mini-game grid. This exception is specifically used to indicate that a
     * cluster does not meet the necessary criteria to be closed, such as
     * mismatched opening and closing symbols or an incomplete cluster.
     *
     * This class extends {@code IllegalStateException} to signal that the
     * cluster is in an invalid state for the requested operation.
     *
     * @version 1.1
     * @since   2024-07-01
     * @Author  Kheagen Haskins
     */
    public class ClusterCloseException extends IllegalStateException {

        /**
         * Constructs a new {@code ClusterCloseException} with the specified
         * detail message.
         *
         * @param cluster the cluster that failed to close.
         * @param s the detail message explaining the reason for the exception.
         */
        public ClusterCloseException(CellCluster cluster, String s) {
            super(s + (cluster == null ? "" : " Cluster text: " + cluster.getText()));
        }
    }

}