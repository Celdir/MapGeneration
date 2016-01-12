/*
 * 2016 - Written by Michael Earl
 */

package simplevoronoi;

import java.util.List;
import java.util.ArrayList;

public class Tile {
    private Site site;
    private List<GraphEdge> edges;
    private List<Tile> adjacentTiles;

    public Tile(Site site) {
        this.site = site;
        this.edges = new ArrayList<GraphEdge>();
        this.adjacentTiles = new ArrayList<Tile>();
    }

    public Site getSite() {
        return site;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public List<Tile> getAdjacentTiles() {
        return adjacentTiles;
    }
}
