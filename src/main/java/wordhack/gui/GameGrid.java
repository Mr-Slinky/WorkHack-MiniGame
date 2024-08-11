package wordhack.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import wordhack.gui.cells.CellManager;
import wordhack.gui.cells.DefaultCellManager;
import wordhack.logic.GameManager;
import wordhack.logic.GameUtility;

/**
 *
 * @author Kheagen Haskins
 */
public final class GameGrid extends GamePanel {

    // ------------------------------ Fields -------------------------------- //
    private GameManager observer;
    private CellManager cellManager;
    private GridPrefix[] panelPrefixes;
    private CellPanel[] panels;
    private int rows, cols;
    private char[] cArr;

    // --------------------------- Constructors ----------------------------- //
    public GameGrid(int totalRows, int cols, char[] characters) {
        if (characters.length != totalRows * cols) {
            throw new IllegalArgumentException("Characters array incorrect length at " + characters.length + ", expected " + (totalRows * cols));
        }

        this.rows = totalRows;
        this.cols = cols;
        this.cArr = characters;

        initComponents();
    }

    public GameGrid(char[] characters) {

        final int desiredRowCountPerGrid = 16;
        int[] pair = getBestPairFor(desiredRowCountPerGrid, characters.length);
        rows = pair[0];
        cols = pair[1];
        cArr = characters;

        initComponents();
    }

    // ------------------------------ Setters ------------------------------- //
    public void setObserver(GameManager observer) {
        this.observer = observer;
    }

    // ---------------------------- API Methods ----------------------------- //
    public String removeDud(String correctWord) {
        return cellManager.removeDud(correctWord);
    }

    // -------------------------- Helper Methods ---------------------------- //
    private void initComponents() {
        final int gap = 10;
        final int panelCount = CellPanel.count();
        int rowsPerPanel = rows / panelCount; // rows per panel

        cellManager = new DefaultCellManager(rows, cols, cArr);
        panelPrefixes = new GridPrefix[panelCount];
        panels = new CellPanel[panelCount];

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        for (int i = 0; i < panelCount; i++) {
            // Initialise panel prefixes
            if (i == 0) {
                panelPrefixes[i] = new GridPrefix(rowsPerPanel);
            } else {
                panelPrefixes[i] = new GridPrefix(rowsPerPanel, panelPrefixes[i - 1].getLastHexValue());
            }

            // Create and configure cell panels
            CellPanel panel = new CellPanel(cellManager.getAllCells());
            panels[i] = panel;
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    processPanelClick(panel);
                }
            });

            // Add components to the layout
            add(panelPrefixes[i]);
            add(Box.createHorizontalStrut(gap));
            add(panel);

            // Add a gap between panels, except after the last panel
            if (i != panelCount - 1) {
                add(Box.createHorizontalStrut(gap));
            }
        }
    }

    private void processPanelClick(CellPanel panel) {
        if (observer == null) {
            throw new IllegalStateException("No observer has been registered for this GameGrid");
        }

        String text = panel.getSelectedText();
        if (text == null) {
            return;
        }
        panel.dissolveLastActiveCluster();
        observer.handleUserGuess(text);
    }

    private int[] getBestPairFor(int desiredRowLength, int length) {
        if (GameUtility.isPrime(length)) {
            throw new IllegalArgumentException("Cannot find row and column pairs for prime number: " + length);
        }

        // Gets all possible combinations of rows and columns
        List<int[]> pairs = GameUtility.findRowColumnPairs(length);
        int rowNum, distance;
        int bestPairIndex = 0;
        int prevDistance = Integer.MAX_VALUE;

        for (int i = 0; i < pairs.size(); i++) {
            int[] pair = pairs.get(i);
            rowNum = pair[0];
            distance = Math.abs(desiredRowLength - rowNum);
            if (distance < prevDistance && distance % 2 == 0) {
                bestPairIndex = i;
                prevDistance = distance;
            }
        }

        return pairs.get(bestPairIndex);
    }

}
