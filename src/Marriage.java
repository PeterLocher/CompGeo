import java.util.ArrayList;
import java.util.List;

public class Marriage implements CHAlgo {

    public MarriageResult convex(List<Point> in) {
        List<Point> out = new ArrayList<>();
        if (in.size() < 2) return new MarriageResult(out);
        float splitX = in.get(0).x - in.get(1).x;
        List<Point> leftPoints = new ArrayList<>();
        List<Point> rightPoints = new ArrayList<>();
        for (Point point : in) {
            if (point.x <= splitX) leftPoints.add(point);
            else rightPoints.add(point);
        }
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
        float lineA = ((LPSolver.Good) res).results[0];
        float lineB = ((LPSolver.Good) res).results[1];
        return new MarriageResult(out);
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
