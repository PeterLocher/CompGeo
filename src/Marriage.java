import java.util.*;

public class Marriage implements CHAlgo {

    public MarriageResult convex(List<Point> in) {
        List<Point> resList = new ArrayList<>(convexSet(in));
        resList.sort((o1, o2) -> (int) Math.signum(o1.x - o2.x));
        return new MarriageResult(resList);
    }

    public Set<Point> convexSet(List<Point> in) {
        Set<Point> out = new HashSet<>();
        if (in.size() < 2) return out;
        // Create line for bridging across
        float splitX = (in.get(0).x + in.get(1).x)/2;
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
        out.addAll(convexSet(leftPoints));
        out.addAll(convexSet(rightPoints));
        return out;
    }

    public class MarriageResult implements AlgorithmResult {

        public MarriageResult(List<Point> res) {
            this.res = res;
        }

        List<Point> res;

        @Override
        public List<Point> returnResult() {
            return res;
        }
    }
}
