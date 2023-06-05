public class Args {
    private String stlFileName = "source.stl";
    private String imageFileName = "image.jpg";
    private Point startPoint = new Point(0., 0., 0.);
    private Vector normal = new Vector(1., 0., 0.);
    private Vector base;
    private String[] args;
    private Integer curr;

    public String getStlFileName() {
        return stlFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector getBase() {
        return base;
    }
    private boolean hasNext() {
        return curr < args.length;
    }
    private String getArg() {
        if(curr == args.length)
            throw new RuntimeException("Incorrect params");
        return args[curr++];
    }
    private Double getDouble() {
        String arg = getArg();
        return Double.valueOf(arg);
    }
    private void printHelp() {
        System.out.println("--help: this text");
        System.out.println("--stl_filename: path to source filename with stl model");
        System.out.println("--image_filename: path to save result image");
        System.out.println("--start_point: point of cut-plane in format: x y z");
        System.out.println("--normal: normal vector of cut-plane in format: x y z");
        System.out.println("--base: one of two base vector on cut plane in format: x y z");
        System.out.println("Ex: --stl_filename cube.stl --image_filename image.jpg " +
                "--start_point 0 0 0 --normal 1 0 0 --base 0 1 0");
    }
    public Args(String[] args) {
        this.args = args;
        this.curr = 0;
        while(hasNext()) {
            String arg = getArg();
            switch (arg.toLowerCase()) {
                case "--help" -> printHelp();
                case "--stl_filename" -> this.stlFileName = getArg();
                case "--image_filename" -> this.imageFileName = getArg();
                case "--start_point" -> this.startPoint = new Point(getDouble(), getDouble(), getDouble());
                case "--normal" -> this.normal = new Vector(getDouble(), getDouble(), getDouble());
                case "--base" -> this.base = new Vector(getDouble(), getDouble(), getDouble());
            }
        }
    }
}
