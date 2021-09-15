import java.util.ArrayList;
import java.util.List;

public class FigureGenerator {
    List<Point> generateTriangle() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0.5f, 1);
        Point p3 = new Point(1, 0);
        List<Point> points = new ArrayList<>(3);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        return points;
    }
}
