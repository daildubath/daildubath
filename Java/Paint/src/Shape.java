import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serial; // NEW: Imported to explicitly support compile-time checking
import java.io.Serializable;

/**
 * ============================================================================
 * CLASS SUMMARY: Shape.java
 * ============================================================================
 * This abstract class serves as the core foundational blueprint for the
 * applications' object-oriented vector drawing model.
 * ============================================================================
 */
public abstract class Shape implements Serializable {

    // ====================================================================
    // WARNING FIX: Modern Compilation Verification
    // ====================================================================
    // The @Serial annotation ensures the field name, modifiers, and types
    // strictly conform to the language serialization specification protocol requirements.
    @Serial
    private static final long serialVersionUID = 5924474061133299053L;

    protected int startX, startY, endX, endY;
    protected Color color;
    protected int thickness;
    protected boolean isFilled;

    /**
     * Common constructor ensuring every subclass instance instantiates with
     * explicit layout, color, and stroke weight properties.
     */
    public Shape(int startX, int startY, int endX, int endY, Color color, int thickness, boolean isFilled) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.thickness = thickness;
        this.isFilled = isFilled;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawShape(g2d);
    }

    protected abstract void drawShape(Graphics2D g2d);
}