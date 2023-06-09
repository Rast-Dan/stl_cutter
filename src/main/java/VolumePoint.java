public class VolumePoint implements Comparable<VolumePoint> {
    private static final Double e = 0.00000000001;
    private final Double x;
    private final Double y;
    private final Double z;
    public static Double getE() {
        return e;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public VolumePoint(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static boolean doubleIsEqual(Double a, Double b) {
        return (Math.abs(a - b) < e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VolumePoint volumePoint)) return false;

        if (!doubleIsEqual(getX(), volumePoint.getX())) return false;
        if (!doubleIsEqual(getY(), volumePoint.getY())) return false;
        return doubleIsEqual(getZ(), volumePoint.getZ());
    }

    @Override
    public int hashCode() {
        int result = getX().hashCode();
        result = 31 * result + getY().hashCode();
        result = 31 * result + getZ().hashCode();
        return result;
    }

    public VolumePoint(Stl.Vec3d vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    @Override
    public int compareTo(VolumePoint volumePoint) {
        if(!doubleIsEqual(getX(), volumePoint.getX()))
            return getX() < volumePoint.getX() ? -1 : 1;
        if(!doubleIsEqual(getY(), volumePoint.getY()))
            return getY() < volumePoint.getY() ? -1 : 1;
        if(!doubleIsEqual(getZ(), volumePoint.getZ()))
            return getZ() < volumePoint.getZ() ? -1 : 1;
        return 0;
    }

    public static VolumePoint getRandom() {
        double max = 100.;
        Double x = (Math.random() - .5) * max;
        Double y = (Math.random() - .5) * max;
        Double z = (Math.random() - .5) * max;
        return new VolumePoint(x, y, z);
    }

    String formatDouble(Double value) {
        return String.format("%.3f", value);
    }

    @Override
    public String toString() {
        return "(" + formatDouble(x) + "; " + formatDouble(y) + "; " + formatDouble(z) + ")";
    }
}
