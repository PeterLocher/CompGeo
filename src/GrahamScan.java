import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GrahamScan {

    public List<Point> convex(List<Point> in) {
        in.sort((p1, p2) -> Float.compare(p1.x, p2.x));
        System.out.println(in);
        List<Point> out = new ArrayList<>();
        return out;
    }

}
