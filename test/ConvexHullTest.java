import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConvexHullTest {
    static float x = 0, y = 0;

    public static float[] nextPoint() {
        float[] point = {x, y};
        x = x + (float) Math.random();
        y = (float) Math.pow(x, 2);
        return point;
    }

    public static void resetPointGen() {
        x = 0;
        y = 0;
    }

    @Test
    void testAllOrientationTests() {
        testOrientation();
        resetPointGen();
        testOrientation2();
        resetPointGen();
        testOrientation3();
    }

    @Test
    void testOrientation() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            counter += ConvexHull.orientationTest(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testOrientation2() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            //System.out.println(in[2][0]);
            counter += ConvexHull.orientationTestFactorized(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testOrientation3() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            //System.out.println(in[2][0]);
            counter += ConvexHull.orientationTestFactorizedAndCollecting(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testGrahamScan() {

    }

}