package simplevoronoi;

public class Tile {
    private int tilenbr; // Tile number, gotten from sitenbr of GraphEdges
    private GraphEdge[] edges;

    public Tile(int tilenbr, GraphEdge[] edges) {
        this.tilenbr = tilenbr;
        this.edges = edges;
    }

    public int number() {
        return tilenbr;
    }

    public GraphEdge[] getEdges() {
        return edges;
    }
}
