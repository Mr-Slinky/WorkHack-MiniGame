package wordhack.gui;

// Abstract Windows Toolkit imports:
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Swing imports:
import javax.swing.JLabel;

// Frontend imports
import wordhack.gui.cells.Cell;
import wordhack.gui.cells.CellCluster;
import wordhack.gui.cells.LetterCell;

// Backend imports
import wordhack.logic.GameUtility;

/**
 * The {@code CellPanel} class manages and renders a subset of cells in the
 * hacking mini-game. It captures and handles mouse events to update the state
 * of cells based on user interactions, and is designed to work as part of a
 * larger grid layout, handling a specific section of the overall game grid.
 *
 * <p>
 * Extending {@code JPanel}, this class uses a {@code GridLayout} to arrange
 * cells and supports double buffering for smoother graphics. It ensures that
 * cells are visually consistent with their state, managing cell selection,
 * state updates, and visual properties like background and foreground colours.
 * </p>
 *
 * <p>
 * Each instance of {@code CellPanel} is initialised with a subset of the main
 * grid of cells. The panel correctly configures cell positions and dimensions,
 * and assigns visual properties to reflect their state.
 * </p>
 *
 * <p>
 * Methods provided by this class include managing cell positions, setting fonts
 * and colours, and handling mouse events. Mouse event handlers update the
 * active cell based on the mouse's position, ensuring accurate highlighting and
 * activation as the mouse moves over the panel.
 * </p>
 *
 * @see Cell
 * @see CellCluster
 *
 * @version 3.2
 * @since 2024-07-01
 * @author Kheagen Haskins
 */
public final class CellPanel extends GamePanel {

    // ------------------------------ Static -------------------------------- //
    /**
     * Can be changed as long as no instances have been created.
     */
    private static int panelCount = 2;

    /**
     * To stop panel count being changed after any panels have been created.
     */
    private static int currentPanelCount = 0;

    /**
     * Returns the current panel count.
     *
     * @return the current panel count.
     */
    public static final int count() {
        return panelCount;
    }

    /**
     * Sets the panel count. This can only be done if no panels have been
     * created.
     *
     * @param panelCount the new panel count.
     * @throws IllegalStateException if panels have already been created.
     */
    public static void setPanelCount(int panelCount) {
        if (currentPanelCount > 0) {
            throw new IllegalStateException("Cannot change panelCount after CellPanels have been created.");
        }

        CellPanel.panelCount = panelCount;
    }

    // ------------------------------ Fields -------------------------------- //
    /**
     * Cache value for querying cells
     */
    private int cellWidth, cellHeight;  // Cache value

    /**
     * Number of rows in this panel
     */
    private int rows;

    /**
     * The 2D array of cells managed by this panel.
     */
    private Cell[][] cells;

    /**
     * The last cell that was active or hovered over.
     */
    private Cell lastFiredCell;

