public class Vector {
    private final Double x;
    private final Double y;
    private final Double z;

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public Vector(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(VolumePoint from, VolumePoint to) {
        this(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public Double scalar(Vector any) {
        return this.x * any.x + this.y * any.y + this.z * any.z;
    }

    public Vector mul(Double coef) {
        return new Vector(getX() * coef, getY() * coef, getZ() * coef);
    }

    public VolumePoint fromPoint(VolumePoint volumePoint) {
        return new VolumePoint(volumePoint.getX() + getX(), volumePoint.getY() + getY(), volumePoint.getZ() + getZ());
    }
    public double size() {
        return Math.sqrt(Math.pow(getX(), 2.) + Math.pow(getY(), 2.) + Math.pow(getZ(), 2.));
    }

    public Vector cross(Vector any) {
        double x = this.getY() * any.getZ() - this.getZ() * any.getY();
        double y = this.getZ() * any.getX() - this.getX() * any.getZ();
        double z = this.getX() * any.getY() - this.getY() * any.getX();
        return new Vector(x, y, z);
    }

    public Vector getNormalized() {
        return mul(1. / size());
    }
}
