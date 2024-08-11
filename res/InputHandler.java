package wordhack.logic;

/**
 *
 * @author [Student Name Here]
 */
public class InputHandler {

    // ------------------------- DO NOT MODIFY THIS CODE ----------------------
    public static int guesses;                   //                            |
    public static boolean resetGuesses;          //                            |
    public static boolean removeDud;             //                            |
    public static String correctWord;            //                            |
    // ________________________________________________________________________|

    /**
     * [Explain your solution here]
     *
     * @param text the text the user clicked on in the game
     */
    public static void processTextSelected(String text) {
        System.out.println("> " + text);
        // WRITE YOUR LOGIC BELOW THIS LINE: ▼
        if (!isWord(text)) {
            if (text.length() == 1) {
                System.out.println("> Error!");
            } else {
                triggerRandomEvent();
            }
            
            return;
        }
        
        processGuess(text);
    }

    private static void triggerRandomEvent() {
        if (Math.random() < .2) {
            resetGuesses = true;
            System.out.println("> Attempts reset");
        } else {
            removeDud = true;
            System.out.println("> Dud removed");
        }
    }

    private static void processGuess(String guess) {
        int s = calculateSimilarity(guess);
        boolean guessCorrect = s == correctWord.length();
        if (guessCorrect) {
            System.out.println("> Correct! You Win!!");
            System.out.println("> Accessing System...");
            System.exit(0);
        } else if (guesses == 0) {
            System.out.println("> Incorrect!");
            System.out.println("> System Locking...");
            System.exit(0);
        } else {
            System.out.println("> " + s + " / " + correctWord.length() + " correct");
            guesses++;
        }
    }

    private static boolean isWord(String userGuess) {
        for (char c : userGuess.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private static int calculateSimilarity(String userGuess) {
        int n = Math.min(correctWord.length(), userGuess.length());
        int s = 0;
        for (int i = 0; i < n; i++) {
            if (correctWord.charAt(i) == userGuess.charAt(i)) {
                s++;
            }
        }
        return s;
    }

}