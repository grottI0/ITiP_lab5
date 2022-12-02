import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

// класс изменяемого изображения
public class JImageDisplay extends javax.swing.JComponent{
    public  java.awt.image.BufferedImage image;

    // конструктор
    JImageDisplay(int width, int height) {
        image = new BufferedImage(width, height, TYPE_INT_RGB);
        Dimension d = new Dimension(width, height);
        super.setPreferredSize(d);
    }

    // отрисовывает изображение
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    // изменяет цвет пикселя
    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }

    // очищает изображение
    public void clearImage() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                drawPixel(i, j, 0);
            }
        }
    }
}
