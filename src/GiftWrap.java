import java.util.ArrayList;
import java.util.List;

public class GiftWrap implements CHAlgo {
    @Override
    public String toString() {
        return "GW";
    }

    long execTimeStart, execTimeStop;

    public GiftWrapResult convex(List<Point> in) {
        execTimeStart = System.nanoTime();
        List<Point> hull = new ArrayList<>();
        Point leftMostPoint = new Point(Float.MAX_VALUE, Float.MAX_VALUE);
        Point rightMostPoint = new Point(Float.MIN_VALUE, Float.MIN_VALUE);
        for (Point point : in) {
            if (leftMostPoint.x > point.x) {
                leftMostPoint = point;
            }
            if (rightMostPoint.x < point.x) {
                rightMostPoint = point;
            }
        }
        Point curPoint = leftMostPoint;
        hull.add(curPoint);
        while (curPoint != rightMostPoint) {
            Point bestPointSoFar = curPoint == leftMostPoint ? rightMostPoint : leftMostPoint;
            for (Point point : in) {
                if (point == curPoint) continue;
                if (Util.orientationTest(curPoint, bestPointSoFar, point) == 1) {
                    bestPointSoFar = point;
                }
            }
            curPoint = bestPointSoFar;
            hull.add(bestPointSoFar);
        }
        execTimeStop = System.nanoTime();
        GiftWrapResult res = new GiftWrapResult(hull);
        res.totalRunTime = execTimeStop - execTimeStart;
        res.orientationCalls = Util.orientationTestCalls;
        Util.orientationTestCalls = 0;
        return res;
    }

    public class GiftWrapResult implements AlgorithmResult {
        List<Point> result;
        long totalRunTime;
        //Orientation test calls
        long orientationCalls;

        public GiftWrapResult(List<Point> hull) {
            this.result = hull;
        }

        @Override
        public List<Point> returnResult() {
            return result;
        }

        @Override
        public Long returnExecTime() {
            return totalRunTime;
        }
    }
}
