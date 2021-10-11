import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GrahamScan implements CHAlgo {
    boolean experimentation = false;
    long sortTimeStart, sortTimeStop;
    long execTimeStart, execTimeStop;
    int removals;

    @Override
    public String toString() {
        return "GH";
    }

    public GrahamScanResult convex(List<Point> in) {

        execTimeStart = System.nanoTime();
        removals = 0;
        if (in.size() < 2) {
            return new GrahamScanResult(new ArrayList<>());
        }
        sortTimeStart = System.nanoTime();
        in.sort((p1, p2) -> Float.compare(p1.x, p2.x));
        sortTimeStop = System.nanoTime();
        ArrayList<Point> uh = new ArrayList<>();
        uh.add(in.get(0));
        uh.add(in.get(1));
        int s = 1;

        for (int i = 2; i < in.size(); i++) {
            while ((s >= 1) && Util.orientationTest(uh.get(s - 1), uh.get(s), in.get(i)) > 0) {
                uh.remove(s);
                s--;
                removals++;
            }
            uh.add(in.get(i));
            s++;
        }
        execTimeStop = System.nanoTime();
        GrahamScanResult res = new GrahamScanResult(uh);
        res.execTimeNano = execTimeStop - execTimeStart;
        res.sortTimeNano = sortTimeStop - sortTimeStart;
        res.orientationTestCall = Util.orientationTestCalls;
        res.removals = removals;
        Util.orientationTestCalls = 0;
        return res;
    }

    public void setExperimentation(boolean experimentation) {
        this.experimentation = experimentation;
    }

    public class GrahamScanResult implements AlgorithmResult {
        List<Point> result;
        long sortTimeNano;
        long execTimeNano;
        int orientationTestCall;
        int removals;

        public GrahamScanResult(List<Point> result) {
            this.result = result;
        }

        @Override
        public List<Point> returnResult() {
            return result;
        }

        @Override
        public Long returnExecTime() {
            return execTimeNano;
        }
    }
}



