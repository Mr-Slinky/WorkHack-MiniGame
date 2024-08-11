package wordhack.logic;

/**
 * The {@code Difficulty} enum represents different levels of difficulty for word lists.
 * <p>
 * This enum is used to categorise word lists into five distinct difficulty levels:
 * BEGINNER, INTERMEDIATE, ADVANCED, EXPERT, and MASTER. Each level indicates the 
 * increasing complexity and challenge of the words included in the list.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     WordList wordList = WordList.getWordList(Difficulty.BEGINNER);
 * </pre>
 * </p>
 *
 * @author Kheagen Haskins
 */
public enum Difficulty {
    BEGINNER,
    INTERMEDIATE, 
    ADVANCED, 
    EXPERT, 
    MASTER
}