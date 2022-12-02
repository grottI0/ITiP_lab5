import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;

    // установка начального диапазона
    @Override
    public void getInitialRange(Rectangle2D.Double plane) {
        plane.x = -2;
        plane.y = -2;
        plane.width = 4;
        plane.height = 4;
    }

    // итеративная функция для фрактала tricorn
    @Override
    public int numIterations(double x, double y) {
        double re = 0, im = 0;
        for (int i = 0; i <= MAX_ITERATIONS; i++) {
            double nextRe = re*re - im*im + x;
            double nextIm = -2*re*im+y;

            re = nextRe;
            im = nextIm;

            if (nextRe*nextRe + nextIm*nextIm > 4) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Tricorn";
    }
}
