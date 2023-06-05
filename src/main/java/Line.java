import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line> {
    private Point from;
    private Vector vector;

    public Point getFrom() {
        return from;
    }

    public Vector getVector() {
        return vector;
    }

    public List<Point> getPoints() {
        List<Point> result = new ArrayList<>();
        result.add(getFrom());
        result.add(getTo());
        return result;
    }

    public Line(Point from, Vector vector) {
        this.from = from;
        this.vector = vector;
    }

    public Line(Point from, Point to) {
        this(from, new Vector(from, to));
    }

    public Point intersect(Plane plane) {
        Point planePoint = plane.getRandomPoint();
        Vector fromAndPoint = new Vector(getFrom(), planePoint);
        Vector toAndPoint = new Vector(planePoint, getTo());
        Double fromOnPlane = fromAndPoint.scalar(plane.getNormal());
        Double toOnPlane = toAndPoint.scalar(plane.getNormal());
        if(Math.abs(fromOnPlane) < Point.getE())
            return getFrom();
        if(Math.abs(toOnPlane) < Point.getE())
            return getTo();
        if((fromOnPlane < -Point.getE()) ^ (toOnPlane < -Point.getE()))
            return null;
        Double coef = fromOnPlane / (fromOnPlane + toOnPlane);
        if(coef > 1 + Point.getE())
            return null;
        Vector sizeVector = getVector().mul(coef);
        return new Line(this.getFrom(), sizeVector).getTo();
    }

    public boolean isOnPlane(Plane plane) {
        return getFrom().equals(plane.getPlanedPoint(getFrom())) && getTo().equals(plane.getPlanedPoint(getTo()));
    }

    public Point getTo() {
        return this.getVector().fromPoint(this.getFrom());
    }

    @Override
    public int compareTo(Line o) {
        int curr = getFrom().compareTo(o.getFrom());
        if(curr != 0)
            return curr;
        return getTo().compareTo(o.getTo());
    }
}
