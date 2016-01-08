import java.util.List;
import java.lang.Math;
import simplevoronoi.*;

public class Test {
    public static void main(String[] args) {
        double[] xVals = {1, 2, 3, 26, 7};
        double[] yVals = {1, 2, 3, 5, 28};
        Voronoi v = new Voronoi(1);
        List<GraphEdge> allEdges = v.generateVoronoi(xVals, yVals, 0, 50, 0, 50);
        for (GraphEdge e : allEdges) {
            System.out.println("(" + e.x1 + ", " + e.y1 + ") to (" + e.x2 + ", " + e.y2 + ")");
        }
    }
}
