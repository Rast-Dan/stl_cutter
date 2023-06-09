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
        Map<VolumePoint, List<Line>> linesByPoint = new TreeMap<>();
        for(Line line : all) {
            for(VolumePoint volumePoint : line.getPoints()) {
                if(!linesByPoint.containsKey(volumePoint))
                    linesByPoint.put(volumePoint, new ArrayList<>());
                linesByPoint.get(volumePoint).add(line);
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
                for(VolumePoint volumePoint : line.getPoints()) {
                    for(Line e : linesByPoint.get(volumePoint)) {
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
    private static PlaneShape.PlanePoint getPlanePoint(VolumePoint volumePoint, VolumePoint start, Vector orientationX, Vector orientationY) {
        Vector pointVector = new Vector(start, volumePoint);
        Double x = orientationX.scalar(pointVector);
        Double y = orientationY.scalar(pointVector);
        return new PlaneShape.PlanePoint(x, y, volumePoint);
    }
    private static PlaneShape.Line getPlaneLine(Line line, VolumePoint start, Vector orientationX, Vector orientationY) {
        PlaneShape.PlanePoint from = getPlanePoint(line.getFrom(), start, orientationX, orientationY);
        PlaneShape.PlanePoint to = getPlanePoint(line.getTo(), start, orientationX, orientationY);
        return new PlaneShape.Line(from, to);
    }
    List<PlaneShape> intersect(Plane plane, Vector baseVector) {
        Vector secondBase = baseVector.cross(plane.getNormal());
        secondBase = secondBase.mul(1. / secondBase.size());
        VolumePoint baseVolumePoint = plane.getPoint();
        List<PlaneShape> result = new ArrayList<>();
        for(List<Line> lines : intersectionLines(plane)) {
            List<PlaneShape.Line> curr = new ArrayList<>();
            for(Line line : lines) {
                curr.add(getPlaneLine(line, baseVolumePoint, baseVector, secondBase));
            }
            result.add(new PlaneShape(curr));
        }
        return result;
    }

    List<PlaneShape> intersect(Plane plane) {
        return intersect(plane, plane.getRandomVector());
    }
}
