public class OneDLPSolver {
    public OneDLPResult solve(float x_m, float[] y_s, float[] x_s) {
        if (y_s.length != x_s.length) {
            return new WrongArraySize();
        }
        return new Infeasible();
    }

    public interface OneDLPResult {
    }

    public class Infeasible implements OneDLPResult {
    }
    public class WrongArraySize implements OneDLPResult {}
    public class Unbounded implements OneDLPResult {
    }
    public class Good implements OneDLPResult {
        public int result;
    }
}
