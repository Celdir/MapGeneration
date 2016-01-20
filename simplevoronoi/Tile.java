/*
 * 2016 - Written by Michael Earl
 */

package simplevoronoi;

import java.util.List;
import java.util.ArrayList;

public class Tile {
    public enum Biome {
        TUNDRA, PLAINS, HILL, MOUNTAIN, FOREST, DESERT, LAKE, UNASSIGNED;
    }

    private Site site;
    private List<GraphEdge> edges;
    private List<Tile> adjacentTiles;
    private Biome biome;

    public Tile(Site site) {
        this.site = site;
        this.edges = new ArrayList<GraphEdge>();
        this.adjacentTiles = new ArrayList<Tile>();
        this.biome = Biome.UNASSIGNED;
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

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }
}
