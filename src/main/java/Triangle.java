import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Triangle {
    private final List<Line> lines;

    public List<Line> getLines() {
        return lines;
    }

    public Triangle(List<Line> lines) {
        assert lines.size() == 3;
        this.lines = lines;
    }
    private static List<Line> getLinesFromStlTriangle(Stl.Triangle triangle) {
        List<Point> points = new ArrayList<>();
        for(Stl.Vec3d vec3d : triangle.vertices()) {
            points.add(new Point(vec3d));
        }
        List<Line> lines = new ArrayList<>();
        for(int i = 0; i < points.size(); i++) {
            lines.add(new Line(points.get(i), points.get((i + 1) % points.size())));
        }
        return lines;
    }
    public Triangle(Stl.Triangle triangle) {
        this(getLinesFromStlTriangle(triangle));
    }
    public List<Line> intersect(Plane plane) {
        Set<Point> points = new TreeSet<>();
        List<Line> lines = new ArrayList<>();
        for(Line line : getLines()) {
            if(line.isOnPlane(plane)) {
                lines.add(line);
                continue;
            }
            Point intersection = line.intersect(plane);
            if(intersection == null)
                continue;
            points.add(intersection);
        }
        if(points.size() == 3) {
            for(Line line : getLines()) {
                if(line.isOnPlane(plane)) {
                    lines.add(line);
                    continue;
                }
                Point intersection = line.intersect(plane);
                if(intersection == null)
                    continue;
                points.add(intersection);
            }
            throw new RuntimeException("Некорректное количество точек перечения");
        }
        List<Point> pointList = points.stream().toList();
        if(points.size() == 1)
            lines.add(new Line(pointList.get(0), pointList.get(0)));
        if(points.size() == 2)
            lines.add(new Line(pointList.get(0), pointList.get(1)));
        return lines;
    }
}
