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
        List<VolumePoint> volumePoints = new ArrayList<>();
        for(Stl.Vec3d vec3d : triangle.vertices()) {
            volumePoints.add(new VolumePoint(vec3d));
        }
        List<Line> lines = new ArrayList<>();
        for(int i = 0; i < volumePoints.size(); i++) {
            lines.add(new Line(volumePoints.get(i), volumePoints.get((i + 1) % volumePoints.size())));
        }
        return lines;
    }
    public Triangle(Stl.Triangle triangle) {
        this(getLinesFromStlTriangle(triangle));
    }
    public List<Line> intersect(Plane plane) {
        Set<VolumePoint> volumePoints = new TreeSet<>();
        List<Line> lines = new ArrayList<>();
        for(Line line : getLines()) {
            if(line.isOnPlane(plane)) {
                lines.add(line);
                continue;
            }
            VolumePoint intersection = line.intersect(plane);
            if(intersection == null)
                continue;
            volumePoints.add(intersection);
        }
        if(volumePoints.size() == 3) {
            throw new RuntimeException("Некорректное количество точек перечения");
        }
        List<VolumePoint> volumePointList = volumePoints.stream().toList();
        if(volumePoints.size() == 2)
            lines.add(new Line(volumePointList.get(0), volumePointList.get(1)));
        return lines;
    }
}
