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
    void testUnboundedHigh() {
        LPSolver solver = new LPSolver();
        float cost = -2;
        float[] contraints = new float[]{1.0f};
        float[] factors = new float[]{2.0f};
        LPSolver.LPResult result = solver.solve(cost, contraints, factors);
        assert result instanceof LPSolver.Unbounded;
    }

}