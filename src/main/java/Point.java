public class Point implements Comparable<Point> {
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

    public Point(Double x, Double y, Double z) {
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
        if (!(o instanceof Point point)) return false;

        if (!doubleIsEqual(getX(), point.getX())) return false;
        if (!doubleIsEqual(getY(), point.getY())) return false;
        return doubleIsEqual(getZ(), point.getZ());
    }

    @Override
    public int hashCode() {
        int result = getX().hashCode();
        result = 31 * result + getY().hashCode();
        result = 31 * result + getZ().hashCode();
        return result;
    }

    public Point(Stl.Vec3d vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    @Override
    public int compareTo(Point point) {
        if(!doubleIsEqual(getX(), point.getX()))
            return getX() < point.getX() ? -1 : 1;
        if(!doubleIsEqual(getY(), point.getY()))
            return getY() < point.getY() ? -1 : 1;
        if(!doubleIsEqual(getZ(), point.getZ()))
            return getZ() < point.getZ() ? -1 : 1;
        return 0;
    }

    public static Point getRandom() {
        double max = 100.;
        Double x = (Math.random() - .5) * max;
        Double y = (Math.random() - .5) * max;
        Double z = (Math.random() - .5) * max;
        return new Point(x, y, z);
    }
}
