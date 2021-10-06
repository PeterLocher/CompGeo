import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class LPTest {

    @BeforeAll
    static void setUp() {
    }

    @Test
    void testWrongArraySize() {
        LPSolver solver = new LPSolver();
        float cost = 2;
        float[] contraints = new float[]{1.0f, 2.0f};
        float[] factors = new float[]{1.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.WrongArraySize;
    }

    @Test
    void testDegenerateCost() {
        LPSolver solver = new LPSolver();
        float cost = 0;
        float[] contraints = new float[]{1.0f, -2.0f};
        float[] factors = new float[]{2.0f, -2.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Degenerate;
    }

    @Test
    void testUnboundedLow() {
        LPSolver solver = new LPSolver();
        float cost = 2;
        float[] contraints = new float[]{1.0f};
        float[] factors = new float[]{2.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Unbounded;
    }

    @Test
    void testNotUnboundedLowDueToCost() {
        LPSolver solver = new LPSolver();
        float cost = -2;
        float[] contraints = new float[]{1.0f};
        float[] factors = new float[]{2.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert !(result instanceof LPSolver.Unbounded);
    }

    @Test
    void testUnboundedHigh() {
        LPSolver solver = new LPSolver();
        float cost = -2;
        float[] contraints = new float[]{1.0f};
        float[] factors = new float[]{-2.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Unbounded;
    }

    @Test
    void testInfeasibleConstraint() {
        LPSolver solver = new LPSolver();
        float cost = 2;
        float[] contraints = new float[]{-1.0f};
        float[] factors = new float[]{0.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Infeasible;
    }

    @Test
    void testInfeasibleProblem() {
        LPSolver solver = new LPSolver();
        float cost = 2;
        float[] contraints = new float[]{1.0f, -2.0f};
        float[] factors = new float[]{1.0f, -1.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Infeasible;
    }

    @Test
    void testSinglePositive2d() {
        LPSolver solver = new LPSolver();
        Point initPoint = new Point(6, -4);
        float c1 = 1.0f;
        float c2 = 2.3f;
        List<Constraint2D> constraints = new ArrayList<>();
        Constraint2D constraint = new Constraint2D();
        constraint.a = new Point(1.2f, -1.5f);
        constraint.b = 12f;
        constraints.add(constraint);
        Constraint2D constraint2 = new Constraint2D();
        constraint2.a = new Point(-1.2f, -1.5f);
        constraint2.b = -3f;
        constraints.add(constraint2);
        LPSolver.LPResult result = solver.solve2D(initPoint, c1, c2, constraints);
        System.out.println(result.getClass());
        LPSolver.Good good = (LPSolver.Good) result;
        for (float v : good.results) {
            System.out.print(v + ", ");
        }
        System.out.println();
    }

    @Test
    void testSinglePositive2d2() {
        LPSolver solver = new LPSolver();
        Point initPoint = new Point(6, -4);
        float c1 = 0.4f;
        float c2 = 0.5f;
        List<Constraint2D> constraints = new ArrayList<>();
        Constraint2D constraint = new Constraint2D();
        constraint.a = new Point(0.3f, 0.1f);
        constraint.b = 2.7f;
        constraints.add(constraint);
        Constraint2D constraint2 = new Constraint2D();
        constraint2.a = new Point(-0.6f, -0.4f);
        constraint2.b = -6f;
        constraints.add(constraint2);
        Constraint2D constraint3 = new Constraint2D();
        constraint3.a = new Point(0.5f, 0.5f);
        constraint3.b = 6f;
        constraints.add(constraint3);
        LPSolver.LPResult result = solver.solve2D(initPoint, c1, c2, constraints);
        System.out.println(result.getClass());
        LPSolver.Good good = (LPSolver.Good) result;
        for (float v : good.results) {
            System.out.print(v + ", ");
        }
        System.out.println();
    }

}