import java.util.ArrayList;
import java.util.List;

public class GiftWrap implements CHAlgo {

    public AlgorithmResult convex(List<Point> in) {
        List<Point> out = new ArrayList<>();
        return new GiftWrapResult(out);
    }

    public class GiftWrapResult implements AlgorithmResult {
        List<Point> res;

        public GiftWrapResult(List<Point> res) {
            this.res = res;
        }

        @Override
        public List<Point> returnResult() {
            return null;
        }
    }
}
