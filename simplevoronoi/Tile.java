/*
 * 2016 - Written by Michael Earl
 */

package simplevoronoi;

public class Tile {
    private Site site;
    private GraphEdge[] edges;

    public Tile(Site site, GraphEdge[] edges) {
        this.site = site;
        this.edges = edges;
    }

    public Site getSite() {
        return site;
    }

    public GraphEdge[] getEdges() {
        return edges;
    }
}
