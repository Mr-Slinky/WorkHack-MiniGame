package wordhack.gui.cells;

import java.util.ArrayList;
import java.util.List;
import wordhack.gui.CellPanel;

/**
 * The {@code DefaultCellManager} class creates and manages the cells in the
 * hacking mini-game. It uses an array of characters to instantiate a 2D array
 * of {@code Cell} objects and manages the clustering of cells. This class
 * serves as the main interaction point for creating, initialising, and managing
 * the state and behaviour of the cells within the game grid.
 *
 * <p>
 * This class is central to the game's functionality, as it is responsible for
 * the initial setup and ongoing management of the cell grid. The
 * {@code GameGrid} class relies on {@code DefaultCellManager} to create a set
 * of cells, which are then provided to the {@code CellPanel} class for display
 * purposes. The {@code CellPanel} class displays the cells but does not play a
 * role in creating them.
 * </p>
 *
 * <p>
 * Through its implementation of the {@code CellManager} interface,
 * {@code DefaultCellManager} offers a flexible and efficient way to handle
 * various cell-related operations, including retrieving specific cells,
 * navigating through the grid, and managing clusters of letters and symbols.
 * The class ensures that the cells are correctly initialised based on their
 * type (letter or symbol) and appropriately clustered to support the game's
 * mechanics.
 * </p>
 *
 * <p>
 * By abstracting the complexities of cell management and clustering,
 * {@code DefaultCellManager} simplifies the development and maintenance of the
 * game, promoting a clean and organised architecture. It handles the nuances of
 * cell initialisation, clustering logic, and grid navigation, making it easier
 * for other components to interact with the cell grid.
 * </p>
 *
 * @version 3.1
 * @since   2024-07-01
 * @author  Kheagen Haskins
 */
public class DefaultCellManager implements CellManager {

    // ------------------------------ Fields -------------------------------- //
    /**
     * The 2D array of cells representing the grid.
     */
    private Cell[][] cells;

    /**
     * The number of rows in the grid.
     */
    private int rows;

    /**
     * The number of columns in the grid.
     */
    private int cols;

    private List<LetterCluster> letterClusters;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a {@code CellManager} with the specified grid dimensions and
     * character array. Initialises the grid and clusters the cells based on
     * their types.
     *
     * @param rowCount    the number of rows in the grid.
     * @param columnCount the number of columns in the grid.
     * @param characters  the array of characters to initialise the cells.
     * @throws IllegalArgumentException if rowCount or columnCount is less than
     *                                  1, or if the length of characters is 
     *                                  not equal to rowCount columnCount.
     */
    public DefaultCellManager(int rowCount, int columnCount, char[] characters) {
        if (rowCount < 1) {
            throw new IllegalArgumentException("Row count must be positive");
        } else if (rowCount % CellPanel.count() != 0) {
            throw new IllegalArgumentException("Row count must be a factor of " + CellPanel.count());
        }

        if (columnCount < 1) {
            throw new IllegalArgumentException("Column count must be positive");
        }

        this.rows = rowCount;
        this.cols = columnCount;
        this.cells = new Cell[rowCount][columnCount];

        initCells(characters);
        clusterLetters(cells);
        clusterSymbols(cells);
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the number of rows in the grid.
     *
     * @return the number of rows in the grid.
     */
    @Override
    public int getRowCount() {
        return rows;
    }

    /**
     * Returns the number of columns in the grid.
     *
     * @return the number of columns in the grid.
     */
    @Override
    public int getColumnCount() {
        return cols;
    }

    /**
     * Returns the 2D array of cells representing the grid.
     *
     * @return the 2D array of cells representing the grid.
     */
    @Override
    public Cell[][] getAllCells() {
        return cells;
    }

    /**
     * Returns the cell at the specified row and column.
     *
     * @param  row the row index of the cell.
     * @param  col the column index of the cell.
     * @return the cell at the specified row and column.
     */
    @Override
    public Cell getCellAt(int row, int col) {
        return cells[row][col];
    }

    /**
     * Returns the previous cell in the grid relative to the specified cell. The
     * previous cell is the one to the left, wrapping to the end of the previous
     * row if necessary.
     *
     * @param  cell the reference cell.
     * @return the previous cell in the grid, or {@code null} if the specified
     *         cell is the first cell.
     */
    @Override
    public Cell getPrevious(Cell cell) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex() - 1;

        if (colIndex < 0) {
            colIndex = cols - 1;
            rowIndex--;
            if (rowIndex < 0) {
                return null;
            }
        }

        return cells[rowIndex][colIndex];
    }

    /**
     * Returns the next cell in the grid relative to the specified cell. The
     * next cell is the one to the right, wrapping to the beginning of the next
     * row if necessary.
     *
     * @param  cell the reference cell.
     * @return the next cell in the grid, or {@code null} if the specified cell
     *         is the last cell.
     */
    @Override
    public Cell getNext(Cell cell) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex() + 1;

        if (colIndex >= cols) {
            colIndex = 0;
            rowIndex++;
            if (rowIndex >= rows) {
                return null;
            }
        }

