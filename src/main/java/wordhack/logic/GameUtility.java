package wordhack.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A home for various utility methods for the application
 *
 * @author Kheagen Haskins
 */
public class GameUtility {

    /**
     * Determines if a given number is prime.
     *
     * <p>
     * A prime number is a natural number greater than 1 that has no positive
     * divisors other than 1 and itself. This method uses an optimised approach
     * to check for primality. It handles small numbers with specific checks and
     * uses a loop to check divisibility for larger numbers.
     * </p>
     *
     * @param x the number to be checked for primality
     * @return {@code true} if the number is prime; {@code false} otherwise
     */
    public static boolean isPrime(int x) {
        // Check for numbers less than 2, which are not prime.
        if (x < 2) {
            return false;
        }

        // Check for 2 and 3, which are prime numbers.
        if (x <= 3) {
            return true;
        }

        // Eliminate multiples of 2 and 3 early.
        if (x % 2 == 0 || x % 3 == 0) {
            return false;
        }

        // Check for prime by testing potential factors up to the square root of x.
        double sqrt = Math.sqrt(x);
        for (int i = 5; i <= sqrt; i += 6) {
            // Check if x is divisible by i or i + 2. (6k ± 1)
            if (x % i == 0 || x % (i + 2) == 0) {
                return false;
            }
        }

        // If no factors are found, x is prime.
        return true;
    }

    /**
     * Finds all possible row and column pairs for a given array length.
     *
     * <p>
     * This method calculates all pairs of integers (rows and columns) such that
     * the product of the two integers equals the specified length. It is used
     * to determine the possible configurations of a 1D array when converting it
     * into a 2D array.</p>
     *
     * @param length the length of the 1D array
     * @return a list of integer arrays, where each integer array contains a
     * pair of possible row and column counts
     */
    public static List<int[]> findRowColumnPairs(int length) {
        // Initialise a list to store the pairs of rows and columns.
        List<int[]> pairs = new ArrayList<>();

        // Loop through all numbers from 1 to the square root of the array length.
        for (int i = 1; i <= Math.sqrt(length); i++) {
            // Check if 'i' is a divisor of 'length'.
            if (length % i == 0) {
                // Add the pair (i, length / i) to the list.
                pairs.add(new int[]{i, length / i});

                // If 'i' and 'length / i' are not the same, add the reverse pair as well.
                if (i != length / i) {
                    pairs.add(new int[]{length / i, i});
                }
            }
        }

        // Return the list of pairs.
        return pairs;
    }

    /**
     * Determines the best contrasting colour (black or white) for the given
     * colour to ensure maximum readability. This method calculates the
     * luminance of the provided colour and returns either black or white based
     * on the luminance value.
     *
     * <p>
     * The luminance is calculated using the formula:
     * <pre>
     *     luminance = 0.299 * red + 0.587 * green + 0.114 * blue
     * </pre>
     * </p>
     * <p>
     * These coefficients reflect the human eye's sensitivity to different
     * wavelengths of light, giving more weight to green, followed by red, and
     * then blue. This approach ensures that the chosen contrasting colour
     * provides optimal readability against the given background colour.
     * </p>
     * <p>
     * If the luminance is greater than 128, the method returns black as the
     * best contrasting colour; otherwise, it returns white.
     * </p>
     *
     * @param color the colour for which the best contrasting colour is to be
     * determined.
     * @return {@code Color.BLACK} if the luminance of the given colour is
     * greater than 128, otherwise {@code Color.WHITE}.
     */
    public static Color getBestContrast(Color color) {
        float lum;

        // Calculate the luminance based on the RGB components of the given colour
        lum  = 0.299f * color.getRed();
        lum += 0.587f * color.getGreen();
        lum += 0.114f * color.getBlue();

        // Return black if the luminance is greater than 128; otherwise, return white
        return lum > 128 ? Color.BLACK : Color.WHITE;
    }

}