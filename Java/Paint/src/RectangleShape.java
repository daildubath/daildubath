import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serial;

/**
 * ============================================================================
 * CLASS SUMMARY: RectangleShape.java
 * ============================================================================
 * This class is a concrete implementation of the abstract Shape class, responsible
 * for representing and rendering a standard rectangular vector outline or solid fill.
 * ----------------------------------------------------------------------------
 * It uses comparative bounding-box mathematics to calculate correct widths,
 * heights, and top-left origin coordinates. This ensures the shape renders
 * correctly regardless of which direction the user drags the mouse.
 * ============================================================================
 */
// ============================================================================
// REQUIREMENT MET: Inheritance (Subclass Implementation)
// ============================================================================
// Inherits standard fields and the parent execution architecture from the base Shape class.
public class RectangleShape extends Shape {

    // Handles error of older art versions
    @Serial
    private static final long serialVersionUID = -2617763165332258324L;
    /**
     * Constructs a RectangleShape instance by routing structural configuration
     * properties directly up to the base superclass constructor layer.
     *
     * @param startX    The absolute horizontal mouse-press coordinate origin.
     * @param startY    The absolute vertical mouse-press coordinate origin.
     * @param endX      The absolute horizontal mouse-release destination.
     * @param endY      The absolute vertical mouse-release destination.
     * @param color     The specific Color profile allocated to this vector object.
     * @param thickness The stroke width mapping size allocated to the outline.
     * @param isFilled  Boolean flag indicating if the interior should be filled solid.
     */
    public RectangleShape(int startX, int startY, int endX, int endY, Color color, int thickness, boolean isFilled) {
        // Leverages inheritance properties to chain tracking initialization to the superclass
        super(startX, startY, endX, endY, color, thickness, isFilled);
    }

    /**
     * Implements the unique vector transformation algorithms required to draw
     * a mathematically sound rectangle outline within a dynamic bounding box.
     *
     * @param g2d The pre-configured Graphics2D execution engine context wrapper.
     */
    @Override
    protected void drawShape(Graphics2D g2d) {
        // ================================================================
        // REQUIREMENT MET: Conditionals / Custom Methods (Math Utility Functions)
        // ================================================================
        // Swing requires shape rendering boundaries to begin from the absolute top-left corner
        // with positive width/height values. Math primitives normalize inverted dragging inputs.

        // Identifies the absolute leftmost boundary coordinate as the layout starting anchor
        int x = Math.min(startX, endX);

        // Identifies the absolute topmost boundary coordinate as the layout starting anchor
        int y = Math.min(startY, endY);

        // Converts negative coordinate differences into an absolute scalar width value
        int width = Math.abs(startX - endX);

        // Converts negative coordinate differences into an absolute scalar height value
        int height = Math.abs(startY - endY);

        // ================================================================
        // REQUIREMENT MET: Conditionals (Fill Processing)
        // ================================================================
        // Evaluates the boolean state variable to determine the correct rendering mode.
        if (isFilled) {
            // Instructs the 2D hardware graphics engine to project a solid rectangle
            g2d.fillRect(x, y, width, height);
        } else {
            // Instructs the 2D hardware graphics engine to project a rectangle outline
            g2d.drawRect(x, y, width, height);
        }
    }
}