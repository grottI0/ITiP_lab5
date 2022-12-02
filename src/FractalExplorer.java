import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int display;
    private JImageDisplay image;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double planeRange;

    private JComboBox<FractalGenerator> box = new JComboBox<>();

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    // конструктор
    public FractalExplorer(int display) {
        this.display = display;
        this.planeRange = new Rectangle2D.Double(0, 0, 0, 0);
        this.fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(planeRange);
    }

    // создает графический интерфейс
    public void createAndShowGUI() {
        image = new JImageDisplay(display, display);

        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());

        JLabel label = new JLabel("Fractals: ");

        JButton resetButton = new JButton("Reset");
        JButton saveButton = new JButton("Save");
        JFrame frame = new JFrame("Fractal generator");
        frame.setLayout(new BorderLayout());

        JPanel upper = new JPanel();
        upper.add(label, BorderLayout.CENTER);
        upper.add(box, BorderLayout.CENTER);

        JPanel lower = new JPanel();
        lower.add(resetButton, BorderLayout.CENTER);
        lower.add(saveButton, BorderLayout.CENTER);

        image.addMouseListener(new MouseListener());
        resetButton.addActionListener(new ResetListener());
        saveButton.addActionListener(new SaveListener());
        box.addActionListener(new BoxListener());

        frame.add(image, BorderLayout.CENTER);
        frame.add(lower, BorderLayout.SOUTH);
        frame.add(upper, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // отрисовывает фрактал
    private void drawFractal() {
        for (int x = 0; x < display; x++) {
            double xCoord = FractalGenerator.getCoord (planeRange.x, planeRange.x + planeRange.width, display, x);
            for (int y = 0; y < display; y++) {
                double yCoord = FractalGenerator.getCoord (planeRange.y, planeRange.y + planeRange.height, display, y);
                int numIterations = fractalGenerator.numIterations(xCoord, yCoord);

                if (numIterations == -1) {
                    image.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) numIterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    image.drawPixel(x, y, rgbColor);

                image.repaint();
                }
            }
        }
    }

    // отслеживает нажатия кнопки reset
    private class ResetListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            image.clearImage();
            fractalGenerator.getInitialRange(planeRange);
            drawFractal();
        }
    }

    // отслеживает нажатия кнопки save
    private class SaveListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int option = fileChooser.showSaveDialog(image);

            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(image.image, "png", fileChooser.getSelectedFile());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(image, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // отслеживает клики мыши
    private class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double x = FractalGenerator.getCoord(planeRange.x,planeRange.x + planeRange.width, display, e.getX());
            double y = FractalGenerator.getCoord(planeRange.y,planeRange.y + planeRange.width, display, e.getY());
            fractalGenerator.recenterAndZoomRange(planeRange, x, y, 0.5);
            drawFractal();
        }
    }

    // отслеживает взаимодействия с элементами ComboBox
    private class BoxListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator = (FractalGenerator) box.getSelectedItem();
            fractalGenerator.getInitialRange(planeRange);
            drawFractal();
        }
    }
}
