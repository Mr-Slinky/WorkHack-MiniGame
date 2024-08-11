package wordhack.gui.cells;

/**
 * The {@code SymbolCluster} class represents a cluster of symbols within the
 * hacking mini-game grid. These symbols must start and end with the same types
 * of opening and closing characters (e.g., '[' and ']'). Clicking a cluster of
 * symbols triggers a random event that either replenishes the user's guesses or
 * removes a dud password from the screen.
 *
 * This class extends {@code AbCluster} and provides additional validation
 * to ensure that the symbol cluster is correctly formed.
 *
 * @see CellCluster
 * @see AbCluster
 * @see SymbolCell
 * 
 * @version 2.1
 * @since   2024-06-27
 * @Author  Kheagen Haskins
 */
class SymbolCluster extends AbCluster {

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Adds a cell to the symbol cluster. The cell must be a {@code SymbolCell}.
     *
     * @param cell the cell to add.
     * @throws IllegalArgumentException if the cell is null or not an instance
     * of {@code SymbolCell}.
     */
    @Override
    public void add(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cannot add a null cell to a symbol cluster.");
        } else if (!(cell instanceof SymbolCell)) {
            throw new IllegalArgumentException("Cannot add a letter cell to a symbol cluster.");
        }

        super.add(cell);
    }

    /**
     * Validates and closes the symbol cluster to ensure its first and last
     * cells are matching open and close types. If the cluster is invalid, a
     * {@code ClusterCloseException} is thrown.
     *
     * @return false if the symbol cluster is invalid
     * @throws ClusterCloseException if the symbol cluster is incomplete or
     * invalid.
     */
    @Override
    public boolean close() {
        if (isEmpty()) {
            return false;
        }

        try {
            SymbolCell firstCell = (SymbolCell) getFirstCell();
            SymbolCell lastCell = (SymbolCell) getLastCell();

            if (lastCell == null) {
                throw new ClusterCloseException(this, "Symbol cluster incomplete.");
            } else if (!(firstCell.isOpenType() && lastCell.isCloseType())) {
                throw new ClusterCloseException(this, "Symbol cluster must start with open type and end with close type.");
            } else if (firstCell.getOpenType() != lastCell.getCloseType()) {
                throw new ClusterCloseException(this, "Symbol cluster must start and end with matching open and close types.");
            }
        } catch (ClassCastException ex) { // Should be guarded by the add() method
            throw new ClusterCloseException(this, "Symbol cluster contains non-symbol cells.");
        }

        return super.close();
    }

}