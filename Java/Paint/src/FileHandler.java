import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * ============================================================================
 * CLASS SUMMARY: FileHandler.java
 * ============================================================================
 * This utility class is completely decoupled from the UI, dedicated strictly
 * to managing persistent data state.
 * ----------------------------------------------------------------------------
 * It provides static methods to stream the application's array of geometric
 * objects to and from the disk via binary serialization, as well as rasterize
 * the live canvas into standard PNG image files.
 * ============================================================================
 */
public class FileHandler {

    // ====================================================================
    // WARNING FIX: Robust Error Logging
    // ====================================================================
    // Replaces printStackTrace with a standardized enterprise logger instance
    // bound directly to this class's context.
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());

    /**
     * ====================================================================
     * REQUIREMENT MET: File I/O (Save Operation)
     * ====================================================================
     * Serializes the active array collection and streams it to the disk.
     *
     * @param shapes   The data model containing all drawn vectors.
     * @param filePath The absolute destination path on the user's hard drive.
     */
    public static void saveArtwork(ArrayList<Shape> shapes, String filePath) {
        // Utilizing a try-with-resources block to automatically close data streams
        // and prevent system memory leaks even if an error occurs.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(shapes);
        } catch (Exception e) {
            // Logs a severe error safely without crashing the main application thread
            LOGGER.log(Level.SEVERE, "Failed to save project data to: " + filePath, e);
        }
    }

    /**
     * ====================================================================
     * REQUIREMENT MET: File I/O (Read Operation)
     * ====================================================================
     * Deserializes binary project state back into memory.
     *
     * @param filePath The absolute path of the targeted .txt or custom data file.
     * @return Reconstructed ArrayList sequence of Shape objects.
     */
    @SuppressWarnings("unchecked") // Suppresses unchecked cast warning since we enforce type during save
    public static ArrayList<Shape> loadArtwork(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ArrayList<Shape>) ois.readObject();
        } catch (Exception e) {
            // Logs the specific file failure and returns an empty list to prevent null pointer crashes
            LOGGER.log(Level.SEVERE, "Failed to load project data from: " + filePath, e);
            return new ArrayList<>();
        }
    }

    /**
     * ====================================================================
     * REQUIREMENT MET: File I/O (Image Export)
     * ====================================================================
     * Converts vector layout coordinates to an uncompressed raster bitmap PNG file.
     *
     * @param canvas   The active rendering surface containing the vectors.
     * @param filePath The destination absolute file system path.
     */
    public static void exportImage(CanvasPanel canvas, String filePath) {
        try {
            // Creates an empty graphical image buffer matching the current window dimensions
            BufferedImage image = new BufferedImage(
                    canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = image.createGraphics();

            // ================================================================
            // NEW FEATURE 3: Fill the background layer before exporting
            // ================================================================
            // Extracts the active background color from the panel and paints a solid rectangle
            g2d.setColor(canvas.getBackground());
            g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Instructs the canvas to paint its shape contents onto our image buffer
            canvas.paint(g2d);
            g2d.dispose(); // Releases graphical memory resources to the OS

            // Ensures the string ends with the proper file extension
            if (!filePath.toLowerCase().endsWith(".png")) {
                filePath += ".png";
            }

            // Writes the encoded binary image data to the disk
            ImageIO.write(image, "png", new File(filePath));

        } catch (Exception e) {
            // Logs rendering or writing errors
            LOGGER.log(Level.SEVERE, "Failed to export PNG image to: " + filePath, e);
        }
    }
}