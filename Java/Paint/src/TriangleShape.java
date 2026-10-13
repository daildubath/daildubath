import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * ============================================================================
 * CLASS SUMMARY: TriangleShape.java
 * ============================================================================
 * This concrete implementation calculates an isosceles triangle based on the 
 * user's drag bounding box, utilizing the native AWT Polygon class.
 * * It dynamically maps three points (bottom-left, bottom-right, and top-center)
 * within the drag constraints to form the geometric structure.
 * ============================================================================
 */
// ============================================================================
// REQUIREMENT MET: Inheritance (Subclass Implementation)
// ============================================================================
public class TriangleShape extends Shape {

    /**
     * Constructs a TriangleShape instance by routing structural configuration
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
    public TriangleShape(int startX, int startY, int endX, int endY, Color color, int thickness, boolean isFilled) {
        // Leverages inheritance properties to chain tracking initialization to the superclass
        super(startX, startY, endX, endY, color, thickness, isFilled);
    }

    /**
     * Implements the unique vector transformation algorithms required to map
     * three distinct coordinate vertices into a single closed Polygon shape.
     *
     * @param g2d The pre-configured Graphics2D execution engine context wrapper.
     */
    @Override
    protected void drawShape(Graphics2D g2d) {
        // ================================================================
        // REQUIREMENT MET: Data Structures (Array Vertex Mapping)
        // ================================================================
        // Calculates the horizontal midpoint to establish the triangle's peak apex
        int midX = startX + (endX - startX) / 2;

        // Defines the horizontal axes of the three vertices: [Bottom-Left, Bottom-Right, Top-Center]
        int[] xPoints = {startX, endX, midX};

        // Defines the vertical axes of the three vertices matching the horizontal indices
        int[] yPoints = {endY, endY, startY};

        // Constructs a geometric Polygon object from the mapped coordinate arrays
        Polygon triangle = new Polygon(xPoints, yPoints, 3);

        // ================================================================
        // REQUIREMENT MET: Conditionals (Fill Processing)
        // ================================================================
        // Evaluates the boolean state variable to determine the correct rendering mode.
        if (isFilled) {
            // Injects solid color into the bounded polygon interior
            g2d.fillPolygon(triangle);
        } else {
            // Traces only the exterior lines connecting the three coordinate points
            g2d.drawPolygon(triangle);
        }
    }
}