        return cells[rowIndex][colIndex];
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Returns a string representation of the grid. Represents the grid in two
     * halves.
     *
     * @return a string representation of the grid and its clusters.
     */
    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder("Characters:\n");
        int rc = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                outp.append(cell.getContent()).append("\t");
            }
            outp.append("\n");
            if (++rc == rows / 2) {
                outp.append("\n");
            }
        }

        return outp.toString();
    }

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
     * @return the dud that was removed, {@code null} if no dud was removed.
     */
    public String removeDud(String correctWord) {
        if (letterClusters.isEmpty() || letterClusters.size() < 2) {
            return null;
        }

        LetterCluster cluster;
        String dud;
        for (LetterCluster lc : letterClusters) {
            dud = lc.getText();
            if (dud != null && !dud.equalsIgnoreCase(correctWord)) {
                lc.clear(true);
                letterClusters.remove(lc);
                return dud;
            }
        }

        return null;
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initialises the cells in the grid with the specified characters. Creates
     * {@code LetterCell} or {@code SymbolCell} objects based on the character
     * type.
     *
     * @param characters the array of characters to initialise the cells.
     */
    private void initCells(char[] characters) {
        int ci = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = characters[ci++];
                if (Character.isLetter(c)) {
                    cells[row][col] = new LetterCell(row, col, c);
                } else {
                    cells[row][col] = new SymbolCell(row, col, c);
                }
            }
        } // End of loop
    }

    /**
     * Clusters adjacent letter cells together into {@code LetterCluster}
     * objects. This algorithm iterates through the grid and groups contiguous
     * letter cells.
     */
    private void clusterLetters(Cell[][] cells) {
        letterClusters = new ArrayList<>();
        LetterCluster currentClusters = new LetterCluster();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];
                // Check if the current cell is a letter
                if (cell instanceof LetterCell) {
                    cell.addCluster(currentClusters);
                } else if (!currentClusters.isEmpty()) {
                    // Finalise the current cluster if a symbol cell is found and the cluster is not empty
                    if (currentClusters.close()) {
                        letterClusters.add(currentClusters);
                    }

                    currentClusters = new LetterCluster();
                }
            } // End of inner loop
        } // End of outer loop

        currentClusters.close();
    }

    // All methods below are related to symbol-clustering Logic:
    // ---------------------------------------------------------------------------
    /**
     * Clusters symbol cells together into {@code SymbolCluster} objects based
     * on matching open and close types. This algorithm processes the grid row
     * by row, identifying matching open and close symbol types without letters
     * in between. Don't worry if this look intimidating; it took me HOURS to
     * figure out... (could probably be simplified)
     *
     * @param cells a 2D array of {@code Cell} objects representing the grid.
     */
    private void clusterSymbols(Cell[][] cells) {
        SymbolCluster cluster = new SymbolCluster(); // Initialise a new SymbolCluster
        for (int rowIndex = 0; rowIndex < cells.length; rowIndex++) {
            Cell[] row = cells[rowIndex]; // Get the current row

            for (int colIndex = 0; colIndex < row.length; colIndex++) {
                // Skip the current cell if it is a LetterCell
                if (row[colIndex] instanceof LetterCell) {
                    continue;
                }

                // Must be a symbol cell:
                SymbolCell cell = (SymbolCell) row[colIndex];
                if (cell.isOpenType()) {
                    // Find the index of the matching close type symbol
                    int closeTypeIndex = getMatchingCloseTypeIndex(
                            row,
                            colIndex + 1,
                            cell.getOpenType()
                    );

                    if (closeTypeIndex > -1) { // Matching close type found
                        // Add all cells from the open type to the close type to the cluster
                        for (int k = colIndex; k <= closeTypeIndex; k++) {
                            row[k].addCluster(cluster);
                        }

                        cluster.close(); // Close the current cluster
                        cluster = new SymbolCluster(); // Initialise a new SymbolCluster for the next match
                    }
                }
            } // End of inner loop

            cluster.close(); // Close the cluster at the end of each row
        } // End of outer loop
    }

    /**
     * Finds the index of the matching close type symbol in the given row.
     *
     * @param  row        the array of {@code Cell} objects representing the row.
     * @param  startIndex the starting index to search for the matching close
     *                    type.
     * @param  openType   the open type symbol to match against.
     * @return the index of the matching close type symbol, or -1 if no match is
     *         found.
     */
    private int getMatchingCloseTypeIndex(Cell[] row, int startIndex, int openType) {
        for (int i = startIndex; i < row.length; i++) {
            // If a LetterCell is encountered, stop the search
            if (row[i] instanceof LetterCell) {
                break;
            }

            SymbolCell cell = (SymbolCell) row[i];
            // Check if the cell is a close type and matches the open type
            if (cell.isCloseType() && (openType == cell.getCloseType())) {
                return i; // Return the index of the matching close type symbol
            }
        }

        return -1; // Return -1 if no matching close type symbol is found
    }

} // End of class