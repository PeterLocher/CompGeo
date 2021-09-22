public class Constraint2D {
    public Point a;
    public Float b;

    public boolean violates(Point p) {
        return a.x * p.x + a.y * p.y > b;
    }
}
