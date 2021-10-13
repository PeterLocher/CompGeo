import java.util.*;

public class LPSolver {
    public long twoDCalls, oneDCalls;

    public LPResult solve2D(Point v, float c1, float c2, List<Constraint2D> constraints) {
        Util.twoDCalls++;
        for (int i = 0, constraintsSize = constraints.size(); i < constraintsSize; i++) {
            Constraint2D constraint = constraints.get(i);
            constraint.index = i;
        }
        Collections.shuffle(constraints, new Random(0));
        int firstTightConstraint = constraints.get(0).index, secondTightConstraint = constraints.get(1).index;
        v = constraints.get(0).intersection(constraints.get(1));
        boolean lastViolatingConstraintIsUnbounded = false;
        for (int i = 1; i < constraints.size(); i++) {
            Constraint2D constraint = constraints.get(i);
            if (constraint.violates(v)) {
                lastViolatingConstraintIsUnbounded = false;
                float[] a = new float[i];
                float[] b = new float[i];
                float x1Term = constraint.a.x / constraint.a.y;
                float constantTerm = constraint.b / constraint.a.y;
                // Translate to 1D constraints
                for (int j = 0; j < i; j++) {
                    Constraint2D preConstraint = constraints.get(j);
                    a[j] = preConstraint.a.x - preConstraint.a.y * x1Term;
                    b[j] = preConstraint.b - preConstraint.a.y * constantTerm;
                }
                float oneDCost = c1 - c2 * x1Term;
                LPResult result = solve(oneDCost, b, a);
                // Interpret result of 1D problem
                if (!(result instanceof Good)) {
                    if (result instanceof Unbounded) {
                        lastViolatingConstraintIsUnbounded = true;
                        continue;
                    }
                    else return result;
                }
                Good good = ((Good) result);
                float newX1 = good.results[0];
                float newX2 = constantTerm - x1Term * newX1;
                v.x = newX1;
                v.y = newX2;
                firstTightConstraint = constraints.get(good.tightConstraints.get(0)).index;
                secondTightConstraint = constraints.get(i).index;
            }
        }
        if (lastViolatingConstraintIsUnbounded)
            return new Unbounded();
        return new Good(v.x, v.y, Arrays.asList(firstTightConstraint, secondTightConstraint));
    }

    public LPResult solve(float c1, float[] constraints, float[] factors) {
        Util.oneDCalls++;
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
                float newConstraint = constraints[i] / factors[i];
                if (newConstraint < feasible_region_high) {
                    feasible_region_high = newConstraint;
                    feasible_region_high_index = i;
                }
            } else {
                float newConstraint = constraints[i] / factors[i];
                if (newConstraint > feasible_region_low) {
                    feasible_region_low = newConstraint;
                    feasible_region_low_index = i;
                }
            }
        }

        if ((feasible_region_high == Float.MAX_VALUE) && (c1 < 0)) {
            return new Unbounded();
        } else if ((feasible_region_low == -Float.MAX_VALUE) && (c1 > 0)) {
            return new Unbounded();
        }

        if (feasible_region_high < feasible_region_low) {
            return new Infeasible();
        }

        if (c1 == 0) {
            return new Degenerate();
        } else if (c1 > 0) {
            return new Good(feasible_region_low, feasible_region_low_index);
        } else {
            return new Good(feasible_region_high, feasible_region_high_index);
        }
    }

    public interface LPResult {
    }

    public class Infeasible implements LPResult {
    }

    public class WrongArraySize implements LPResult {
    }

    public class Degenerate implements LPResult {
    }

    public class Unbounded implements LPResult {
    }

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
            results = new float[]{result1, result2};
        }

        public Good(float result1, float result2, List<Integer> tightConstraints) {
            results = new float[]{result1, result2};
            this.tightConstraints = tightConstraints;
        }
    }
}
