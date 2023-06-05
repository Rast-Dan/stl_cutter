public class Plane {
    private final Point point;
    private final Vector normal;

    public Point getPoint() {
        return point;
    }

    public Vector getNormal() {
        return normal;
    }

    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.getNormalized();
    }

    public Point getPlanedPoint(Point point) {
        Vector fromPointToOrig = new Vector(point, this.getPoint());
        Double fromOnPlane = fromPointToOrig.scalar(this.getNormal());
        Vector offset = this.getNormal().mul(fromOnPlane);
        return offset.fromPoint(point);
    }

    public Point getRandomPoint() {
        return getPlanedPoint(Point.getRandom());
    }

    public Vector getRandomVector() {
        Vector result = new Vector(getRandomPoint(), getRandomPoint());
        return result.mul(1. / result.size());
    }

}
