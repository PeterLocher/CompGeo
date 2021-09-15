import java.util.List;

public class Util {

    public static int orientationTest(List<Point> in) {
        float x1 = in.get(0).x, x2 = in.get(1).x, x3 = in.get(2).x;
        float y1 = in.get(0).y, y2 = in.get(1).y, y3 = in.get(2).y;
        return (int) Math.signum(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
    }

    public static int orientationTestFactorized(List<Point> in) {
        float x1 = in.get(0).x, x2 = in.get(1).x, x3 = in.get(2).x;
        float y1 = in.get(0).y, y2 = in.get(1).y, y3 = in.get(2).y;
        return (int) Math.signum(x1 * y2 - x1 * y3 + x2 * y3 - x2 * y1 + x3 * y1 - x3 * y2);
    }

    public static int orientationTestFactorizedAndCollecting(List<Point> in) {
        float x1 = in.get(0).x, x2 = in.get(1).x, x3 = in.get(2).x;
        float y1 = in.get(0).y, y2 = in.get(1).y, y3 = in.get(2).y;
        return (int) Math.signum(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }
}
