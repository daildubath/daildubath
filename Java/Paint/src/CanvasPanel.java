import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * ============================================================================
 * CLASS SUMMARY: CanvasPanel.java
 * ============================================================================
 * This class serves as the core rendering engine and mouse input event center
 * for the application. Inheriting from JPanel, it manages the central drawing surface.
 * ----------------------------------------------------------------------------
 * It coordinates interaction states (active tools, selected colors, stroke thickness),
 * intercepts mouse movements to build live vectors, and maintains full tracking collections
 * (history list and undo/redo stacks). It also overrides paintComponent to ensure smooth,
 * object-oriented canvas redraws.
 * ============================================================================
 */
// ============================================================================
// REQUIREMENT MET: Inheritance (UI Component Subclassing)
// ============================================================================
public class CanvasPanel extends JPanel {

    // ====================================================================
    // REQUIREMENT MET: Data Structures / Collections (State History tracking)
    // ====================================================================
    // Dynamic collections holding persistent vector shapes and undo historical logs.
    private ArrayList<Shape> shapes;

    // WARNING FIX: 'redoStack' may be 'final' because the storage container stays constant
    private final ArrayList<Shape> redoStack;

    // --- STATE VARIABLES ---
    private Shape currentShape;       // The temporary vector object actively being dragged
    private Color currentColor;       // Active color value to apply to new shapes
    private String currentTool;       // Active tool selection descriptor string
    private int currentThickness = 2; // Active stroke thickness value, defaults to 2px
    private int currentOpacity = 255;
    private boolean currentFill = false;


    /**
     * Initializes the canvas workspace and configures event listeners.
     */
    public CanvasPanel() {
        setBackground(Color.WHITE); // Establishes default base surface color
        shapes = new ArrayList<>();
        redoStack = new ArrayList<>();
        currentColor = Color.BLACK; // Fallback initial color assignment
        currentTool = "Freehand";   // Fallback initial tool utility configuration

        // Registers custom input event listeners
        setupMouseListeners();
    }

    public void setCurrentFill(boolean fill) { this.currentFill = fill; }

    /**
     * ====================================================================
     * REQUIREMENT MET: Functions / Custom Methods (State Modification)
     * ====================================================================
     * Pops the last drawn element out of the rendering collection list
     * and shifts it into the redo stack history container.
     */
    public void undo() {
        // ================================================================
        // REQUIREMENT MET: Conditionals (Collection Size Verification)
        // ================================================================
        if (!shapes.isEmpty()) {
            // WARNING FIX: Replaced with removeLast() call for safety and compliance
            Shape lastShape = shapes.removeLast();
            redoStack.add(lastShape);
            repaint(); // Requests system level repaint processing refresh
        }
    }

    /**
     * Pops the top element out of the redo history log collection
     * and restores it to the active rendering list layer.
     */
    public void redo() {
        // ================================================================
        // REQUIREMENT MET: Conditionals (Collection Size Verification)
        // ================================================================
        if (!redoStack.isEmpty()) {
            // WARNING FIX: Replaced with removeLast() call for safety and compliance
            Shape nextShape = redoStack.removeLast();
            shapes.add(nextShape);
            repaint(); // Triggers canvas layout pixel refresh
        }
    }

    // --- ENCAPSULATED UTILITY INTERFACE SETTERS & GETTERS ---
    public void setCurrentTool(String tool) { this.currentTool = tool; }
    public void setCurrentColor(Color color) { this.currentColor = color; }
    public void setCurrentThickness(int thickness) { this.currentThickness = thickness; }

    // NEW FEATURE 1: Encapsulated setter to update alpha tracking from the UI slider
    public void setCurrentOpacity(int opacity) { this.currentOpacity = opacity; }

    public ArrayList<Shape> getShapes() { return shapes; }

    /**
     * Overwrites the active element array with a newly provided list container.
     * Used primarily to restore saved project data models from disk.
     *
     * @param loadedShapes Reconstructed sequence read from persistent store.
     */
    public void setShapes(ArrayList<Shape> loadedShapes) {
        this.shapes = loadedShapes;
        this.redoStack.clear(); // Wipes alternate timelines to prevent data sequence conflicts
        repaint();
    }

    /**
     * ====================================================================
     * REQUIREMENT MET: Functions / Custom Methods (Event Binding)
     * ====================================================================
     * Constructs and attaches event listeners to intercept mouse actions.
     */
    private void setupMouseListeners() {
        // Concrete inline adapter implementation mapping targeted interaction events
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // NEW FEATURE 1: Extracts the RGB values from the currently selected palette color
                // and merges them with the live opacity slider value to create an alpha-enabled Color profile.
                Color shapeColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentOpacity);

                // ================================================================
                // REQUIREMENT MET: Conditionals (Switch Expressions)
                // ================================================================
                // Evaluates the active tool string value and initializes the matching
                // polymorphic shape subclass constructor with the newly generated alpha color.
                switch (currentTool) {
                    // UPDATED: All constructors now receive the `currentFill` boolean parameter
                    case "Line" -> currentShape = new LineShape(x, y, x, y, shapeColor, currentThickness, currentFill);
                    case "Rectangle" -> currentShape = new RectangleShape(x, y, x, y, shapeColor, currentThickness, currentFill);
                    case "Oval" -> currentShape = new OvalShape(x, y, x, y, shapeColor, currentThickness, currentFill);
                    case "Triangle" -> currentShape = new TriangleShape(x, y, x, y, shapeColor, currentThickness, currentFill);
                    case "Freehand" -> currentShape = new FreehandShape(x, y, shapeColor, currentThickness, currentFill);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // ================================================================
                // REQUIREMENT MET: Conditionals (Active Vector Tracking)
                // ================================================================
                if (currentShape != null) {
                    // Type-checks shape identity to safely call specialized subclass interfaces
                    if (currentShape instanceof FreehandShape) {
                        ((FreehandShape) currentShape).addPoint(e.getX(), e.getY());
                    } else {
                        // Updates bounds dimensions for standard geometric vectors
                        currentShape.endX = e.getX();
                        currentShape.endY = e.getY();
                    }
                    repaint(); // Requests a real-time redraw to display drag preview lines
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // ================================================================
                // REQUIREMENT MET: Conditionals (Interaction Finalization)
                // ================================================================
                if (currentShape != null) {
                    if (!(currentShape instanceof FreehandShape)) {
                        currentShape.endX = e.getX();
                        currentShape.endY = e.getY();
                    }
                    shapes.add(currentShape); // Permanently records the completed vector shape
                    currentShape = null;       // Disposes temporary tracking reference pointer
                    redoStack.clear();        // Clearing redo state blocks historical sequence splits
                    repaint();
                }
            }
        };

        // Registers the composite helper handler to match window container system hooks
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    /**
     * Coordinates canvas rendering. Overrides the native component view layer manager.
     *
     * @param g Base hardware pipeline graphics context stream interface.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Performs standard clean background initialization sweeps

        // ====================================================================
        // REQUIREMENT MET: Loops (Batch Vector Rendering Pipeline)
        // ====================================================================
        // Iterates through every recorded shape object stored within the history
        // collection, invoking its custom polymorphic draw logic sequentially.
        for (Shape shape : shapes) {
            shape.draw(g); // Automatically processes internal context updates and line painting
        }

        // Renders real-time guidance line previews for objects currently being drawn
        if (currentShape != null) {
            currentShape.draw(g);
        }
    }
}