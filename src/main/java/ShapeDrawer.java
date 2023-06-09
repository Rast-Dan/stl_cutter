import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.*;

public class ShapeDrawer {
    List<PlaneShape> shapes;
    List<PlaneShape> normalized;

    VolumePoint base;

    public ShapeDrawer(List<PlaneShape> shapes, VolumePoint base) {
        this.shapes = shapes;
        this.base = base;
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
    private final static Integer MAX = 500;
    private final static Integer OFFSET = 10;

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

    private BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(MAX + 2 * OFFSET, MAX + 2 * OFFSET, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();  // not sure on this line, but this seems more right
        g.setColor(Color.white);
        g.fillRect(0, 0, MAX + 2 * OFFSET, MAX + 2 * OFFSET); // give the whole image a white background
        Double maxX = null;
        Double minX = null;
        Double maxY = null;
        Double minY = null;
        List<PlaneShape.Line> lines = new ArrayList<>();
        PlaneShape.PlanePoint basePoint = new PlaneShape.PlanePoint(0., 0., base);
        lines.add(new PlaneShape.Line(basePoint, basePoint));
        PlaneShape baseShape = new PlaneShape(lines);
        shapes.add(baseShape);
        for(PlaneShape shape : shapes)
            for(PlaneShape.Line line : shape.getLines()) {
                for(PlaneShape.PlanePoint planePoint : line.getPoints()) {
                    if(maxX == null || planePoint.getX() > maxX)
                        maxX = planePoint.getX();
                    if(minX == null || planePoint.getX() < minX)
                        minX = planePoint.getX();
                    if(maxY == null || planePoint.getY() > maxY)
                        maxY = planePoint.getY();
                    if(minY == null || planePoint.getY() < minY)
                        minY = planePoint.getY();
                }
            }
        Double xMul = 1. / (maxX - minX);
        Double yMul = 1. / (maxY - minY);
        Double mul = Math.min(xMul, yMul) * MAX;
        minX -= (double)OFFSET / mul;
        minY -= (double)OFFSET / mul;
        normalized = new ArrayList<>();
        for(PlaneShape shape : shapes) {
            normalized.add(shape.getNormalized(minX, minY, mul));
        }
        for(PlaneShape normalizedShape : normalized) {
            CustomShape customShape = new CustomShape(normalizedShape);
            g.setColor(Color.black);
            g.draw(customShape);
        }
        colorShapes(image);
        lines = new ArrayList<>();
        PlaneShape.PlanePoint normalizedPoint = basePoint.getNormalized(minX, minY, mul);
        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(normalizedPoint.getX(), 0., null),
                new PlaneShape.PlanePoint(normalizedPoint.getX(), 1. * COUNT, null)));
        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(0., normalizedPoint.getY(), null),
                new PlaneShape.PlanePoint(1. * COUNT, normalizedPoint.getY(), null)));

        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(normalizedPoint.getX(), 0., null),
                new PlaneShape.PlanePoint(normalizedPoint.getX() - 5, 5., null)));
        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(normalizedPoint.getX(), 0., null),
                new PlaneShape.PlanePoint(normalizedPoint.getX() + 5, 5., null)));

        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(1. * COUNT - 5, normalizedPoint.getY() - 5, null),
                new PlaneShape.PlanePoint(1. * COUNT, normalizedPoint.getY(), null)));
        lines.add(new PlaneShape.Line(new PlaneShape.PlanePoint(1. * COUNT - 5, normalizedPoint.getY() + 5, null),
                new PlaneShape.PlanePoint(1. * COUNT, normalizedPoint.getY(), null)));

        CustomShape customShape = new CustomShape(new PlaneShape(lines));
        g.setColor(Color.black);
        g.draw(customShape);
        return image;
    }
    private PlaneShape.PlanePoint getNearestPoint(Double x, Double y) {
        Double minSize = 1. * COUNT * COUNT;
        PlaneShape.PlanePoint result = null;
        for(PlaneShape shape : normalized)
            for(PlaneShape.Line line : shape.getLines()) {
                for (PlaneShape.PlanePoint planePoint : line.getPoints()) {
                    Double xDist = Math.abs(x - planePoint.getX());
                    Double yDist = Math.abs(y - planePoint.getY());
                    Double currSize = Math.sqrt(xDist * xDist + yDist * yDist);
                    if(currSize < minSize) {
                        minSize = currSize;
                        result = planePoint;
                    }
                }
            }
        if(minSize > 5)
            return null;
        return result;
    }
    public void showImage() {
        BufferedImage img = toBufferedImage();
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        JPanel panel = (JPanel)frame.getContentPane();
        panel.setLayout(null);
        JLabel label = new JLabel(icon);
        panel.add(label);
        JTextPane textPane = new JTextPane();
        textPane.setBounds(COUNT + 5, 10, 225, 20);
        textPane.setEditable(false);
        panel.add(textPane);
        frame.setSize(COUNT + 250,COUNT + 50);
        label.setBounds(0, 0, COUNT, COUNT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mouseLocation = new Point(e.getX(), e.getY());
                if(mouseLocation.getX() < 0 || mouseLocation.getX() >= COUNT ||
                        mouseLocation.getY() < 0 || mouseLocation.getY() >= COUNT)
                    return;
                PlaneShape.PlanePoint nearest = getNearestPoint(mouseLocation.getX(), mouseLocation.getY());
                if(nearest == null)
                    return;
                textPane.setText(nearest.getOriginal().toString());
            }
        });
    }
    public void saveToFile(String filename) throws IOException {
        BufferedImage image = toBufferedImage();
        File outputfile = new File(filename);
        ImageIO.write(image, "jpg", outputfile);
    }
}
