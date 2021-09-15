import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GrahamScan {

    public List<Point> convex(List<Point> in) {
        in.sort((p1, p2) -> Float.compare(p1.x, p2.x));
        ArrayList<Point> uh = new ArrayList<>();
        uh.add(in.get(0));
        uh.add(in.get(1));
        int s = 1;

        for (int i = 3; i<= in.size(); i++) {
            while ((s >= 1) && Util.orientationTest(uh.get(s-1), uh.get(s), in.get(i)) > 0) {
                uh.remove(s);
                s--;
            }
            uh.add(in.get(i));
            s++;
        }
        return uh;
    }

}
