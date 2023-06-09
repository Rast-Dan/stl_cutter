import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaneShape {
    public static class PlanePoint {
        private final Double x;
        private final Double y;

        private final VolumePoint original;
        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }
        public VolumePoint getOriginal() {
            return original;
        }

        public PlanePoint(Double x, Double y, VolumePoint original) {
            this.x = x;
            this.y = y;
            this.original = original;
        }
        public PlanePoint getNormalized(Double xMin, Double yMin, Double mul) {
            return new PlanePoint((getX() - xMin) * mul, (getY() - yMin) * mul, original);
        }

        @Override
        public String toString() {
            return String.format("%.3f", x) + "," + String.format("%.3f", y);
        }
    }
    public static class Line {
        PlanePoint from;
        PlanePoint to;

        public PlanePoint getFrom() {
            return from;
        }

        public PlanePoint getTo() {
            return to;
        }

        public List<PlanePoint> getPoints() {
            List<PlanePoint> res = new ArrayList<>();
            res.add(getFrom());
            res.add(getTo());
            return res;
        }

        public Line(PlanePoint from, PlanePoint to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return getPoints().stream().map(PlanePoint::toString).collect(Collectors.joining(","));
        }
    }
    List<PlaneShape.Line> lines;

    public List<Line> getLines() {
        return lines;
    }

    public PlaneShape(List<Line> lines) {
        this.lines = lines;
    }

    public PlaneShape getNormalized(Double minX, Double minY, Double mul) {
        List<Line> lines = new ArrayList<>();
        for(Line line : getLines()) {
            PlanePoint newFrom = line.getFrom().getNormalized(minX, minY, mul);
            PlanePoint newTo = line.getTo().getNormalized(minX, minY, mul);
            lines.add(new Line(newFrom, newTo));
        }
        return new PlaneShape(lines);
    }

    @Override
    public String toString() {
        return getLines().stream().map(Line::toString).collect(Collectors.joining("; "));
    }
}
