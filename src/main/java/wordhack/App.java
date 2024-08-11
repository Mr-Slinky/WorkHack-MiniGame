package wordhack;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import wordhack.logic.GameManager;
import wordhack.gui.AnimationPanel;
import wordhack.gui.CellPanel;

/**
 * The {@code App} class serves as the main entry point for the WordHack
 * mini-game application. It extends {@code JFrame} and is responsible for
 * initialising the main game components, setting up the user interface, and
 * launching the application.
 *
 * <p>
 * This class handles the following tasks:
 * <ul>
 * <li>Setting up the main game window.</li>
 * <li>Initialising game components such as {@code GameManager} and
 * {@code AnimationPanel}.</li>
 * <li>Adding the main game display to the animation panel.</li>
 * <li>Managing the application launch and visibility.</li>
 * </ul>
 * </p>
 *
 * <p>
 * To run the application, the {@code main} method uses
 * {@code EventQueue.invokeLater} to ensure that the UI is created on the Event
 * Dispatch Thread, which is the proper thread for Swing components.
 * </p>
 *
 * @version 3.0
 * @since   2024-07-05
 * @author  Kheagen Haskins
 */
public final class App extends JFrame {

    private static int ROWS_PER_GRID = 16;
    private static int COL_PER_GRID = 15;

    /**
     * The entry point of the application. This method is invoked by the Java
     * runtime when the program starts.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new App().launch());
    }

    /**
     * The panel responsible for handling animations and rendering the game
     * display.
     */
    private AnimationPanel animationPanel;

    /**
     * The manager responsible for handling game logic and state.
     */
    private GameManager gameManager;

    /**
     * Constructs a new {@code App} instance. This constructor initialises the
     * main game window, sets default close operation, and sets up the
     * components required for the game.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns
     * true.
     */
    public App() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setContentPane(animationPanel);
        pack();
        setLocationRelativeTo(null); // Centres the window
        setResizable(false);
    }

    /**
     * Launches the application by making the main game window visible.
     */
    private void launch() {
        setVisible(true);
    }

    /**
     * Initialises the game components, including the {@code GameManager} and
     * {@code AnimationPanel}. Adds the main game display to the animation panel
     * with a {@code BorderLayout.CENTER} layout.
     */
    private void initComponents() {
        CellPanel.setPanelCount(2);
        gameManager = new GameManager(ROWS_PER_GRID, COL_PER_GRID);
        animationPanel = new AnimationPanel(600, 600);
        animationPanel.add(gameManager.getMainGameDisplay(), BorderLayout.CENTER);
    }

}
