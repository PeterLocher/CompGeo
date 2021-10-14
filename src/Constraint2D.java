public class Constraint2D {
    public Point a;
    public Float b;
    public int index;

    public boolean violates(Point p) {
        return a.x * p.x + a.y * p.y > b;
    }

    public Point intersection(Constraint2D con) {
        float aydiv = a.y / con.a.y;
        float x1 = (b - aydiv *con.b)/(a.x - aydiv *con.a.x);
        float x2 = (b - a.x * x1)/a.y;
        return new Point(x1, x2);
    }
}
