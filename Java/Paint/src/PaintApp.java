import javax.swing.*;
import java.awt.*;

/**
 * ============================================================================
 * CLASS SUMMARY: PaintApp.java
 * ============================================================================
 * This class serves as the main entry point and user interface controller for
 * the application. It acts as the "View" and "Controller" in an MVC-style setup.
 * ============================================================================
 */
class PaintApp {

    void main() {
        SwingUtilities.invokeLater(() -> new PaintApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // --- WINDOW SETUP ---
        JFrame frame = new JFrame("Simple Paint - Pro Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600); // Slightly wider to fit all our awesome new tools!
        frame.setLayout(new BorderLayout());

        CanvasPanel canvas = new CanvasPanel();

        // --- MULTI-ROW TOOLBAR LAYOUT ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JToolBar mainTools = new JToolBar();
        mainTools.setFloatable(false);

        JToolBar propertyTools = new JToolBar();
        propertyTools.setFloatable(false);

        // --- BUTTON INSTANTIATION (ROW 1) ---
        JButton btnFreehand = new JButton("Freehand");
        JButton btnLine = new JButton("Line");
        JButton btnRect = new JButton("Rectangle");
        JButton btnOval = new JButton("Oval");
        JButton btnTriangle = new JButton("Triangle");

        JButton btnUndo = new JButton("Undo");
        JButton btnRedo = new JButton("Redo");

        JButton btnSave = new JButton("Save Project");
        JButton btnLoad = new JButton("Open Project");
        JButton btnExport = new JButton("Export to PNG");

        JFileChooser fileChooser = new JFileChooser();

        // --- ACTION LISTENERS ---
        btnFreehand.addActionListener(_ -> canvas.setCurrentTool("Freehand"));
        btnLine.addActionListener(_ -> canvas.setCurrentTool("Line"));
        btnRect.addActionListener(_ -> canvas.setCurrentTool("Rectangle"));
        btnOval.addActionListener(_ -> canvas.setCurrentTool("Oval"));
        btnTriangle.addActionListener(_ -> canvas.setCurrentTool("Triangle"));

        btnUndo.addActionListener(_ -> canvas.undo());
        btnRedo.addActionListener(_ -> canvas.redo());

        btnSave.addActionListener(_ -> {
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                FileHandler.saveArtwork(canvas.getShapes(), fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        btnLoad.addActionListener(_ -> {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                canvas.setShapes(FileHandler.loadArtwork(fileChooser.getSelectedFile().getAbsolutePath()));
            }
        });

        btnExport.addActionListener(_ -> {
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                FileHandler.exportImage(canvas, fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(frame, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // --- BACKGROUND COLOR CONTROLS (ROW 1) ---
        JButton btnBgColorPicker = createBgColorPickerButton(frame, canvas);
        JButton btnBgHexPicker = createBgHexButton(frame, canvas);

        // --- LAYOUT ROW 1 CONSTRUCTION ---
        mainTools.add(btnFreehand); mainTools.add(btnLine); mainTools.add(btnRect); mainTools.add(btnOval); mainTools.add(btnTriangle);
        mainTools.addSeparator();
        mainTools.add(btnUndo); mainTools.add(btnRedo);
        mainTools.addSeparator();
        mainTools.add(btnBgColorPicker);
        mainTools.add(btnBgHexPicker);
        mainTools.addSeparator();
        mainTools.add(btnSave); mainTools.add(btnLoad); mainTools.add(btnExport);

        // --- DRAWING COLOR PALETTE & CONTROLS (ROW 2) ---
        propertyTools.add(new JLabel(" Draw Color: "));

        Color[] paletteColors = {
                Color.BLACK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE,
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.MAGENTA, Color.PINK, Color.CYAN
        };

        for (Color c : paletteColors) {
            JButton colorBtn = new JButton();
            colorBtn.setBackground(c);
            colorBtn.setPreferredSize(new Dimension(25, 25));
            colorBtn.setMaximumSize(new Dimension(25, 25));
            colorBtn.setOpaque(true);
            colorBtn.setBorderPainted(false);
            colorBtn.addActionListener(_ -> canvas.setCurrentColor(c));
            propertyTools.add(colorBtn);
        }

        // --- TOOL COLOR CONTROLS (ROW 2) ---
        JButton btnToolColorPicker = createToolColorPickerButton(frame, canvas);
        JButton btnToolHex = createHexButton(frame, canvas);

        propertyTools.add(Box.createHorizontalStrut(5));
        propertyTools.add(btnToolColorPicker);
        propertyTools.add(btnToolHex);
        propertyTools.addSeparator();

        // --- STROKE THICKNESS CONTROL ---
        propertyTools.add(new JLabel(" Thickness: "));
        JSlider thicknessSlider = new JSlider(1, 20, 2);
        thicknessSlider.setMaximumSize(new Dimension(100, 30));
        thicknessSlider.addChangeListener(_ -> canvas.setCurrentThickness(thicknessSlider.getValue()));
        propertyTools.add(thicknessSlider);

        propertyTools.add(Box.createHorizontalStrut(5));

        // --- OPACITY CONTROL ---
        propertyTools.add(new JLabel(" Opacity: "));
        JSlider opacitySlider = new JSlider(0, 255, 255);
        opacitySlider.setMaximumSize(new Dimension(100, 30));
        opacitySlider.addChangeListener(_ -> canvas.setCurrentOpacity(opacitySlider.getValue()));
        propertyTools.add(opacitySlider);

        propertyTools.add(Box.createHorizontalStrut(5));

        // --- SHAPE FILL TOGGLE ---
        JCheckBox fillBox = new JCheckBox("Fill Shape");
        fillBox.addActionListener(_ -> canvas.setCurrentFill(fillBox.isSelected()));
        propertyTools.add(fillBox);

        // --- VIEW ASSEMBLY ---
        topPanel.add(mainTools);
        topPanel.add(propertyTools);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ====================================================================
    // HELPER METHODS: UI COMPONENT DELEGATION
    // ====================================================================

    /** Creates a visual JColorChooser for the active drawing tool color. */
    private JButton createToolColorPickerButton(JFrame frame, CanvasPanel canvas) {
        JButton btnColorPicker = new JButton("Custom Color...");
        btnColorPicker.addActionListener(_ -> {
            Color newColor = JColorChooser.showDialog(frame, "Select Drawing Color", Color.BLACK);
            if (newColor != null) {
                canvas.setCurrentColor(newColor);
            }
        });
        return btnColorPicker;
    }

    /** Creates a text-based Hex Input dialog for the active drawing tool color. */
    private JButton createHexButton(JFrame frame, CanvasPanel canvas) {
        JButton btnHex = new JButton("Tool Hex...");
        btnHex.addActionListener(_ -> {
            String hexInput = JOptionPane.showInputDialog(frame, "Enter Hex Color for Brush (e.g., #FF0000):");
            if (hexInput != null && !hexInput.trim().isEmpty()) {
                try {
                    if (!hexInput.startsWith("#")) hexInput = "#" + hexInput;
                    canvas.setCurrentColor(Color.decode(hexInput));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Hex format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return btnHex;
    }

    /** Creates a visual JColorChooser for the canvas background color. */
    private JButton createBgColorPickerButton(JFrame frame, CanvasPanel canvas) {
        JButton btnBgColor = new JButton("Bg Color...");
        btnBgColor.addActionListener(_ -> {
            Color newBg = JColorChooser.showDialog(frame, "Select Canvas Background", canvas.getBackground());
            if (newBg != null) {
                canvas.setBackground(newBg);
                canvas.repaint();
            }
        });
        return btnBgColor;
    }

    /** Creates a text-based Hex Input dialog for the canvas background color. */
    private JButton createBgHexButton(JFrame frame, CanvasPanel canvas) {
        JButton btnBgHex = new JButton("Bg Hex...");
        btnBgHex.addActionListener(_ -> {
            String hexInput = JOptionPane.showInputDialog(frame, "Enter Hex Color for Background (e.g., #FF0000):");
            if (hexInput != null && !hexInput.trim().isEmpty()) {
                try {
                    if (!hexInput.startsWith("#")) hexInput = "#" + hexInput;
                    canvas.setBackground(Color.decode(hexInput));
                    canvas.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Hex format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return btnBgHex;
    }
}