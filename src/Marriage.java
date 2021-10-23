import java.util.*;

public class Marriage implements CHAlgo {
    private long execTimeStop;
    private long execTimeStart;
    private long recursionDepth;

    @Override
    public String toString() {
        return "MBQ";
    }

    public MarriageResult convex(List<Point> in) {
        recursionDepth = 0;
        execTimeStart = System.nanoTime();
        List<Point> resList = new ArrayList<>(convexSet(in, 0L, 0, null, null));
        resList.sort((o1, o2) -> (int) Math.signum(o1.x - o2.x));
        execTimeStop = System.nanoTime();
        MarriageResult result = new MarriageResult(resList);
        result.totalRunTime = execTimeStop - execTimeStart;
        result.recDepth = recursionDepth;
        result.oneDCalls = Util.oneDCalls;
        result.twoDCalls = Util.twoDCalls;
        Util.oneDCalls = 0;
        Util.twoDCalls = 0;
        return result;
    }

    Random random = new Random();

    public Set<Point> convexSet(List<Point> in, long depth, int recursionDirection, Point prevLeftBridgeP, Point prevRightBrightP) {
        //Recursiondirection: 0 initial, 1 right, -1 left
        if (depth > recursionDepth) recursionDepth = depth;
        Set<Point> out = new HashSet<>();
        if (in.size() == 0) return out;
        if (in.size() < 2) {
            Point p = in.get(0);
            if (recursionDirection == 0) {
                //Convex hull on one point is just one point?
                return new HashSet<>(in);
            } else {
                if (Util.orientationTest(prevLeftBridgeP, prevRightBrightP, p) <= 0) {
                    return new HashSet<>(in);
                } else {
                    return out;
                }
            }
        }
        // Create line for bridging across
        double[] arr = in.stream().mapToDouble(p -> (double) p.x).toArray();
        float[] floatArr = new float[in.size()];
        for (int i = 0; i < in.size(); i++) {
            floatArr[i] = (float) arr[i];
        }
        //float split = QuickSelect.selectRecursive(floatArr, Math.round(in.size() / 2));
        float splitX = splitXAverage(in);
        //float splitX = QuickSelect.selectRecursive(floatArr, Math.round(in.size() / 2));
        //System.out.println(splitX);
        // Convert points to constraints
        List<Constraint2D> constraints = new ArrayList<>();
        for (Point point : in) {
            Constraint2D constraint = new Constraint2D();
            constraint.a = new Point(-point.x, -1);
            constraint.b = -point.y;
            constraints.add(constraint);
        }
        // Find two points on each side of the bridge
        int lowX = -1;
        int highX = -1;
        for (int i = 0; i < in.size(); i++) {
            Point point = in.get(i);
            if (point.x < splitX) lowX = i;
            else highX = i;
            if (lowX != -1 && highX != -1) break;
        }
        if (lowX == -1 || highX == -1) throw new Error();
        // Find bridge
        LPSolver.LPResult res = new LPSolver().solve2D(lowX, highX, splitX, 1, constraints);
        // Extract result for bridge
        if (!(res instanceof LPSolver.Good))
            return out;
        LPSolver.Good goodRes = (LPSolver.Good) res;
        Point p1 = in.get(goodRes.tightConstraints.get(0));
        Point p2 = in.get(goodRes.tightConstraints.get(1));
        Point rightBridgePoint, leftBridgePoint;
        if (p1.x < p2.x) {
            leftBridgePoint = p1;
            rightBridgePoint = p2;
        } else {
            leftBridgePoint = p2;
            rightBridgePoint = p1;
        }
        // Partition points based on the line
        List<Point> leftPoints = new ArrayList<>();
        List<Point> rightPoints = new ArrayList<>();
        /* for (Point point : in) {
            //System.out.println(point.x);
            if (point.x <= rightBridgePoint.x && point.x >= leftBridgePoint.x) continue;
            if (recursionDirection == 0) {
                if (point.x <= splitX) leftPoints.add(point);
                else rightPoints.add(point);
            } else if (recursionDirection == 1) {
                //Looking at points to the right of some previous recursion
                // prev rightbridgepoint - (some potential useless points) - leftbridgepoint
                if (point.x <= splitX) continue;
                else rightPoints.add(point);
            } else if (recursionDirection == -1) {
                //leftbridgepoint - (some points under bridge ) - splitx - (some points under bridge) -  rightbridgepoint - (some potential useless points) - previous leftbridgepoint
                if (point.x >= splitX) continue;
                else leftPoints.add(point);
            }
        }*/
        for (Point point : in) {
            //System.out.println(point.x);
            if (point.x <= rightBridgePoint.x && point.x >= leftBridgePoint.x) continue;
            if (point.x <= splitX) leftPoints.add(point);
            else rightPoints.add(point);
        }
        //System.out.println(in.size() + " split into " + leftPoints.size() + ", " + rightPoints.size());
        // Accumulate solution from recursive calls
        /*System.out.println("Leftbridge point: " + leftBridgePoint);
        System.out.println("Right bridge point: " + rightBridgePoint);*/
        if (leftPoints.size() > 1) {
            leftPoints.add(leftBridgePoint);
        }
        if (rightPoints.size() > 1) {
            rightPoints.add(rightBridgePoint);
        }
        out.add(leftBridgePoint);
        out.add(rightBridgePoint);
        out.addAll(convexSet(leftPoints, depth + 1, -1, leftBridgePoint, rightBridgePoint));
        out.addAll(convexSet(rightPoints, depth + 1, 1, leftBridgePoint, rightBridgePoint));
        return out;
    }

    private float splitXRandom(List<Point> in) {
        return (in.get(0).x + in.get(random.nextInt(in.size() - 1) + 1).x) / 2;
    }

    private float splitXAverage(List<Point> in) {
        float average = 0;
        for (Point point : in) {
            average += (point.x / in.size());
        }
        return average;
    }

    public class MarriageResult implements AlgorithmResult {
        //# internal 1d calls
        //# Recursion depth
        public MarriageResult(List<Point> res) {
            this.res = res;
        }

        List<Point> res;
        long totalRunTime;
        long recDepth, oneDCalls, twoDCalls;

        @Override
        public List<Point> returnResult() {
            return res;
        }

        @Override
        public Long returnExecTime() {
            return totalRunTime;
        }
    }


}
