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
        List<Point> resList = new ArrayList<>(convexSet(in, 0L));
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

    public Set<Point> convexSet(List<Point> in, long depth) {
        if (depth > recursionDepth) recursionDepth = depth;
        Set<Point> out = new HashSet<>();
        if (in.size() < 2) return out;
        // Create line for bridging across
        float splitX = splitXAverage(in);
        // Convert points to constraints
        List<Constraint2D> constraints = new ArrayList<>();
        for (Point point : in) {
            Constraint2D constraint = new Constraint2D();
            constraint.a = new Point(-point.x, -1);
            constraint.b = -point.y;
            constraints.add(constraint);
        }
        // Find bridge
        LPSolver.LPResult res = new LPSolver().solve2D(in.get(0), splitX, 1, constraints);
        // Extract result for bridge
        if (!(res instanceof LPSolver.Good)) return out;
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
        //System.out.println("Split around " + splitX + ": " + in.get(0).x  + ", " + in.get(1).x);
        for (Point point : in) {
            //System.out.println(point.x);
            if (point.x <= rightBridgePoint.x && point.x >= leftBridgePoint.x) continue;
            if (point.x <= splitX) leftPoints.add(point);
            else rightPoints.add(point);
        }
        //System.out.println(in.size() + " split into " + leftPoints.size() + ", " + rightPoints.size());
        // Accumulate solution from recursive calls
        out.add(leftBridgePoint);
        out.add(rightBridgePoint);
        out.addAll(convexSet(leftPoints, depth + 1));
        out.addAll(convexSet(rightPoints, depth + 1));
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
