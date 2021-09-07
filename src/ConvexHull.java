import java.util.Random;
import java.util.function.Function;

public class ConvexHull {


    public static void main(String[] args) {
        testOrientation();
        testOrientation2();
        testOrientation3();
    }

    private static void testOrientation() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            counter += orientationTest(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    private static void testOrientation2() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            //System.out.println(in[2][0]);
            counter += orientationTestFactorized(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    private static void testOrientation3() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            float[][] in = new float[3][2];
            in[0] = nextPoint();
            in[1] = nextPoint();
            in[2] = nextPoint();
            //System.out.println(in[2][0]);
            counter += orientationTestFactorizedAndCollecting(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }



    static float x = 0, y = 0;

    public static float[] nextPoint() {
        float[] point = {x, y};
        x = x + (float) Math.random();
        y = (float) Math.pow(x, 2);
        return point;
    }


    public static int orientationTest(float[][] in) {
        float x1 = in[0][0], x2 = in[1][0], x3 = in[2][0];
        float y1 = in[0][1], y2 = in[1][1], y3 = in[2][1];
        return (int) Math.signum(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
    }

    public static int orientationTestFactorized(float[][] in) {
        float x1 = in[0][0], x2 = in[1][0], x3 = in[2][0];
        float y1 = in[0][1], y2 = in[1][1], y3 = in[2][1];
        return (int) Math.signum(x1 * y2 - x1 * y3 + x2 * y3 - x2 * y1 + x3 * y1 - x3 * y2);
    }

    public static int orientationTestFactorizedAndCollecting(float[][] in) {
        float x1 = in[0][0], x2 = in[1][0], x3 = in[2][0];
        float y1 = in[0][1], y2 = in[1][1], y3 = in[2][1];
        return (int) Math.signum(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }

    public float[][] convex(float[][] in) {
        float[][] out = new float[1][];
        return out;
    }

}
