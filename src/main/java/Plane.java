public class Plane {
    private final VolumePoint volumePoint;
    private final Vector normal;

    public VolumePoint getPoint() {
        return volumePoint;
    }

    public Vector getNormal() {
        return normal;
    }

    public Plane(VolumePoint volumePoint, Vector normal) {
        this.volumePoint = volumePoint;
        this.normal = normal.getNormalized();
    }

    public VolumePoint getPlanedPoint(VolumePoint volumePoint) {
        Vector fromPointToOrig = new Vector(volumePoint, this.getPoint());
        Double fromOnPlane = fromPointToOrig.scalar(this.getNormal());
        Vector offset = this.getNormal().mul(fromOnPlane);
        return offset.fromPoint(volumePoint);
    }

    public VolumePoint getRandomPoint() {
        return getPlanedPoint(VolumePoint.getRandom());
    }

    public Vector getRandomVector() {
        Vector result = new Vector(getRandomPoint(), getRandomPoint());
        return result.mul(1. / result.size());
    }

}
