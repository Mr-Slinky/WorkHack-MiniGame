package wordhack.logic;

/**
 * The {@code WordBank} class provides a repository of word lists categorized by
 * difficulty levels.
 * <p>
 * It offers word lists for five difficulty levels: BEGINNER, INTERMEDIATE,
 * ADVANCED, EXPERT, and MASTER. Each word list is shuffled upon initialization
 * to ensure random order. The class also provides a method to retrieve the word
 * list for a specific difficulty level.
 * </p>
 * <p>
 * Example usage:
 * <pre>
     WordSet beginnerList = WordBank.getWordList(Difficulty.BEGINNER);
 </pre>
 * </p>
 *
 * @author Kheagen Haskins
 *
 * @see wordhack.gamelogic.WordSet
 */
public final class WordBank {

    // ---------------------------- Word Lists ------------------------------ //
    /**
     * The word list for the BEGINNER difficulty level.
     */
    private static WordSet beginnerWords = new WordSet(
            "BAKE", "BARN", "BIDE", "BARK", "BAND", "CAKE", "CART", "EARN",
            "FERN", "SIDE", "HARK", "WAKE", "YARN"
    ).shuffle();

    /**
     * The word list for the INTERMEDIATE difficulty level.
     */
    private static WordSet intermediateWords = new WordSet(
            "SPIES", "JOINS", "TIRES", "TRICK", "TRIED", "SKIES",
            "TERMS", "THIRD", "FRIES", "PRICE", "TRIES", "TRITE",
            "TANKS", "THANK", "THICK", "TRIBE", "TEXAS"
    ).shuffle();

    /**
     * The word list for the ADVANCED difficulty level.
     */
    private static WordSet advancedWords = new WordSet(
            "CONFIRM", "ROAMING", "FARMING", "GAINING", "HEARING", "MANKIND",
            "MORNING", "HEALING", "LEAVING", "CONSIST", "JESSICA", "HOUSING",
            "STERILE", "GETTING", "TACTICS", "ENGLISH", "FENCING", "KEDRICK"
    ).shuffle();

    /**
     * The word list for the EXPERT difficulty level.
     */
    private static WordSet expertWords = new WordSet(
            "EXAMPLE", "EXCLAIM", "EXPLODE", "BALCONY", "EXCERPT", "EXCITED",
            "EXCISES", "TEACHER", "IMAGINE", "HUSBAND", "TEASHOP", "TEASING",
            "TEABAGS", "FASHION", "PENGUIN", "FICTION", "FACTORY", "MONITOR",
            "FACTUAL", "FACIALS"
    ).shuffle();

    /**
     * The word list for the MASTER difficulty level.
     */
    private static WordSet masterWords = new WordSet(
            "CREATION", "DURATION", "LOCATION", "INTERNAL", "ROTATION",
            "INTEREST", "INTACTED", "REDACTED", "INTERCOM", "UNWANTED",
            "UNBROKEN", "FRAGMENT", "JUDGMENT", "SHIPMENT", "BASEMENT"
    ).shuffle();

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Returns the word list for the specified difficulty level.
     * <p>
     * This method retrieves the corresponding {@link WordSet} for the given
     * {@link Difficulty} level. If an unknown difficulty level is provided, an
     * {@link Error} is thrown.
     * </p>
     *
     * @param difficulty the difficulty level for which the word list is to be retrieved.
     * @return the {@link WordSet} corresponding to the specified difficulty
     * level.
     * @throws Error if the provided difficulty level is unknown.
     */
    public static WordSet getWordList(Difficulty difficulty) {
        switch (difficulty) {
            case BEGINNER:
                return beginnerWords;
            case INTERMEDIATE:
                return intermediateWords;
            case ADVANCED:
                return advancedWords;
            case EXPERT:
                return expertWords;
            case MASTER:
                return masterWords;
            default: // Should never run!
                throw new Error("Unknown difficulty: " + difficulty);
        }
    }

}