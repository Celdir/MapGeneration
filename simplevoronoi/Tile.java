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
    private ArrayList<Point2D> points;
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

    public void determineCorner(double width, double height) {
        boolean left = false, right = false, top = false, bottom = false;
        for (GraphEdge e : edges) {
            if (e.x1 == 0 || e.x2 == 0) left = true;
            if (e.x1 == width || e.x2 == width) right = true;
            if (e.y1 == 0 || e.y2 == 0) top = true;
            if (e.y1 == height || e.y2 == height) bottom = true;
        }

        if (left && top) points.add(new Point2D(0, 0));
        if (right && top) points.add(new Point2D(width, 0));
        if (left && bottom) points.add(new Point2D(0, height));
        if (right && bottom) points.add(new Point2D(width, height));
    }

    public void organizePoints(double width, double height) {
        determineCorner(width, height);
        HashSet<Point2D> pointSet = new HashSet<Point2D>();
        for (GraphEdge e : edges) {
            pointSet.add(new Point2D(e.x1, e.y1));
            pointSet.add(new Point2D(e.x2, e.y2));
        }
        ConvexHull c = new ConvexHull();
        for (Point2D p : pointSet) {
            points.add(p);
        }
        points = c.quickHull(points);
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }
}
