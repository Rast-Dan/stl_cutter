import java.util.ArrayList;
import java.util.List;

public class PlaneShape {
    public static class Point {
        private Double x;
        private Double y;

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }
        public Point getNormalized(Double xMin, Double yMin, Double mul) {
            return new Point((getX() - xMin) * mul, (getY() - yMin) * mul);
        }
    }
    public static class Line {
        Point from;
        Point to;

        public Point getFrom() {
            return from;
        }

        public Point getTo() {
            return to;
        }

        public List<Point> getPoints() {
            List<Point> res = new ArrayList<>();
            res.add(getFrom());
            res.add(getTo());
            return res;
        }

        public Line(Point from, Point to) {
            this.from = from;
            this.to = to;
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
            Point newFrom = line.getFrom().getNormalized(minX, minY, mul);
            Point newTo = line.getTo().getNormalized(minX, minY, mul);
            lines.add(new Line(newFrom, newTo));
        }
        return new PlaneShape(lines);
    }
}