    /**
     * The grid layout for this panel.
     */
    private GridLayout layout;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a {@code CellPanel} and initialises it with the provided grid
     * of cells.
     *
     * @param gridOfCells the 2D array of cells to be managed by this panel.
     * @throws IllegalArgumentException if the provided cell grid is null,
     * empty, or cannot be evenly divided into the specified panel count.
     */
    public CellPanel(Cell[][] gridOfCells) {
        if (gridOfCells == null || gridOfCells.length == 0) {
            throw new IllegalArgumentException("Cannot instantiate CellColumn with an empty or null array");
        }

        if (gridOfCells.length % panelCount != 0) {
            throw new IllegalArgumentException("Grid of cells with row count " + gridOfCells.length + " cannot be divided into " + panelCount);
        }

        if (currentPanelCount == panelCount) {
            throw new IllegalArgumentException("Cannot instantiate CellPanel when the count has been reached");
        }

        this.lastFiredCell = null; // No active cell to start
        this.rows = gridOfCells.length / panelCount;
        this.layout = new GridLayout(rows, gridOfCells[0].length, 0, 0);

        setLayout(layout);
        setForeground(Cell.DEFAULT_FOREGROUND_OFF);
        setOpaque(false);              // Transparent panel background
        configureCells(gridOfCells);   // Configure cells and ensure cells are consistent

        addMouseMotionListener(new CellPanelMouseListener());
        addMouseListener(new CellPanelMouseListener());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calcCellDimensions();
            }
        });

        CellPanel.currentPanelCount++; // Only update after successful instantiation
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the currently selected text from the cells.
     *
     * @return the currently selected text, or {@code null} if no cell is
     * active.
     */
    public String getSelectedText() {
        if (lastFiredCell == null) {
            return null;
        }

        String text;
        CellCluster mainCluster = lastFiredCell.getMainCluster();
        if (mainCluster != null) {
            text = mainCluster.getText();
        } else {
            text = String.valueOf(lastFiredCell.getContent());
        }

        return text;
    }

    public void dissolveLastActiveCluster() {
        CellCluster mainCluster = lastFiredCell.getMainCluster();
        if (mainCluster != null) {
            mainCluster.clear(true);
        }
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the foreground color for the cells in this panel.
     *
     * @param fg the new foreground color.
     */
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        updateCellColor(fg);
    }

    /**
     * Sets the font for the cells in this panel.
     *
     * @param font the new font.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        updateCellFont(font);
    }

    /**
     * Sets the size of the panel and adjusts cell dimensions accordingly.
     *
     * @param d the new dimension.
     */
    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        calcCellDimensions();
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Configures the initial positions and visual properties of the cells. Sets
     * the position, background color, and foreground color for each cell.
     *
     * @param wholeGrid the full grid of cells to be divided among panels.
     */
    private void configureCells(final Cell[][] wholeGrid) {
        // rowCount should be consistent for each grid
        final int rowCount = wholeGrid.length / panelCount;
        // should be a multiple of rowCount
        int mainGridRowNum = rowCount * currentPanelCount; // starting row
        Color cellColor = getForeground();

        cells = new Cell[rowCount][wholeGrid[0].length];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < wholeGrid[row].length; col++) {
                // Map reference to main grid
                cells[row][col] = wholeGrid[mainGridRowNum][col];

                // Configure cell to be consistent with this container
                cells[row][col].setBackground(cellColor);
                cells[row][col].setForegroundOff(cellColor);

                // Set on-text to black or white depending on its color
                cells[row][col].setForegroundOn(GameUtility.getBestContrast(cellColor));
                add((JLabel) cells[row][col]);
            }
            mainGridRowNum++;
        }

    }

    /**
     * Updates the color in all cells
     *
     * @param fg the new cell color
     */
    private void updateCellColor(Color fg) {
        if (cells == null || cells.length == 0) {
            return;
        }

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setBackground(fg);
                cell.setForegroundOff(fg);
            }
        }
    }

    /**
     * Updates the font in all Cells
     *
     * @param font the new font
     */
    private void updateCellFont(Font font) {
        if (cells == null || cells.length == 0) {
            return;
        }

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setFont(font);
            }
        }
    }

    /**
     * Returns the cell at the specified mouse coordinates.
     *
     * <p>
     * This method calculates the cell based on the mouse's x and y coordinates
     * and returns the corresponding cell from the grid.
     *
     * @param mx the x-coordinate of the mouse.
     * @param my the y-coordinate of the mouse.
     * @return the cell at the specified mouse coordinates.
     */
    private Cell getCellAt(int mx, int my) {
        int rowCursor = my / cellHeight;
        int colCursor = mx / cellWidth;

        if (rowCursor >= cells.length || colCursor >= cells[0].length) {
            return null;
        }

        return cells[rowCursor][colCursor];
    }

    /**
     * Deactivates the specified cell or its cluster if it is part of one.
     *
     * <p>
     * This method deactivates the given cell or its cluster, ensuring that no
     * cell remains active unnecessarily.
     *
     * @param cell the cell to deactivate.
     */
    private void deactivate(Cell cell) {
        if (cell == null) {
            return;
        }

        cell.setClusterStateTo(false);
        cell.setState(false);
    }

    /**
     * Calculate new width and height dimensions for each cell according to the
     * width and height of the panel.
     */
    private void calcCellDimensions() {
        cellWidth = getWidth() / layout.getColumns();
        cellHeight = getHeight() / layout.getRows();
    }

    // |¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯| 
    // |                        Event Handling Logic                        | 
    // |____________________________________________________________________| 
    /**
     * The {@code MouseEventHandler} class extends {@code MouseAdapter} to
     * handle mouse events for the cell grid in the hacking mini-game. It
     * processes mouse movements and exit events to update the state of the
     * cells based on the mouse's position.
     *
     * <p>
     * This class ensures that the correct cell or cluster of cells is activated
     * or deactivated when the mouse moves over the grid or exits the component
     * area. It manages the state of the cells to provide interactive feedback
     * to the player.</p>
     */
    private class CellPanelMouseListener extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent mev) {
            processCellUpdate(getCellAt(mev.getX(), mev.getY()));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            handleMouseExit();
        }

        /**
         * Handles the mouse exit event and updates the state of the active
         * cell.
         *
         * <p>
         * This method deactivates the last fired cell or its cluster if the
         * mouse exits the component area. It ensures that no cell remains
         * active when the mouse is outside the component.
         */
        private void handleMouseExit() {
            if (lastFiredCell == null) {
                return;
            }

            if (lastFiredCell.inCluster()) {
                lastFiredCell.setClusterStateTo(false);
            }

            lastFiredCell.setState(false);
            lastFiredCell = null;
        }

        /**
         * Processes the update of the cell based on the mouse's current
         * position.
         *
         * <p>
         * If the mouse is hovering over a new cell, this method determines if
         * the cell is a letter or a symbol and processes it accordingly. It
         * also manages the activation and deactivation of the last and new
         * cells or their clusters.
         *
         * @param newCell the cell that the mouse is currently hovering over.
         */
        private void processCellUpdate(Cell newCell) {
            if (newCell == null) {
                return;
            } else if (lastFiredCell == newCell) {
                // Same cluster as last, processing would be redundant
                return;
            }

            // Mouse has hovered over a new cell, process it:
            if (lastFiredCell instanceof LetterCell) {
                processNewLetter(newCell);
            } else {
                processNewSymbol(newCell);
            }

            // Deactivate last cell or cluster and activate the new cell or cluster
            lastFiredCell = newCell;
        }

        /**
         * Processes a new symbol cell that the mouse is hovering over.
         *
         * <p>
         * This method deactivates the last fired cell or its cluster and
         * activates the new symbol cell or its cluster if it is part of one.
         *
         * @param newSymbol the new symbol cell that the mouse is hovering over.
         */
        private void processNewSymbol(Cell newSymbol) {
            deactivate(lastFiredCell);

            if (newSymbol.inCluster()) {
                newSymbol.setClusterStateTo(true);
            }

            newSymbol.setState(true);
        }

        /**
         * Processes a new letter cell that the mouse is hovering over.
         *
         * <p>
         * This method deactivates the last fired cell or its cluster and
         * activates the new letter cell's cluster if the new cell shares a
         * cluster with the last fired cell.
         *
         * @param newLetter the new letter cell that the mouse is hovering over.
         */
        private void processNewLetter(Cell newLetter) {
            if (lastFiredCell instanceof LetterCell && lastFiredCell.sharesClusterWith(newLetter)) {
                return; // Exit method
            }

            deactivate(lastFiredCell);
            newLetter.setClusterStateTo(true);
            newLetter.setState(true);
        }

    }

}
