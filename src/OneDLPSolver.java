import java.util.*;

public class OneDLPSolver {

    public OneDLPResult solve2D(Point v, float c1, float c2, List<Constraint2D> constraints) {
        Collections.shuffle(constraints);
        for (int i = 0; i < constraints.size(); i++) {
            Constraint2D constraint = constraints.get(i);
            if (constraint.violates(v)) {
                float[] b = new float[i];
                float[] a = new float[i];
                float x1Term = constraint.a.x / constraint.a.y;
                float constantTerm = constraint.b / constraint.a.y;

                for (int j = 0; j < i; j++) {
                    Constraint2D preConstraint = constraints.get(j);
                    a[j] = preConstraint.a.x - preConstraint.a.y * x1Term;
                    b[j] = preConstraint.b - constantTerm;
                }
                float oneDCost = c1 - c2 * x1Term;
                OneDLPResult result = solve(oneDCost, b, a);
                if (!(result instanceof Good)) return result;
                float newX1 = ((Good) result).result;
                float newX2 = constantTerm - x1Term * newX1;
                v.x = newX1;
                v.y = newX2;
            }
        }
        return new Good();
    }

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
