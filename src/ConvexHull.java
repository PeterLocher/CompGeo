import java.util.Random;
import java.util.function.Function;

public class ConvexHull {


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
