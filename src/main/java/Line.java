import java.util.ArrayList;
import java.util.List;

public class Line implements Comparable<Line> {
    private VolumePoint from;
    private Vector vector;

    public VolumePoint getFrom() {
        return from;
    }

    public Vector getVector() {
        return vector;
    }

    public List<VolumePoint> getPoints() {
        List<VolumePoint> result = new ArrayList<>();
        result.add(getFrom());
        result.add(getTo());
        return result;
    }

    public Line(VolumePoint from, Vector vector) {
        this.from = from;
        this.vector = vector;
    }

    public Line(VolumePoint from, VolumePoint to) {
        this(from, new Vector(from, to));
    }

    public VolumePoint intersect(Plane plane) {
        VolumePoint planeVolumePoint = plane.getRandomPoint();
        Vector fromAndPoint = new Vector(getFrom(), planeVolumePoint);
        Vector toAndPoint = new Vector(planeVolumePoint, getTo());
        Double fromOnPlane = fromAndPoint.scalar(plane.getNormal());
        Double toOnPlane = toAndPoint.scalar(plane.getNormal());
        if(Math.abs(fromOnPlane) < VolumePoint.getE())
            return getFrom();
        if(Math.abs(toOnPlane) < VolumePoint.getE())
            return getTo();
        if((fromOnPlane < -VolumePoint.getE()) ^ (toOnPlane < -VolumePoint.getE()))
            return null;
        Double coef = fromOnPlane / (fromOnPlane + toOnPlane);
        if(coef > 1 + VolumePoint.getE())
            return null;
        Vector sizeVector = getVector().mul(coef);
        return new Line(this.getFrom(), sizeVector).getTo();
    }

    public boolean isOnPlane(Plane plane) {
        return getFrom().equals(plane.getPlanedPoint(getFrom())) && getTo().equals(plane.getPlanedPoint(getTo()));
    }

    public VolumePoint getTo() {
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
