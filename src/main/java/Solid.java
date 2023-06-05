import java.util.*;

public class Solid {
    private final List<Triangle> triangles;

    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solid solid)) return false;

        return getTriangles().equals(solid.getTriangles());
    }

    @Override
    public int hashCode() {
        return getTriangles().hashCode();
    }

    public Solid(List<Triangle> triangles) {
        this.triangles = triangles;
    }
    List<List<Line>> intersectionLines(Plane plane) {
        List<Line> all = new ArrayList<>();
        for(Triangle triangle : getTriangles()) {
            all.addAll(triangle.intersect(plane));
        }
        List<List<Line>> result = new ArrayList<>();
        Map<Point, List<Line>> linesByPoint = new TreeMap<>();
        for(Line line : all) {
            for(Point point : line.getPoints()) {
                if(!linesByPoint.containsKey(point))
                    linesByPoint.put(point, new ArrayList<>());
                linesByPoint.get(point).add(line);
            }
        }
        Set<Line> wasVisited = new TreeSet<>();
        for(Line currLine : all) {
            if(wasVisited.contains(currLine))
                continue;
            wasVisited.add(currLine);
            List<Line> curr = new ArrayList<>();
            Queue<Line> q = new LinkedList<>();
            q.add(currLine);
            curr.add(currLine);
            while(!q.isEmpty()) {
                Line line = q.remove();
                for(Point point : line.getPoints()) {
                    for(Line e : linesByPoint.get(point)) {
                        if(wasVisited.contains(e))
                            continue;
                        wasVisited.add(e);
                        q.add(e);
                        curr.add(e);
                    }
                }
            }
            result.add(curr);
        }
        return result;
    }
    private static PlaneShape.Point getPlanePoint(Point point, Point start, Vector orientationX, Vector orientationY) {
        Vector pointVector = new Vector(start, point);
        Double x = orientationX.scalar(pointVector);
        Double y = orientationY.scalar(pointVector);
        return new PlaneShape.Point(x, y);
    }
    private static PlaneShape.Line getPlaneLine(Line line, Point start, Vector orientationX, Vector orientationY) {
        PlaneShape.Point from = getPlanePoint(line.getFrom(), start, orientationX, orientationY);
        PlaneShape.Point to = getPlanePoint(line.getTo(), start, orientationX, orientationY);
        return new PlaneShape.Line(from, to);
    }
    List<PlaneShape> intersect(Plane plane, Vector baseVector) {
        Vector secondBase = baseVector.cross(plane.getNormal());
        secondBase = secondBase.mul(1. / secondBase.size());
        Point basePoint = plane.getPoint();
        List<PlaneShape> result = new ArrayList<>();
        for(List<Line> lines : intersectionLines(plane)) {
            List<PlaneShape.Line> curr = new ArrayList<>();
            for(Line line : lines) {
                curr.add(getPlaneLine(line, basePoint, baseVector, secondBase));
            }
            result.add(new PlaneShape(curr));
        }
        return result;
    }

    List<PlaneShape> intersect(Plane plane) {
        return intersect(plane, plane.getRandomVector());
    }
}
