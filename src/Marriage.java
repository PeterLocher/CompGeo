import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Marriage implements CHAlgo {

    public MarriageResult convex(List<Point> in) {
        List<Point> out = new ArrayList<>();
        if (in.size() < 2) return new MarriageResult(out);
        float splitX = in.get(0).x - in.get(1).x;
        List<Constraint2D> constraints = new ArrayList<>();
        for (Point point : in) {
            Constraint2D constraint = new Constraint2D();
            constraint.a = new Point(-point.x, -1);
            constraint.b = -point.y;
            constraints.add(constraint);
        }
        // TODO: Indbyg at linjen skal krydse splitX mellem dens to punkter
        LPSolver.LPResult res = new LPSolver().solve2D(in.get(0), splitX, 1, constraints);
        if (!(res instanceof LPSolver.Good)) return new MarriageResult(out);
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
        for (Point point : in) {
            if (point.x < rightBridgePoint.x && point.x > leftBridgePoint.x) continue;
            if (point.x <= splitX) leftPoints.add(point);
            else rightPoints.add(point);
        }
        MarriageResult recResLeft = convex(leftPoints);
        MarriageResult recResRight = convex(rightPoints);
        recResLeft.res.addAll(recResRight.res);
        recResLeft.res.sort((o1, o2) -> (int) Math.signum(o1.x - o2.x));
        //TODO: Maybe some other way than sorting?
        return recResLeft;
    }

    public class MarriageResult implements AlgorithmResult {

        public MarriageResult(List<Point> res) {
            this.res = res;
        }

        List<Point> res;

        @Override
        public List<Point> returnResult() {
            return null;
        }
    }
}
