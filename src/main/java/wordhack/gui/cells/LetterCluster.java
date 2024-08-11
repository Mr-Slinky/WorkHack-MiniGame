package wordhack.gui.cells;

/**
 * The {@code LetterCluster} class represents a cluster of letter cells within
 * the hacking mini-game grid. This class extends {@code AbCluster} and ensures
 * that only {@code LetterCell} instances can be added to the cluster.
 * <p>
 * A {@code LetterCluster} is a specialised cluster designed specifically to
 * hold instances of {@code LetterCell}. It provides functionality to add cells,
 * ensuring that only valid {@code LetterCell} instances are added, and includes
 * logic to close the cluster under certain conditions.
 * </p>
 *
 * @see LetterCell
 * @see CellManager
 * @see AbCluster
 * @see LetterCluster
 *
 * @version 3.1
 * @since 2024-06-26
 * @Author Kheagen Haskins
 */
class LetterCluster extends AbCluster {

    /**
     * Adds a cell to the letter cluster. The cell must be an instance of
     * {@code LetterCell}.
     *
     * <p>
     * This method overrides the {@code add} method in the {@code AbCluster}
     * class to enforce that only {@code LetterCell} instances can be added to
     * the cluster. If the cell is {@code null} or not an instance of
     * {@code LetterCell}, an {@code IllegalArgumentException} is thrown.
     * </p>
     *
     * @param cell the cell to add to the cluster.
     * @throws IllegalArgumentException if the cell is {@code null} or not an
     * instance of {@code LetterCell}.
     */
    @Override
    public void add(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cannot add a null cell to a letter cluster.");
        } else if (!(cell instanceof LetterCell)) {
            throw new IllegalArgumentException("Cannot add a non-letter cell to a letter cluster.");
        }

        super.add(cell);
    }

    /**
     * Closes the letter cluster.
     * <p>
     * This method overrides the {@code close} method in the {@code AbCluster}
     * class. It ensures that the cluster is not empty and contains at least two
     * {@code LetterCell} instances before closing. If these conditions are not
     * met, the method throws a {@code ClusterCloseException} or returns
     * {@code false} if the cluster is empty.
     * </p>
     *
     * @return {@code true} if the cluster is successfully closed, {@code false}
     * if the cluster is empty.
     * @throws ClusterCloseException if the cluster contains fewer than two
     * {@code LetterCell} instances.
     */
    @Override
    public boolean close() {
        if (isEmpty()) {
            return false;
        }

        if (getSize() < 2) {
            throw new ClusterCloseException(this, "Letter cluster must contain a minimum of 2 letters");
        }

        return super.close();
    }

}