public class LPSolver {
    public LPResult solve(float cost, float[] constraints, float[] factors) {
        if (constraints.length != factors.length) {
            return new WrongArraySize();
        }

        return new Infeasible();
    }

    public interface LPResult {
    }

    public class Infeasible implements LPResult {
    }
    public class WrongArraySize implements LPResult {}
    public class Unbounded implements LPResult {
    }
    public class Good implements LPResult {
        public float[] result;
    }
}
