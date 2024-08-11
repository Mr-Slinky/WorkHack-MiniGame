package wordhack.logic;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code WordSet} class manages a list of words and provides functionality
 * for iterating through the list, resetting the cursor, and generating a n
 * interspersed string with random symbols inserted between the words.
 * <p>
 * This class ensures that the word list is not null or empty upon
 * initialisation and provides various methods for interacting with the list.
 * </p>
 *
 * @author [Student Name Here]
 */
public final class WordSet {

    // ------------------------------ Fields -------------------------------- //
    /**
     * An array of special characters used to add symbols between words.
     */
    private static char[] symbols = "!@#$%^&*()_+{}[]:;<>,?/'\\\"".toCharArray();

    /**
     * A {@link Random} instance for generating random values.
     */
    private Random random = ThreadLocalRandom.current();

    /**
     * The list of words managed by this class.
     */
    private String[] wordList;

    /**
     * A random index chosen for the correct word
     */
    private int correctWordIndex;

    /**
     * The current position in the word list.
     */
    private int cursor;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a new {@code WordSet} with the specified words.
     * <p>
     * Throws an {@link IllegalArgumentException} if the word list is null or
     * empty.
     * </p>
     *
     * @param listOfWords the words to be managed by this list.
     * @throws IllegalArgumentException if the word list is null or empty.
     */
    public WordSet(String... listOfWords) {
        if (listOfWords == null || listOfWords.length == 0) {
            throw new IllegalArgumentException("Word list cannot be empty or null");
        }

        this.wordList = listOfWords;
        correctWordIndex = random.nextInt(wordList.length);
        cursor = 0;
    }

    // ------------------------------- Getters ------------------------------ //
    /**
     * Returns the total number of characters in all words in the list.
     *
     * @return the total number of characters.
     */
    public int getTotalCharacters() {
        int sum = 0;
        for (int i = 0; i < wordList.length; i++) {
            sum += wordList[i].length();
        }
        return sum;
    }

    // --------------------------------- API -------------------------------- //

    /**
     * Provides the correct word in the word set.
     *
     * @return the correct word in the word set.
     */
    public String getCorrectWord() {
        return wordList[correctWordIndex];
    }

    /**
     * Shuffles the words in the word list using the Fisher-Yates algorithm.
     * <p>
     * This method implements the modern version of the Fisher-Yates shuffle
     * algorithm, also known as the Knuth shuffle. It ensures each permutation
     * of the array elements is equally probable. The method iterates through
     * the array from the first element to the last, swapping each element with
     * a randomly selected one from the range of unshuffled elements. By
     * gradually reducing the range of the random selection, the algorithm
     * prevents elements from being shuffled multiple times, which would
     * compromise the uniformity of the shuffle. Don't worry, I didn't get it
     * either...
     * </p>
     *
     * @return the {@code WordSet} object with its words rearranged in a random
     * order. The method modifies the original {@code WordSet} object and
     * returns the same reference for chaining or further manipulation.
     */
    public WordSet shuffle() {
        int ri; // Variable to hold the random index for swapping
        for (int i = 0; i < wordList.length; i++) { // Iterate through each element in the array
            // Generate a random index from the current position to the end of the array
            ri = random.nextInt(i, wordList.length);

            // Swap the current element with the element at the random index
            String temp = wordList[i];  // Temporary storage for the current element
            wordList[i] = wordList[ri]; // Place the randomly chosen element at the current position
            wordList[ri] = temp;        // Move the element from the current position to the random position
        }
        return this; // Return the same WordSet object with its elements now shuffled
    }

    /**
     * Generates a string with the specified size, inserting random symbols
     * between the words.
     *
     * <p>
     * The generated string includes each word from the list with random symbols
     * added between them to reach the specified size.
     * </p>
     *
     * @param size the desired size of the game string.
     * @return the generated game string.
     */
    public String jumble(int size) {
        int totalChar = getTotalCharacters();
        // Check if the specified size is smaller than the total number of characters in all words combined.
        if (size < totalChar) {
            throw new IllegalArgumentException("Size " + size + " is too small for total character length of " + totalChar);
        }

        // Calculate how many additional symbols must be used in addition to the words to meet the specified size.
        int symbolCurrency = size - totalChar;
        // Determine how many symbols should be added on average between each word.
        int symbolCountPerGap = symbolCurrency / wordList.length;

        StringBuilder outp = new StringBuilder();
        int leftHalfBound, rightHalfBound;

        // Iterate through each word in the list to add it to the output with symbols before and after it.
        for (String word : wordList) {
            // Randomly determine the number of symbols to add before the current word. This is a random split of the gap.
            leftHalfBound = random.nextInt(symbolCountPerGap);
            // Calculate remaining symbols to place after the word by subtracting the first part from the total gap.
            rightHalfBound = symbolCountPerGap - leftHalfBound;

            addSymbols(outp, leftHalfBound);  // Add a random number of symbols before the word
            outp.append(word);                // Add the word itself to the output
            addSymbols(outp, rightHalfBound); // Add the remaining symbols after the word
        }

        // If there are any leftover symbols after distributing them evenly, add them at the end of the string.
        addSymbols(outp, symbolCurrency % wordList.length);

        return outp.toString();
    }

    // --------------------------- Helper Methods --------------------------- //
    /**
     * Adds the specified number of random symbols to the given
     * {@link StringBuilder}.
     *
     * @param str the {@link StringBuilder} to append symbols to.
     * @param amount the number of symbols to add.
     */
    private void addSymbols(StringBuilder str, int amount) {
        for (int i = 0; i < amount; i++) {
            str.append(symbols[random.nextInt(symbols.length)]);
        }
    }

}