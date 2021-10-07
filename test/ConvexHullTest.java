import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class ConvexHullTest {

    static float x = 0, y = 0;
    static int testCases, figureSize;
    private static Random random;

    public static Point nextPoint() {
        Point point = new Point(x, y);
        x = x + (float) Math.random() + 0.1f;
        y = (float) Math.pow(x, 2);
        return point;
    }

    public static void resetPointGen() {
        x = 0;
        y = 0;
    }

    static List<List<Point>> testCasesSquare = new ArrayList<>();

    static List<List<Point>> testCasesQuadratic = new ArrayList<>();

    static List<List<Point>> testCasesCircle = new ArrayList<>();

    static List<List<Point>> testCasesLog = new ArrayList<>();

    public static ArrayList<Point> generateLogPoints(int n) {
        ArrayList<Point> points = new ArrayList<>();
        float prevX = 1;
        Point p = new Point(1, (float) Math.log(1));
        points.add(p);
        for (int i = 1; i < n; i++) {
            float newX = prevX + (float) Math.random() + 0.1f;
            prevX = newX;
            points.add(new Point(newX, (float) Math.log(newX)));
        }
        return points;
    }

    public static ArrayList<Point> generatePointsInCircle(int n, int radius) {
        ArrayList<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double a = Math.random() * 2 * Math.PI;
            double r = radius * Math.sqrt(Math.random());
            Point p = new Point((float) (r * Math.cos(a)), (float) (r * Math.sin(a)));
            points.add(p);
        }
        return points;
    }

    @BeforeAll
    static void setUp() {
        random = new Random();
        random.setSeed(11);
        testCases = 1000;
        generateTestCases(10000);
    }

    private static void generateTestCases(int figSize) {
        figureSize = figSize;
        for (int i = 0; i < testCases; i++) {
            List<Point> pointCloud = new ArrayList<>();
            for (int j = 0; j < figureSize; j++) {
                pointCloud.add(new Point(random.nextFloat(), random.nextFloat()));
            }
            testCasesSquare.add(pointCloud);
        }
        for (int i = 0; i < testCases; i++) {
            List<Point> pointCloud = new ArrayList<>();
            for (int j = 0; j < figureSize; j++) {
                pointCloud.add(nextPoint());
            }
            testCasesQuadratic.add(pointCloud);
            resetPointGen();
        }
        for (int i = 0; i < testCases; i++) {
            List<Point> testCase = generatePointsInCircle(figureSize, 1);
            testCasesCircle.add(testCase);
            testCase = generateLogPoints(figureSize);
            testCasesLog.add(testCase);
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

    private void testConvexHullAlgo(CHAlgo algorithm, List<List<Point>> testCases) {
        System.out.println("Testing " + testCases.size() + " cases");
        for (int j = 0; j < testCases.size(); j++) {
            List<Point> pointCloud = testCases.get(j);
            List<Point> cHull = algorithm.convex(pointCloud).returnResult();
            int errors = 0;
            for (Point point : pointCloud) {
                for (int i = 0; i < cHull.size() - 1; i++) {
                    Point cPoint = cHull.get(i);
                    Point cPoint2 = cHull.get(i + 1);
                    if (cPoint == point || cPoint2 == point) continue;
                    if (Util.orientationTest(cPoint, cPoint2, point) > 0) {
                        errors++;
                        break;
                        //System.out.println("Error on: " + point);
                    }
                }
            }
            if (errors > 1) {
                System.out.println(errors + " errors on pointCloud #" + j);
            }
        }
    }

    @Test
    void testGrahamScan() {
        GrahamScan grahamScan = new GrahamScan();
        testConvexHullAlgo(grahamScan, testCasesSquare);
        testConvexHullAlgo(grahamScan, testCasesQuadratic);
        testConvexHullAlgo(grahamScan, testCasesCircle);
        testConvexHullAlgo(grahamScan, testCasesLog);
    }

    @Test
    void testGiftWrap() {
        GiftWrap giftWrap = new GiftWrap();
        testConvexHullAlgo(giftWrap, testCasesSquare);
        testConvexHullAlgo(giftWrap, testCasesQuadratic);
        testConvexHullAlgo(giftWrap, testCasesCircle);
        testConvexHullAlgo(giftWrap, testCasesLog);
    }

    @Test
    void testMarriage() {
        Marriage marriage = new Marriage();
        testConvexHullAlgo(marriage, testCasesSquare);
        testConvexHullAlgo(marriage, testCasesQuadratic);
        testConvexHullAlgo(marriage, testCasesCircle);
        testConvexHullAlgo(marriage, testCasesLog);
    }

    @Test
    void testSimpleGraham() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(2, 2.2f);
        Point point3 = new Point(0.5f, 0.5f);
        Point point4 = new Point(0.2f, 1.3f);
        Point point5 = new Point(4.9f, 2);
        Point point6 = new Point(0, 5.4444f);

        List<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        GrahamScan grahamScan = new GrahamScan();
        testConvexHullAlgo(grahamScan, Arrays.asList(points));
    }

    void saveRunTimeGHPerFigure(List<TestClassName> names, ArrayList<ArrayList<Long>> times, Integer figSize) {
        //Assume names of format name-name-name-name-name...
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("GHPerFigure" + "_" + testCases + "_" + figSize + ".txt"));
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                out.append(names.get(i)).append(": ");
                for (int j = 0; j < times.get(i).size(); j++) {
                    out.append(times.get(i).get(j)).append(", ");
                }
                out.append("\n");
            }
            String s = String.valueOf(out);
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void runSaveExperimentsGH() {
        resetTestCaseArrays();
        ArrayList<Integer> figSizes = new ArrayList<>(Arrays.asList(
                2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576
        ));
        testCases = 500;
        for (Integer figN : figSizes) {
            System.out.println(figN);
            //generateTestCases(figN);
            ArrayList<TestClassName> testClasses = new ArrayList<>();
            testClasses.add(TestClassName.SQUARE);
            testClasses.add(TestClassName.CIRCLE);
            testClasses.add(TestClassName.LOG);
            testClasses.add(TestClassName.QUADRATIC);
            ArrayList<ArrayList<Long>> avgExecTimes = new ArrayList<>(4);
            for (TestClassName tName : testClasses) {
                GrahamScan gh = new GrahamScan();
                long execSum = 0;
                long sortSum = 0;
                int orientationCalls = 0;
                int uhSizeSum = 0;
                long removalSum = 0;
                for (int j = 0; j < testCases; j++) {
                    GrahamScan.GrahamScanResult res;
                    switch (tName) {
                        case LOG -> res = gh.convex(generateLogPoints(figN));
                        case CIRCLE -> res = gh.convex(generatePointsInCircle(figN, 1));
                        case SQUARE -> {
                            List<Point> pointCloud = new ArrayList<>(figN);
                            for (int k = 0; k < figN; k++) {
                                pointCloud.add(new Point(random.nextFloat(), random.nextFloat()));
                            }
                            res = gh.convex(pointCloud);
                        }
                        case QUADRATIC -> {
                            List<Point> pointCloud = new ArrayList<>(figN);
                            for (int k = 0; k < figN; k++) {
                                pointCloud.add(nextPoint());
                            }
                            res = gh.convex(pointCloud);
                            resetPointGen();
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + tName);
                    }
                    /*switch (tName) {
                        case LOG -> res = gh.convex(testCasesLog.get(j));
                        case CIRCLE -> res = gh.convex(testCasesCircle.get(j));
                        case SQUARE -> res = gh.convex(testCasesSquare.get(j));
                        case QUADRATIC -> res = gh.convex(testCasesQuadratic.get(j));
                        default -> throw new IllegalStateException("Unexpected value: " + tName);
                    }*/
                    execSum += res.execTimeNano;
                    sortSum += res.sortTimeNano;
                    orientationCalls += res.orientationTestCall;
                    uhSizeSum += res.result.size();
                    removalSum += res.removals;
                }
                ArrayList<Long> experimentResults = new ArrayList<>();
                long execAvg = execSum / testCases;
                long sortAvg = sortSum / testCases;
                int orientationAvg = orientationCalls / testCases;
                int upperHullSize = uhSizeSum / testCases;
                long removalAverage = removalSum / testCases;
                experimentResults.add(execAvg);
                experimentResults.add(sortAvg);
                experimentResults.add((long) orientationAvg);
                experimentResults.add(removalAverage);
                experimentResults.add((long) upperHullSize);
                avgExecTimes.add(experimentResults);
                switch (tName) {
                    case LOG -> testCasesLog = new ArrayList<>();
                    case CIRCLE -> testCasesCircle = new ArrayList<>();
                    case SQUARE -> testCasesSquare = new ArrayList<>();
                    case QUADRATIC -> testCasesQuadratic = new ArrayList<>();
                    default -> throw new IllegalStateException("Unexpected value: " + tName);
                }
            }
            saveRunTimeGHPerFigure(testClasses, avgExecTimes, figN);
        }

    }

    private void resetTestCaseArrays() {
        testCasesSquare = new ArrayList<>();
        testCasesQuadratic = new ArrayList<>();
        testCasesCircle = new ArrayList<>();
        testCasesLog = new ArrayList<>();
    }
}