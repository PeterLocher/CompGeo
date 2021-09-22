public class LPSolver {
    public LPResult solve(float cost, float[] constraints, float[] factors) {
        if (constraints.length != factors.length) {
            return new WrongArraySize();
        }
        float feasible_region_low = -Float.MAX_VALUE;
        float feasible_region_high = Float.MAX_VALUE;
        for (int i = 0; i   <constraints.length; i++) {

            if (factors[i] == 0) {
                if (constraints[i] < 0) {
                    return new Infeasible();
                }
            } else if (factors[i] > 0) {
                feasible_region_high = Float.min(feasible_region_high, constraints[i]/factors[i]);
            } else {
                feasible_region_low = Float.max(feasible_region_low, constraints[i]/factors[i]);
            }
        }

        if ((feasible_region_high == Float.MAX_VALUE) && (cost < 0)) {
            return new Unbounded();
        } else if ((feasible_region_low == -Float.MAX_VALUE) && (cost > 0)) {
            return new Unbounded();
        }

        if (cost == 0) {
            return new Degenerate();
        } else if(cost > 0) {
            return new Good(feasible_region_low);
        } else {
            return new Good(feasible_region_high);
        }
    }

    public interface LPResult {
    }

    public class Infeasible implements LPResult {}
    public class WrongArraySize implements LPResult {}
    public class Degenerate implements LPResult {}
    public class Unbounded implements LPResult {}
    public class Good implements LPResult {
        public float[] results;
        public Good(float result) {
             results = new float[1];
             results[0] = result;
        }
    }
}
