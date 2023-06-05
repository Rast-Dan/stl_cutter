import java.io.IOException;
import java.util.List;

public class StlCutter {

    public static void main(String[] argsString) throws IOException {
        System.out.println("Hello world!");
        Args args = new Args(argsString);
        Plane plane = new Plane(args.getStartPoint(), args.getNormal());
        Solid solid = FileReader.readFromFile(args.getStlFileName());
        Vector baseVector = args.getBase();
        if(baseVector == null) {
            baseVector = plane.getRandomVector();
        }
        List<PlaneShape> result = solid.intersect(plane, baseVector);
        ShapeDrawer drawer = new ShapeDrawer(result);
        drawer.saveToFile(args.getImageFileName());
    }
}