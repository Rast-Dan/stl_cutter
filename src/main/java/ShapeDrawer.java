import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShapeDrawer {
    List<PlaneShape> shapes;

    public ShapeDrawer(List<PlaneShape> shapes) {
        this.shapes = shapes;
    }

    private static class CustomShape implements Shape {
        private GeneralPath generalPath = new GeneralPath();
        public CustomShape(PlaneShape planeShape) {
            for(var line : planeShape.lines) {
                generalPath.moveTo(line.getFrom().getX(), line.getFrom().getY());
                generalPath.lineTo(line.getTo().getX(), line.getTo().getY());
            }
            generalPath.closePath();
        }
        @Override
        public java.awt.Rectangle getBounds() {
            return generalPath.getBounds();
        }

        @Override
        public Rectangle2D getBounds2D() {
            return generalPath.getBounds2D();
        }

        @Override
        public boolean contains(double x, double y) {
            return generalPath.contains(x, y);
        }

        @Override
        public boolean contains(Point2D p) {
            return generalPath.contains(p);
        }

        @Override
        public boolean intersects(double x, double y, double w, double h) {
            return generalPath.intersects(x, y, w, h);
        }

        @Override
        public boolean intersects(Rectangle2D r) {
            return generalPath.intersects(r);
        }

        @Override
        public boolean contains(double x, double y, double w, double h) {
            return generalPath.contains(x, y,w ,h);
        }

        @Override
        public boolean contains(Rectangle2D r) {
            return generalPath.contains(r);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at) {
            return generalPath.getPathIterator(at);
        }

        @Override
        public PathIterator getPathIterator(AffineTransform at, double flatness) {
            return generalPath.getPathIterator(at, flatness);
        }
    }
    private final static Integer MAX = 1000;
    private final static Integer OFFSET = 50;

    private final static Integer COUNT = MAX + 2 * OFFSET;

    private static class Pair {
        Integer i;
        Integer j;

        public Integer getI() {
            return i;
        }

        public Integer getJ() {
            return j;
        }

        public Pair(Integer i, Integer j) {
            this.i = i;
            this.j = j;
        }
        public Pair add(Pair any) {
            return new Pair(this.i + any.i, this.j + any.j);
        }
    }

    private static void colorShapes(BufferedImage image) {
        List<List<Boolean>> visited = new ArrayList<>();
        for(int i = 0; i < COUNT; i++) {
            visited.add(new ArrayList<>());
            for(int j = 0; j < COUNT; j++)
                visited.get(i).add(false);
        }
        List<Pair> adds = new ArrayList<>();
        adds.add(new Pair(-1, 0));
        adds.add(new Pair(0, 1));
        adds.add(new Pair(1, 0));
        adds.add(new Pair(0, -1));
        for(int i = 0; i < COUNT; i++)
            for(int j = 0; j < COUNT; j++) {
                if(visited.get(i).get(j))
                    continue;
                Queue<Pair> q = new LinkedList<>();
                q.add(new Pair(i, j));
                Color color = (i == 0 && j == 0) ? Color.WHITE : new Color((int)(Math.random() * 16777216));
                while(!q.isEmpty()) {
                    Pair curr = q.remove();
                    for(Pair add : adds) {
                        Pair nw = curr.add(add);
                        if(Math.min(nw.getI(), nw.getJ()) < 0 || Math.max(nw.getI(), nw.getJ()) >= COUNT)
                            continue;
                        if(visited.get(nw.getI()).get(nw.getJ()) || image.getRGB(nw.getI(), nw.getJ()) == Color.black.getRGB())
                            continue;
                        image.setRGB(nw.getI(), nw.getJ(), color.getRGB());
                        visited.get(nw.getI()).set(nw.getJ(), true);
                        q.add(nw);
                    }
                }
            }
    }

    public void saveToFile(String filename) throws IOException {
        BufferedImage image = new BufferedImage(MAX + 2 * OFFSET, MAX + 2 * OFFSET, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();  // not sure on this line, but this seems more right
        g.setColor(Color.white);
        g.fillRect(0, 0, MAX + 2 * OFFSET, MAX + 2 * OFFSET); // give the whole image a white background
        Double maxX = null;
        Double minX = null;
        Double maxY = null;
        Double minY = null;
        for(PlaneShape shape : shapes)
            for(PlaneShape.Line line : shape.getLines()) {
                for(PlaneShape.Point point : line.getPoints()) {
                    if(maxX == null || point.getX() > maxX)
                        maxX = point.getX();
                    if(minX == null || point.getX() < minX)
                        minX = point.getX();
                    if(maxY == null || point.getY() > maxY)
                        maxY = point.getY();
                    if(minY == null || point.getY() < minY)
                        minY = point.getY();
                }
            }
        Double xMul = 1. / (maxX - minX);
        Double yMul = 1. / (maxY - minY);
        Double mul = Math.min(xMul, yMul) * MAX;
        minX -= (double)OFFSET / mul;
        minY -= (double)OFFSET / mul;
        for(PlaneShape shape : shapes) {
            PlaneShape normalized = shape.getNormalized(minX, minY, mul);
            CustomShape customShape = new CustomShape(normalized);
            g.setColor(Color.blue);
            g.fill(customShape);
            g.setColor(Color.black);
            g.draw(customShape);
        }
        colorShapes(image);
        File outputfile = new File(filename);
        ImageIO.write(image, "jpg", outputfile);
    }
}
