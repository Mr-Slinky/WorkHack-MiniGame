package wordhack.gui.cells;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AbCluster} class provides a skeletal implementation of the
 * {@code CellCluster} interface, designed to minimise the effort required to
 * implement this interface. It manages a collection of cells and handles their
 * collective state and behaviour, allowing for efficient operations on groups
 * of cells within the hacking mini-game grid.
 *
 * <p>
 * This abstract class enables operations to be performed on all cells within
 * the cluster, such as setting their active state or concatenating their
 * content. It supports adding and removing cells from the cluster, provided the
 * cluster is not closed to modifications. This centralises common cluster
 * functionalities, promoting the DRY (Don't Repeat Yourself) principle by
 * avoiding redundant code across different cluster implementations.
 * </p>
 *
 * <p>
 * The {@code AbCluster} class also manages the active and closed states of
 * clusters. An active cluster means all its cells are highlighted or
 * interactable, while a closed cluster prevents further modifications, ensuring
 * stability in the game logic. The class provides methods to check these
 * states, manage the cells within the cluster, and handle the lifecycle of the
 * cluster from creation to closure.
 * </p>
 *
 * <p>
 * By inheriting from {@code AbCluster}, concrete cluster classes like
 * {@code SymbolCluster} and {@code LetterCluster} can focus on their specific
 * behaviours and rules, such as managing symbols or letters, without
 * re-implementing the common functionality provided by this abstract class.
 * This approach enhances maintainability, reduces the potential for errors, and
 * simplifies the extension of cluster-related features in the game.
 * </p>
 *
 * @see Cell
 * @see CellManager
 * @see SymbolCluster
 * @see LetterCluster
 *
 * @version 3.1
 * @since   2024-07-01
 * @author  Kheagen Haskins
 */
abstract class AbCluster implements CellCluster {

    // ------------------------------ Fields -------------------------------- //
    /**
     * The list of cells that belong to this cluster. This list is used to
     * perform operations on all cells within the cluster.
     */
    private List<Cell> cells;

    /**
     * Indicates whether the cluster is currently active. If true, all cells in
     * the cluster are in an active state (e.g., highlighted).
     */
    private boolean active;

    /**
     * Indicates whether the cluster is closed to further modifications. If
     * true, no additional cells can be added to the cluster.
     */
    private boolean closed;

    /**
     * The result of concatenating the content of each cell in this cluster.
     */
    private String text;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code AbstractCluster}. Initialises the cluster as
     * empty, with inactive and open states.
     */
    AbCluster() {
        cells = new ArrayList<>();
        active = false;
        closed = false;
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the number of cells in the cluster.
     *
     * @return the size of the cluster.
     */
    @Override
    public int getSize() {
        return cells.size();
    }

    /**
     * Checks if the cluster is currently active.
     *
     * @return {@code true} if the cluster is active, {@code false} otherwise.
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the cluster is closed to further modifications.
     *
     * @return {@code true} if the cluster is closed, {@code false} otherwise.
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    /**
     * Checks if the cluster is empty.
     *
     * @return {@code true} if the cluster is empty, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return cells.isEmpty();
    }

    /**
     * Returns the index of the specified cell within the cluster.
     *
     * @param  cell the cell to find.
     * @return the index of the cell, or -1 if the cell is not in the cluster.
     */
    @Override
    public int getIndexOf(Cell cell) {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i) == cell) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieves the Cell at the specified index.
     *
     * @param  index The index of the cell to return
     * @return the Cell at the specified index.
     */
    @Override
    public Cell getCellAt(int index) {
        return cells.get(index);
    }

    /**
     * Returns the concatenated text of all cells in the cluster. If this method
     * returns {@code null}, then the cluster has been cleared and you are
     * trying to retrieve the text of an empty array.
     *
     * @return the concatenated text of all cells.
     */
    @Override
    public String getText() {
        if (text != null) {
            return text;
        }

        if (isEmpty()) {
            return null;
        }

        finaliseText();
        return text;
    }

    /**
     * Returns the first cell in the cluster.
     *
     * @return the first cell, or {@code null} if the cluster is empty.
     */
    @Override
    public Cell getFirstCell() {
        if (cells.isEmpty()) {
            return null;
        }

        return cells.get(0);
    }

    /**
     * Returns the last cell in the cluster.
     *
     * @return the last cell, or {@code null} if the cluster is empty.
     */
    @Override
    public Cell getLastCell() {
        if (cells.isEmpty()) {
            return null;
        }

        return cells.get(cells.size() - 1);
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the active state of the cluster, activating all cells in the cluster
     * or deactivating them
     *
     * @param newState the new state of the cluster
     */
    @Override
    public void setState(boolean newState) {
        if (active == newState) {
            return;
        }

        active = newState;
        for (Cell cell : cells) {
            cell.setState(newState);
        }
    }

    /**
     * Adds a cell to the cluster. If the cluster is closed, this method throws
     * an {@code IllegalStateException}. No null check performed.
     *
     * @param  cell the cell to add.
     * @throws IllegalStateException if the cluster is closed.
     */
    @Override
    public void add(Cell cell) {
        if (closed) {
            throw new IllegalStateException("Attempted to add cell with content " + cell.getContent() + " to closed cluster(" + getText() + ")");
        }

        cells.add(cell);
    }

    /**
     * Removes a cell from the cluster. If the cluster is closed, this method
     * throws an {@code IllegalStateException}.
     *
     * @param  cell the cell to remove.
     * @throws IllegalArgumentException if the cell is null.
     * @throws IllegalStateException if the cluster is closed.
     */
    @Override
    public void remove(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cannot remove a null cell from a cluster");
        }

        if (closed) {
            throw new IllegalStateException("Cannot remove from a closed cluster.");
        }

        cells.remove(cell);
    }

    /**
     * Checks if the cluster contains the specified cell.
     *
     * @param  cell the cell to check.
     * @return {@code true} if the cluster contains the cell, {@code false}
     *         otherwise.
     */
    @Override
    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Clears all cells from the cluster. If the cluster is closed, it cannot be
     * cleared unless {@code bypass} is set to true.
     *
     * @param  bypass a flag to control whether to bypass the closed flag
     * @throws IllegalStateException if the cluster is closed and bypass is set
     *                               to false.
     */
    @Override
    public void clear(boolean bypass) {
        if (closed) {
            if (!bypass) {
                throw new IllegalStateException("Cannot clear a closed cluster.");
            }
            
            if (this instanceof LetterCluster) {
                flood('.');
            }
            
            closed = false;
        }

        // Remove the reference of this cluster for each of its cells
        for (Cell cell : cells) {
            cell.setState(false);
            cell.removeCluster(this);
        }

        cells.clear();
        text = null; // Clears text

        closed = true;
    }

    /**
     * Closes the cluster, concatenates the contents of the cells into a single
     * string, and marks the cluster as closed.
     *
     * <p>
     * This method iterates through the list of cells in the cluster,
     * concatenates their contents, and stores the resulting string. It then
     * marks the cluster as closed and returns true.
     *
     * @return {@code true} indicating that the cluster has been successfully
     *         closed.
     */
    @Override
    public boolean close() {
        finaliseText();
        closed = true;
        return true;
    }

    /**
     * Sets the content of all cells in the cluster to the specified character.
     * This method is intended to be implemented by subclasses to provide
     * specific behaviour.
     *
     * @param c the character to set as the content of all cells.
     */
    @Override
    public void flood(char c) {
        if (cells.isEmpty()) {
            return;
        }

        for (Cell cell : cells) {
            cell.setContent(c);
        }
    }

    private void finaliseText() {
        StringBuilder outp = new StringBuilder();

        for (int i = 0; i < cells.size(); i++) {
            outp.append(cells.get(i).getContent());
        }

        text = outp.toString();
    }

}