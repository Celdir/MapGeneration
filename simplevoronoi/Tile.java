/*
 * 2016 - Written by Michael Earl
 */

package simplevoronoi;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import javafx.geometry.Point2D;

public class Tile {
    public enum Biome {
        TUNDRA, PLAINS, HILL, MOUNTAIN, FOREST, DESERT, LAKE, UNASSIGNED;
    }

    private Site site;
    private List<GraphEdge> edges;
    private List<Point2D> points;
    private List<Tile> adjacentTiles;
    private Biome biome;

    public Tile(Site site) {
        this.site = site;
        this.edges = new ArrayList<GraphEdge>();
        this.points = new ArrayList<Point2D>();
        this.adjacentTiles = new ArrayList<Tile>();
        this.biome = Biome.UNASSIGNED;
    }

    public Site getSite() {
        return site;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public List<Tile> getAdjacentTiles() {
        return adjacentTiles;
    }

    public Biome getBiome() {
        return biome;
    }

    public void organizePoints() {
        HashSet<Point2D> pointSet = new HashSet<Point2D>();
        for (GraphEdge e : edges) {
            pointSet.add(new Point2D(e.x1, e.y1));
            pointSet.add(new Point2D(e.x2, e.y2));
        }
        ConvexHull c = new ConvexHull();
        ArrayList<Point2D> pList = new ArrayList<Point2D>();
        for (Point2D p : pointSet) {
            pList.add(p);
        }
        points = c.quickHull(pList);
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }
}
