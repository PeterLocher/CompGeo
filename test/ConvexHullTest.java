import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
            Point p = new Point((float) (r * Math.cos(a)) + 1f, (float) (r * Math.sin(a)) + 1f);
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
    void testGrahamComparedToGW() {
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
        for (int i = 0; i < 1000; i++) {
            points = generatePointsInCircle(1000, 1);
            GrahamScan grahamScan = new GrahamScan();
            List<Point> ghRes = grahamScan.convex(points).result;

            GiftWrap gw = new GiftWrap();
            List<Point> gwRes = gw.convex(points).result;

            if (gwRes.size() != ghRes.size()){
                System.out.println("wtf");
            }
        }


    }

    @Test
    void testSimple() {
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
        testConvexHullAlgo(new Marriage(), Arrays.asList(points));
    }




    // Experiments

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

    void saveRunTimeGWPerFigure(List<TestClassName> names, ArrayList<ArrayList<Long>> times, Integer figSize) {
        //Assume names of format name-name-name-name-name...
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("GWPerFigure" + "_" + testCases + "_" + figSize + ".txt"));
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
    void compareUpperHulls() {
        ArrayList<Point> testCase = generateLogPoints(1000);
        GrahamScan gh = new GrahamScan();
        Marriage mbq = new Marriage();
        GrahamScan.GrahamScanResult ghRes = gh.convex(testCase);
        Marriage.MarriageResult mbqRes = mbq.convex(testCase);
        System.out.println(mbqRes);
    }

    @Test
    void mbqTestCase() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(2, 2.2f);
        Point point3 = new Point(0.5f, 0.5f);
        Point point4 = new Point(0.2f, 1.3f);
        Point point5 = new Point(4.9f, 2);
        Point point6 = new Point(0, 5.4444f);
        Point point7 = new Point(4.356729f, 3.4358428f);
        Point point8 = new Point(4.356729f, 3.9358428f);

        List<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);
        points.add(point8);

        GrahamScan scan = new GrahamScan();
        GrahamScan.GrahamScanResult b = scan.convex(points);
        Marriage mbq = new Marriage();
        Marriage.MarriageResult a = mbq.convex(points);
        int abe = 0;
    }

    @Test
    void allAlgorithmsExperimentSave() {
        resetTestCaseArrays();
        ArrayList<Integer> figSizes = new ArrayList<>(Arrays.asList(
                65536, 131072, 262144, 524288, 1048576
        ));
        testCases = 500;

        for (Integer figN : figSizes) {
            System.out.println(figN);
            ArrayList<TestClassName> testClasses = new ArrayList<>();
            testClasses.add(TestClassName.SQUARE);
            testClasses.add(TestClassName.CIRCLE);
            testClasses.add(TestClassName.LOG);
            testClasses.add(TestClassName.QUADRATIC);
            //ArrayList<ArrayList<Long>> avgExecTimes = new ArrayList<>(4);

            Map<CHAlgo, ArrayList<ArrayList<Long>>> algoIntoClassResultsMap = new HashMap<>();
            GrahamScan gh = new GrahamScan();
            Marriage mbq = new Marriage();
            GiftWrap gw = new GiftWrap();
            algoIntoClassResultsMap.put(gh, new ArrayList<>());
            algoIntoClassResultsMap.put(mbq, new ArrayList<>());
            algoIntoClassResultsMap.put(gw, new ArrayList<>());

            for (TestClassName tName : testClasses) {
                //Can be moved out if results are cleared after each call in the algorithm classes

                ArrayList<CHAlgo> algos = new ArrayList<>();
                algos.add(gh);
                algos.add(mbq);
                algos.add(gw);

                Map<CHAlgo, Long> execTimeMap = new HashMap<>();
                execTimeMap.put(gh, 0L);
                execTimeMap.put(mbq, 0L);
                execTimeMap.put(gw, 0L);

                long execSum = 0;
                long sortSum = 0;
                int orientationCalls = 0;
                int uhSizeSum = 0;
                long removalSum = 0;
                for (int j = 0; j < testCases; j++) {
                    ArrayList<Point> input = new ArrayList<>(figN);
                    switch (tName) {
                        case LOG -> input = generateLogPoints(figN);
                        case CIRCLE -> input = generatePointsInCircle(figN, 1);
                        case SQUARE -> {
                            ArrayList<Point> pointCloud = new ArrayList<>(figN);
                            for (int k = 0; k < figN; k++) {
                                pointCloud.add(new Point(random.nextFloat(), random.nextFloat()));
                            }
                            input = pointCloud;
                        }
                        case QUADRATIC -> {
                            ArrayList<Point> pointCloud = new ArrayList<>(figN);
                            for (int k = 0; k < figN; k++) {
                                pointCloud.add(nextPoint());
                            }
                            input = pointCloud;
                            resetPointGen();
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + tName);
                    }
                    for (CHAlgo alg : algos) {
                        AlgorithmResult res = alg.convex(input);
                        execTimeMap.computeIfPresent(alg, (key, val) -> val + res.returnExecTime());
                    }
                }
                for (int i = 0; i < algos.size(); i++) {
                    ArrayList<Long> metricResults = new ArrayList<>();
                    Long totalExecTime = execTimeMap.get(algos.get(i));
                    if (totalExecTime != null) {
                        metricResults.add(totalExecTime / testCases);
                    }
                    algoIntoClassResultsMap.get(algos.get(i)).add(metricResults);
                }
            }
            saveComparisonData(testClasses, algoIntoClassResultsMap, figN);
        }
    }

    private void saveComparisonData(ArrayList<TestClassName> testClasses, Map<CHAlgo, ArrayList<ArrayList<Long>>> avgExecTimesMap, Integer figN) {
        //Assume names of format name-name-name-name-name...
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("comparison" + "_" + testCases + "_" + figN + ".txt"));
            StringBuilder out = new StringBuilder();
            for (CHAlgo algorithm : avgExecTimesMap.keySet()) {
                out.append(algorithm.toString()).append("\n");
                ArrayList<ArrayList<Long>> avgExecTimes = avgExecTimesMap.get(algorithm);
                for (int i = 0; i < testClasses.size(); i++) {
                    out.append(testClasses.get(i)).append(": ");
                    for (int j = 0; j < avgExecTimes.get(i).size(); j++) {
                        out.append(avgExecTimes.get(i).get(j)).append(", ");
                    }
                    out.append("\n");
                }
            }
            writer.write(String.valueOf(out));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void runSaveExperimentsGH() {
        resetTestCaseArrays();
        ArrayList<Integer> figSizes = new ArrayList<>(Arrays.asList(
               /* 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144,*/ 524288, 1048576
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

    @Test
    void runSaveExperimentsGW() {
        resetTestCaseArrays();
        ArrayList<Integer> figSizes = new ArrayList<>(Arrays.asList(
                2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144/*, 524288, 1048576*/
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
                GiftWrap gh = new GiftWrap();
                long execSum = 0;
                int orientationCalls = 0;
                long uhSize = 0;
                for (int j = 0; j < testCases; j++) {
                    GiftWrap.GiftWrapResult res;
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
                    execSum += res.totalRunTime;
                    orientationCalls += res.orientationCalls;
                    uhSize += res.result.size();
                }
                ArrayList<Long> experimentResults = new ArrayList<>();
                long execAvg = execSum / testCases;
                int orientationAvg = orientationCalls / testCases;
                experimentResults.add(execAvg);
                experimentResults.add((long) orientationAvg);
                experimentResults.add(uhSize / testCases);
                avgExecTimes.add(experimentResults);
                switch (tName) {
                    case LOG -> testCasesLog = new ArrayList<>();
                    case CIRCLE -> testCasesCircle = new ArrayList<>();
                    case SQUARE -> testCasesSquare = new ArrayList<>();
                    case QUADRATIC -> testCasesQuadratic = new ArrayList<>();
                    default -> throw new IllegalStateException("Unexpected value: " + tName);
                }
            }
            saveRunTimeGWPerFigure(testClasses, avgExecTimes, figN);
        }
    }

    @Test
    void runSaveExperimentsMBQ() {
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
                Marriage gh = new Marriage();
                long execSum = 0;
                long hullSize = 0;
                long recDepth = 0;
                long oneD = 0;
                long twoD = 0;
                for (int j = 0; j < testCases; j++) {
                    Marriage.MarriageResult res;
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
                    execSum += res.totalRunTime;
                    recDepth += res.recDepth;
                    oneD += res.oneDCalls;
                    twoD += res.twoDCalls;
                    hullSize += res.res.size();
                }
                ArrayList<Long> experimentResults = new ArrayList<>();
                long execAvg = execSum / testCases;
                long recAvg = recDepth / testCases;
                long oneAvg = oneD / testCases;
                long twoAvg = twoD / testCases;
                long hullSizeAvg = hullSize / testCases;
                experimentResults.add(execAvg);
                experimentResults.add(recAvg);
                experimentResults.add(oneAvg);
                experimentResults.add(twoAvg);
                experimentResults.add(hullSizeAvg);
                avgExecTimes.add(experimentResults);
                switch (tName) {
                    case LOG -> testCasesLog = new ArrayList<>();
                    case CIRCLE -> testCasesCircle = new ArrayList<>();
                    case SQUARE -> testCasesSquare = new ArrayList<>();
                    case QUADRATIC -> testCasesQuadratic = new ArrayList<>();
                    default -> throw new IllegalStateException("Unexpected value: " + tName);
                }
            }
            saveRunTimeMBQPerFigure(testClasses, avgExecTimes, figN);
        }
    }

    void saveRunTimeMBQPerFigure(List<TestClassName> names, ArrayList<ArrayList<Long>> times, Integer figSize) {
        //Assume names of format name-name-name-name-name...
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("MBQPerFigure" + "_" + testCases + "_" + figSize + ".txt"));
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

    private void resetTestCaseArrays() {
        testCasesSquare = new ArrayList<>();
        testCasesQuadratic = new ArrayList<>();
        testCasesCircle = new ArrayList<>();
        testCasesLog = new ArrayList<>();
    }
}