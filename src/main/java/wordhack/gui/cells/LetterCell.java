package wordhack.gui.cells;

/**
 * The {@code LetterCell} class represents a cell within the hacking mini-game
 * grid that specifically handles letter characters. This class extends
 * {@code AbCell} to provide additional functionality for recognising and
 * managing letter characters, ensuring that the content is restricted to valid
 * letters and optionally a single valid symbol.
 *
 * <p>
 * Letter cells are crucial for the game's logic, as they represent the primary
 * type of interactive element that players interact with to guess the password.
 * By enforcing that content must be a letter or the valid symbol {@code '.'},
 * the class ensures data integrity and consistency across the game grid.
 * </p>
 *
 * <p>
 * All letter characters are automatically converted to uppercase to maintain
 * uniform presentation and simplify comparisons. Any attempt to set a
 * non-letter character (except the valid symbol) will result in an
 * {@code IllegalArgumentException}, providing immediate feedback during
 * development and runtime to avoid invalid states.
 * </p>
 *
 * <p>
 * This class illustrates the DRY (Don't Repeat Yourself) principle by
 * centralising the validation logic for letter characters. By doing so, it
 * eliminates redundant checks in other parts of the application and ensures
 * that all letter cells behave consistently. Additionally, the
 * {@code LetterCell} class integrates seamlessly with the clustering mechanism,
 * ensuring that letter cells can only belong to a <b>single</b>
 * {@code LetterCluster} instance, further enforcing logical groupings within
 * the game.
 * </p>
 *
 * <p>
 * The {@code LetterCell} class leverages inheritance from {@code AbCell} to
 * minimise boilerplate code, allowing for easier maintenance and extension.
 * Subclasses or instances of {@code LetterCell} can focus on game-specific
 * logic without re-implementing common cell functionality.
 * </p>
 *
 * @see Cell
 * @see AbCell
 * @see SymbolCell
 * @see CellCluster
 * @see LetterCluster
 * @see SymbolCluster
 * @see CellManager
 * @see wordhack.gui.CellPanel
 *
 * @version 3.2
 * @since   2024-06-27
 * @author  Kheagen Haskins
 */
public class LetterCell extends AbCell {

    // ------------------------------ Static -------------------------------- //
    /**
     * A single valid symbol that a letter cell can contain.
     */
    static final char VALID_SYMBOL = '.';

    /**
     * Validates that the character is a letter and converts it to uppercase.
     *
     * @param c the character to validate
     * @return the uppercase letter character
     * @throws IllegalArgumentException if the character is not a letter
     */
    private static char validateCharacter(char c) {
        if (!Character.isLetter(c) && c != VALID_SYMBOL) {
            throw new IllegalArgumentException(c + " is not a valid letter character");
        }
        return Character.toUpperCase(c);
    }

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code LetterCell} with the specified row, column, and
     * content, and default dimensions.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param content the character content of the cell; must be a valid letter
     * @throws IllegalArgumentException if the character content is not valid
     */
    public LetterCell(int row, int col, char content) {
        super(row, col, validateCharacter(content));
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Retrieves the first cluster in this cell's cluster list, of which there
     * should only be a single letter cluster.
     *
     * @return the first cluster in this cell's cluster list
     */
    @Override
    public CellCluster getMainCluster() {
        return getClusters().get(0);
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the content of the cell. The content is validated to ensure it is a
     * valid letter and converted to uppercase.
     *
     * @param c the new character content of the cell
     * @throws IllegalArgumentException if the character is not a valid letter
     */
    @Override
    public void setContent(char c) {
        super.setContent(validateCharacter(c));
    }

    /**
     * Sets the active state of all clusters that this cell belongs to.
     *
     * <p>
     * If the cell is not part of any cluster, the method returns immediately.
     * Otherwise, it iterates through all clusters that this cell belongs to and
     * sets their active state to the specified new state.
     * </p>
     *
     * @param newState {@code true} to activate the cluster, {@code false} to
     * deactivate it.
     */
    @Override
    public void setClusterStateTo(boolean newState) {
        if (!inCluster()) {
            return;
        }

        CellCluster mainCluster = getMainCluster();
        if (mainCluster != null) {
            mainCluster.setState(newState);
            return;
        }
        
//        this.setState(newState);
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Adds the cell to a cluster. Ensures the cluster is of type
     * {@code LetterCluster} and that this cell is not already part of a
     * cluster.
     *
     * @param cluster the {@code CellCluster} to add the cell to
     * @throws IllegalArgumentException if the cluster is not a
     * {@code LetterCluster}
     * @throws IllegalStateException if the cell is already part of another
     * cluster
     */
    @Override
    public void addCluster(CellCluster cluster) {
        if (!(cluster instanceof LetterCluster)) {
            throw new IllegalArgumentException("Cannot add a letter cell to a non-letter cluster.");
        }

        if (inCluster()) {
            throw new IllegalStateException("Cannot add a LetterCell to more than one cluster");
        }

        super.addCluster(cluster);
    }

}