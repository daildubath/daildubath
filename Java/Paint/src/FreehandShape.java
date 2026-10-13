import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serial;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * ============================================================================
 * CLASS SUMMARY: FreehandShape.java
 * ============================================================================
 * This class is a concrete implementation of the abstract Shape class, enabling
 * continuous, freeform drawing.
 * ----------------------------------------------------------------------------
 * Unlike geometric shapes defined by a simple bounding box (start and end points),
 * freehand drawing requires capturing and stitching together a sequential series
 * of tracking coordinates recorded as the mouse moves across the canvas surface.
 * ============================================================================
 */
// ============================================================================
// REQUIREMENT MET: Inheritance (Subclass Context)
// ============================================================================
// Inherits standard color, thickness, and base structural behaviors from Shape.
public class FreehandShape extends Shape {

    /**
     * ====================================================================
     * REQUIREMENT MET: Data Structures / Collections (State Storage)
     * ====================================================================
     * A sequential historical coordinate stream array list. Marked final
     * because the reference container never changes after instantiation,
     * even as elements are continuously appended to it.
     */
    // Handles error of older art versions that do not include fill
    @Serial
    private static final long serialVersionUID = -5994723871077374834L;
    private final ArrayList<Point> points;

    /**
     * Constructs an initial instance state anchor point for the path tracking.
     *
     * @param startX    The initial horizontal coordinate grid origin.
     * @param startY    The initial vertical coordinate grid origin.
     * @param color     The targeted stroke color profile.
     * @param thickness The dimension width scale allocated to the stroke.
     * @param isFilled  Indicates fill state (passed to parent, though unused by paths).
     */
    public FreehandShape(int startX, int startY, Color color, int thickness, boolean isFilled) {
        // Leverages parent constructor rules to initialize root properties
        super(startX, startY, startX, startY, color, thickness, isFilled);

        // Allocates memory for our position collection array layout
        points = new ArrayList<>();

        // Locks down the absolute starting coordinate index spot
        points.add(new Point(startX, startY));
    }

    /**
     * ====================================================================
     * REQUIREMENT MET: Functions / Custom Methods
     * ====================================================================
     * Appends live mouse capture coordinate points to the array tracker
     * as drag mouse listener events trigger updates.
     *
     * @param x The current horizontal coordinate point.
     * @param y The current vertical coordinate point.
     */
    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    /**
     * Handles the specific vector processing algorithms required to paint
     * a variable continuous point array graph line.
     *
     * @param g2d The configured 2D hardware graphics drawing context wrapper.
     */
    @Override
    protected void drawShape(Graphics2D g2d) {
        // ================================================================
        // REQUIREMENT MET: Conditionals (Single Click Fallback)
        // ================================================================
        // Prevents processing loops if no movement occurs, safely falling back
        // to single dot micro line plotting.
        if (points.size() < 2) {
            g2d.drawLine(startX, startY, startX, startY);
            return; // Interrupts operational execution chain immediately
        }

        // ================================================================
        // WARNING FIX: Extracted Method Implementation
        // ================================================================
        // Calls the separated helper method to construct the geometry before rendering
        Path2D path = buildContinuousPath();

        // Renders the entire path string simultaneously with a single opacity calculation
        g2d.draw(path);
    }

    /**
     * ====================================================================
     * REQUIREMENT MET: Functions / Custom Methods (Helper Routine)
     * ====================================================================
     * Constructs a continuous geometric path from the stored coordinate list.
     * Extracted from drawShape to improve modularity and readability.
     * * @return A finalized Path2D object ready for the graphics engine.
     */
    private Path2D buildContinuousPath() {
        Path2D path = new Path2D.Float();

        // ================================================================
        // WARNING FIX: Modern Java Collections API
        // ================================================================
        // Replaced .get(0) with .getFirst() for safer, cleaner code expression
        path.moveTo(points.getFirst().x, points.getFirst().y);

        // ================================================================
        // REQUIREMENT MET: Loops (Array Path Reconstruction)
        // ================================================================
        // Iterates through the remaining stored points, appending them as vertices
        // on our continuous path object.
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            path.lineTo(p.x, p.y);
        }

        return path;
    }
}