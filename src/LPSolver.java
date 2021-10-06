import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LPSolver {

    public LPResult solve2D(Point v, float c1, float c2, List<Constraint2D> constraints) {
        Collections.shuffle(constraints);
        for (int i = 1; i < constraints.size(); i++) {
            Constraint2D constraint = constraints.get(i);
            if (constraint.violates(v)) {
                float[] a = new float[i];
                float[] b = new float[i];
                float x1Term = constraint.a.x / constraint.a.y;
                float constantTerm = constraint.b / constraint.a.y;

                for (int j = 0; j < i; j++) {
                    Constraint2D preConstraint = constraints.get(j);
                    a[j] = preConstraint.a.x - preConstraint.a.y * x1Term;
                    b[j] = preConstraint.b - preConstraint.a.y * constantTerm;
                }
                float oneDCost = c1 - c2 * x1Term;
                LPResult result = solve(oneDCost, b, a);
                if (!(result instanceof Good)) return result;
                float newX1 = ((Good) result).results[0];
                float newX2 = constantTerm - x1Term * newX1;
                v.x = newX1;
                v.y = newX2;
            }
        }
        return new Good(v.x, v.y);
    }

    public LPResult solve(float cost, float[] constraints, float[] factors) {
        if (constraints.length != factors.length) {
            return new WrongArraySize();
        }
        float feasible_region_low = -Float.MAX_VALUE;
        float feasible_region_high = Float.MAX_VALUE;
        int feasible_region_low_index = 0;
        int feasible_region_high_index = 0;
        for (int i = 0; i < constraints.length; i++) {

            if (factors[i] == 0) {
                if (constraints[i] < 0) {
                    return new Infeasible();
                }
            } else if (factors[i] > 0) {
                feasible_region_high = Float.min(feasible_region_high, constraints[i]/factors[i]);
                feasible_region_high_index = i;
            } else {
                feasible_region_low = Float.max(feasible_region_low, constraints[i]/factors[i]);
                feasible_region_low_index = i;
            }
        }

        if ((feasible_region_high == Float.MAX_VALUE) && (cost < 0)) {
            return new Unbounded();
        } else if ((feasible_region_low == -Float.MAX_VALUE) && (cost > 0)) {
            return new Unbounded();
        }

        if (feasible_region_high < feasible_region_low) {
            return new Infeasible();
        }

        if (cost == 0) {
            return new Degenerate();
        } else if(cost > 0) {
            return new Good(feasible_region_low, feasible_region_low_index);
        } else {
            return new Good(feasible_region_high, feasible_region_high_index);
        }
    }

    public interface LPResult {}
    public class Infeasible implements LPResult {}
    public class WrongArraySize implements LPResult {}
    public class Degenerate implements LPResult {}
    public class Unbounded implements LPResult {}
    public class Good implements LPResult {
        public float[] results;
        public List<Integer> tightConstraints;
        public Good(float result, int tight_constraint_index) {
            results = new float[1];
            results[0] = result;
            tightConstraints = new ArrayList<>();
            tightConstraints.add(tight_constraint_index);
        }
        public Good(float result1, float result2) {
            results = new float[] {result1, result2};
        }

        public Good(float result1, float result2, List<Integer> tightConstraints) {
            results = new float[] {result1, result2};
            this.tightConstraints = tightConstraints;
        }
    }
}
