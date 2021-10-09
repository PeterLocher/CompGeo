import java.util.ArrayList;
import java.util.List;

public class GiftWrap implements CHAlgo {
    @Override
    public String toString() {
        return "GW";
    }

    public AlgorithmResult convex(List<Point> in) {
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
        return new GiftWrapResult(hull);
    }

    public class GiftWrapResult implements AlgorithmResult {
        List<Point> result;

        //Orientation test calls

        public GiftWrapResult(List<Point> hull) {
            this.result = hull;
        }

        @Override
        public List<Point> returnResult() {
            return result;
        }

        @Override
        public Long returnExecTime() {
            return 0L;
        }
    }
}
