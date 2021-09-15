import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ConvexHullTest {

    static float x = 0, y = 0;

    public static Point nextPoint() {
        Point point = new Point(x, y);
        x = x + (float) Math.random();
        y = (float) Math.pow(x, 2);
        return point;
    }

    public static void resetPointGen() {
        x = 0;
        y = 0;
    }

    static List<List<Point>> testCasesSquare100 = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            List<Point> pointCloud = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                pointCloud.add(new Point(random.nextFloat()*1000, random.nextFloat()*1000));
            }
            testCasesSquare100.add(pointCloud);
        }
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
            List<Point> in = new ArrayList<>();
            in.add(nextPoint());
            in.add(nextPoint());
            in.add(nextPoint());
            counter += Util.orientationTest(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testOrientation2() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            List<Point> in = new ArrayList<>();
            in.add(nextPoint());
            in.add(nextPoint());
            in.add(nextPoint());
            //System.out.println(in[2][0]);
            counter += Util.orientationTestFactorized(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testOrientation3() {
        int size = 1000;
        int counter = 0;
        for (int i = 0; i < size; i++) {
            List<Point> in = new ArrayList<>();
            in.add(nextPoint());
            in.add(nextPoint());
            in.add(nextPoint());
            //System.out.println(in[2][0]);
            counter += Util.orientationTestFactorizedAndCollecting(in) == 1 ? 1 : 0;
        }
        System.out.println(counter);
    }

    @Test
    void testGrahamScan() {
        GrahamScan grahamScan = new GrahamScan();
        grahamScan.convex(new ArrayList<>());
        for (List<Point> pointCloud : testCasesSquare100) {
            List<Point> cHull = grahamScan.convex(pointCloud);
            for (Point point : pointCloud) {
                for (int i = 0; i < cHull.size() - 1; i++) {
                    Point cPoint = cHull.get(i);
                    Point cPoint2 = cHull.get(i + 1);
                    if (cPoint == point || cPoint2 == point) continue;
                    if (Util.orientationTest(cPoint, cPoint2, point) > 0) {
                        System.out.println("Error on: " + point);
                    }
                }
            }
        }
    }

}