package wordhack;

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
            
        guesses--;
    }

}