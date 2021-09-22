import java.util.ArrayList;
import java.util.List;

public class Marriage implements CHAlgo {

    public MarriageResult convex(List<Point> in) {
        List<Point> out = new ArrayList<>();
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
