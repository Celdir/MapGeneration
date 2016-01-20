import simplevoronoi.*;
import pcg.Pcg32;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;

public class RPGMap {
    private double width;
    private double height;
    public List<Tile> tiles;

    public RPGMap(int numTiles, double width, double height) {
        this.width = width;
        this.height = height;
        generateTiles(numTiles);
        smoothPointDistribution(2);
        sortTiles();
        assignBiomes();
    }

    private void generateTiles(int numTiles) {
        double[] xVals = new double[numTiles];
        double[] yVals = new double[numTiles];
        Pcg32 r = new Pcg32();
        for (int i = 0; i < xVals.length; i++) {
            xVals[i] = r.nextDouble(width);
            yVals[i] = r.nextDouble(height);
        }
        Voronoi v = new Voronoi(0.00001f);

        tiles = v.generateVoronoi(xVals, yVals, 0, width, 0, height);
    }

    private void smoothPointDistribution(int smoothness) {
        for (int i = 0; i < smoothness; i++) {
            double[] xVals = new double[tiles.size()];
            double[] yVals = new double[tiles.size()];
            
            for (int j = 0; j < tiles.size(); j++) {
                double xSum = 0;
                double ySum = 0;
                for (GraphEdge e : tiles.get(j).getEdges()) {
                    xSum += e.x1 + e.x2;
                    ySum += e.y1 + e.y2;
                }
                xVals[j] = xSum / (2 * tiles.get(j).getEdges().size());
                yVals[j] = ySum / (2 * tiles.get(j).getEdges().size());
            }
            Voronoi v = new Voronoi(0.00001f);

            tiles = v.generateVoronoi(xVals, yVals, 0, width, 0, height);
        }
    }

    private void sortTiles() {
        Collections.sort(tiles, new Comparator<Tile>() {
            @Override
            public final int compare(Tile t1, Tile t2) {
                Site s1 = t1.getSite();
                Site s2 = t2.getSite();

                if (s1.getY() < s2.getY()) {
                    return -1;
                }
                if (s1.getY() > s2.getY()) {
                    return 1;
                }
                return 0;
            }
        });

        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).getSite().setNumber(i);
        }
    }

    private void assignBiomes() {
        // seed biomes
        Pcg32 r = new Pcg32();
        int tundraSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int plainsSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int hillSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int mountainSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int forestSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int desertSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;
        int lakeSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) + 1) + 1;

        List<Tile> tundraTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToTundra = new HashSet<Tile>();
        for (int i = 0; i < tundraSeeds; i++) {
            // put tundra seeds in the top 10th of the map
            int tileIndex = r.nextInt(tiles.size() / 10);
            tiles.get(tileIndex).setBiome(Tile.Biome.TUNDRA);
            tundraTiles.add(tiles.get(tileIndex));
            adjacentToTundra.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> plainsTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToPlains = new HashSet<Tile>();
        for (int i = 0; i < plainsSeeds; i++) {
            // put plains seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.PLAINS);
            plainsTiles.add(tiles.get(tileIndex));
            adjacentToPlains.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> hillTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToHill = new HashSet<Tile>();
        for (int i = 0; i < hillSeeds; i++) {
            // put hill seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.HILL);
            hillTiles.add(tiles.get(tileIndex));
            adjacentToHill.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> mountainTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToMountain = new HashSet<Tile>();
        for (int i = 0; i < mountainSeeds; i++) {
            // put mountain seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.MOUNTAIN);
            mountainTiles.add(tiles.get(tileIndex));
            adjacentToMountain.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> forestTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToForest = new HashSet<Tile>();
        for (int i = 0; i < forestSeeds; i++) {
            // put forest seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.FOREST);
            forestTiles.add(tiles.get(tileIndex));
            adjacentToForest.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> desertTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToDesert = new HashSet<Tile>();
        for (int i = 0; i < desertSeeds; i++) {
            // put desert seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.DESERT);
            desertTiles.add(tiles.get(tileIndex));
            adjacentToDesert.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> lakeTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToLake = new HashSet<Tile>();
        for (int i = 0; i < lakeSeeds; i++) {
            // put lake seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.LAKE);
            lakeTiles.add(tiles.get(tileIndex));
            adjacentToLake.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }
    }
}
