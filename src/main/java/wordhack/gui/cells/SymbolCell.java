package wordhack.gui.cells;

/**
 * The {@code SymbolCell} class represents a cell within the hacking mini-game
 * grid that specifically handles symbol characters, such as brackets. This
 * class extends {@code AbCell} to provide additional functionality for
 * recognising and managing symbol characters.
 *
 * <p>
 * The cell content is restricted to non-letter ASCII characters, ensuring that
 * only symbols are used. This restriction helps maintain the integrity of the
 * game by preventing the use of invalid characters. The class also determines
 * whether a symbol is an opening or closing bracket, facilitating specific game
 * logic related to these types of characters.
 * </p>
 *
 * <p>
 * By extending {@code AbCell}, the {@code SymbolCell} class benefits from a
 * centralised implementation of common cell behaviour, adhering to the DRY
 * (Don't Repeat Yourself) principle. This design reduces redundancy and ensures
 * that symbol cells integrate seamlessly with the overall game logic. The class
 * handles validation and setting of symbol content, making sure that each
 * symbol cell behaves consistently and predictably.
 * </p>
 *
 * <p>
 * The {@code SymbolCell} class also includes logic to identify and manage its
 * type as an opening or closing bracket. This feature is crucial for the game's
 * mechanics, which may treat opening and closing brackets differently. The
 * class provides methods to check the type of bracket and retrieve its
 * corresponding type index.
 * </p>
 *
 * <p>
 * Additionally, the {@code SymbolCell} class integrates with the clustering
 * mechanism, ensuring that it can only be added to {@code SymbolCluster}
 * instances. This further enforces logical groupings within the game,
 * preventing invalid cluster associations and enhancing game integrity.
 * </p>
 *
 * @see Cell
 * @see AbCell
 * @see LetterCell
 * @see CellCluster
 * @see AbCluster
 * @see SymbolCluster
 *
 * @version 3.2
 * @since 2024-06-27
 * @author Kheagen Haskins
 */
public class SymbolCell extends AbCell {

    // ------------------------------ Static -------------------------------- //
    /**
     * Characters that are considered as opening brackets.
     */
    private static final char[] openChars = {'(', '{', '[', '<'};

    /**
     * Characters that are considered as closing brackets.
     */
    private static final char[] closeChars = {')', '}', ']', '>'};

    // ------------------------------ Fields -------------------------------- //
    /**
     * Represents the type of opening bracket if the cell contains one,
     * otherwise -1.
     */
    private int openType;

    /**
     * Represents the type of closing bracket if the cell contains one,
     * otherwise -1.
     */
    private int closeType;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code SymbolCell} with the specified row, column, and
     * content, and default dimensions.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param content the character content of the cell; must be a valid symbol
     * @throws IllegalArgumentException if the character content is not valid
     */
    public SymbolCell(int row, int col, char content) {
        super(row, col, content);
        setContent(content); // Validates and sets the content
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Checks if the cell contains an opening bracket type.
     *
     * @return {@code true} if the cell contains an opening bracket,
     * {@code false} otherwise.
     */
    public boolean isOpenType() {
        return openType != -1;
    }

    /**
     * Checks if the cell contains a closing bracket type.
     *
     * @return {@code true} if the cell contains a closing bracket,
     * {@code false} otherwise.
     */
    public boolean isCloseType() {
        return closeType != -1;
    }

    /**
     * Gets the type index of the opening bracket contained in the cell.
     *
     * @return the type index of the opening bracket, or -1 if none.
     */
    public int getOpenType() {
        return openType;
    }

    /**
     * Gets the type index of the closing bracket contained in the cell.
     *
     * @return the type index of the closing bracket, or -1 if none.
     */
    public int getCloseType() {
        return closeType;
    }

    /**
     * Retrieves the main cluster associated with this symbol cell.
     *
     * <p>
     * If the cell belongs to any clusters, this method iterates through them to
     * find the cluster where this cell is the first cell. If such a cluster is
     * found, it is returned. If the cell does not belong to any clusters, or if
     * it is not the first cell in any of its clusters, this method returns
     * {@code null}.
     * </p>
     *
     * @return the main {@code CellCluster} this cell belongs to, or
     * {@code null} if the cell does not belong to any cluster or is not the
     * first cell in any of its clusters.
     */
    @Override
    public CellCluster getMainCluster() {
        if (inCluster()) {
            for (CellCluster cluster : getClusters()) {
                if (cluster.getFirstCell() == this) {
                    return cluster;
                }
            }
        }

        return null;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the content of the cell. The content must be a non-letter ASCII
     * character. This method also determines if the character is an opening or
     * closing bracket.
     *
     * @param c the character to set as the content of the cell.
     *
     * @throws IllegalArgumentException if the character is a letter.
     */
    @Override
    public final void setContent(char c) {
        if (Character.isLetter(c)) {
            throw new IllegalArgumentException("Cannot add a letter to a symbol cell");
        }

        openType = -1;
        closeType = -1;

        // Determine open type (if any)
        for (int i = 0; i < openChars.length; i++) {
            if (openChars[i] == c) {
                openType = i;
                break;
            }
        }

        // Determine close type (if any)
        for (int i = 0; i < closeChars.length; i++) {
            if (closeChars[i] == c) {
                closeType = i;
                break;
            }
        }

        super.setContent(c);
    }

    /**
     * Sets the active state of the cluster that this cell belongs to if this
     * cell is the first cell in the cluster.
     *
     * <p>
     * If the cell is not part of any cluster, the method returns immediately.
     * Otherwise, it iterates through all clusters that this cell belongs to and
     * sets their active state to the specified new state, but only if this cell
     * is the first cell in the cluster. If this cell is not the first cell in
     * any of its clusters, the method does nothing.
     * </p>
     *
     * @param newState {@code true} to activate the cluster, {@code false} to
     * deactivate it.
     */
    @Override
    public void setClusterStateTo(boolean newState) {
        if (!inCluster()) {
            return; // Exit Method
        }

        CellCluster mainCluster = getMainCluster();
        if (mainCluster != null) {
            mainCluster.setState(newState);
            return; // Exit Method
        }
        
//        this.setState(newState);
    }

    /**
     * Adds the SymbolCell to a SymbolCluster. A cluster is a group of cells
     * that are treated as a unit within the game logic.
     *
     * @param sCluster the {@code SymbolCluster} to add the cell to.
     * @throws IllegalArgumentException if the {@code sCluster} argument is not
     * an instance of {@link SymbolCluster}
     */
    @Override
    public void addCluster(CellCluster sCluster) {
        if (!(sCluster instanceof SymbolCluster)) {
            throw new IllegalArgumentException("Cannot add a symbol cell to a non-symbol cluster.");
        }

        super.addCluster(sCluster);
    }
}
