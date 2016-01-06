import java.util.ArrayList;
import simplevoronoi.*;

public class Test {
    public static void main(String[] args) {
        Voronoi v = new Voronoi(0.00001f);
        List<GraphEdge> allEdges = v.generateVoronoi();
    }
}
