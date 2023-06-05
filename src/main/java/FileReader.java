import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    public static Solid readFromFile(String filename) throws IOException {
        Stl data = Stl.fromFile(filename);
        List<Triangle> triangles = new ArrayList<>();
        for(Stl.Triangle triangle : data.triangles()) {
            triangles.add(new Triangle(triangle));
        }
        return new Solid(triangles);
    }
}
