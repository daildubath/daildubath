import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serial;

/**
 * ============================================================================
 * CLASS SUMMARY: OvalShape.java
 * ============================================================================
 * This class is a concrete implementation of the abstract Shape class, responsible
 * for representing and rendering an oval (ellipse) vector element.
 * ----------------------------------------------------------------------------
 * It uses comparative bounding-box mathematics to calculate correct widths,
 * heights, and top-left origin coordinates. This ensures the shape renders
 * correctly regardless of which direction the user drags the mouse (up, down,
 * left, or right).
 * ============================================================================
 */
// ============================================================================
// REQUIREMENT MET: Inheritance (Subclass Implementation)
// ============================================================================
// Inherits standard fields (startX, startY, endX, endY, color, thickness)
// and the parent execution architecture from the base Shape class.
public class OvalShape extends Shape {

    // Handles error of older art versions that do not include fill
    @Serial
    private static final long serialVersionUID = 6107337121512270032L;
    /**
     * Constructs an OvalShape instance by routing structural configuration
     * properties directly up to the base superclass constructor layer.
     *
     * @param startX    The absolute horizontal mouse-press coordinate origin.
     * @param startY    The absolute vertical mouse-press coordinate origin.
     * @param endX      The absolute horizontal mouse-release destination.
     * @param endY      The absolute vertical mouse-release destination.
     * @param color     The specific Color profile allocated to this vector object.
     * @param thickness The stroke width mapping size allocated to the outline.
     */
    public OvalShape(int startX, int startY, int endX, int endY, Color color, int thickness, boolean isFilled) {
        // Leverages inheritance properties to chain tracking initialization to the superclass
        super(startX, startY, endX, endY, color, thickness, isFilled);
    }

    /**
     * Implements the unique vector transformation algorithms required to draw
     * a mathematically sound oval outline within a dynamic bounding box.
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

        // Instructs the 2D hardware graphics engine to project an oval outline circumscribed
        // entirely within the calculated rectangular bounding box grid constraints.
        if (isFilled) {
            g2d.fillOval(x, y, width, height);
        } else {
            g2d.drawOval(x, y, width, height);
        }
    }
}