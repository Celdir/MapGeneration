import simplevoronoi.*;
import pcg.Pcg32;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
        int lakeSeeds = r.nextInt((int) Math.ceil(Math.log10(tiles.size())) * 3 + 1) + 1; // more lake seeds because lake biomes are composed of single tiles

        int tileCount = 0;

        List<Tile> tundraTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToTundra = new HashSet<Tile>();
        for (int i = 0; i < tundraSeeds; i++) {
            // put tundra seeds in the top 10th of the map
            int tileIndex = r.nextInt(tiles.size() / 10);
            tiles.get(tileIndex).setBiome(Tile.Biome.TUNDRA);
            tundraTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToTundra.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> plainsTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToPlains = new HashSet<Tile>();
        for (int i = 0; i < plainsSeeds; i++) {
            // put plains seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.PLAINS);
            plainsTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToPlains.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> hillTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToHill = new HashSet<Tile>();
        for (int i = 0; i < hillSeeds; i++) {
            // put hill seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.HILL);
            hillTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToHill.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> mountainTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToMountain = new HashSet<Tile>();
        for (int i = 0; i < mountainSeeds; i++) {
            // put mountain seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.MOUNTAIN);
            mountainTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToMountain.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> forestTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToForest = new HashSet<Tile>();
        for (int i = 0; i < forestSeeds; i++) {
            // put forest seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.FOREST);
            forestTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToForest.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> desertTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToDesert = new HashSet<Tile>();
        for (int i = 0; i < desertSeeds; i++) {
            // put desert seeds in the bottom 90% of map
            int tileIndex = r.nextInt(tiles.size() * 9 / 10) + tiles.size() / 10;
            tiles.get(tileIndex).setBiome(Tile.Biome.DESERT);
            desertTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToDesert.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        List<Tile> lakeTiles = new ArrayList<Tile>();
        HashSet<Tile> adjacentToLake = new HashSet<Tile>();
        for (int i = 0; i < lakeSeeds; i++) {
            // put lake seeds anywhere in map
            int tileIndex = r.nextInt(tiles.size());
            tiles.get(tileIndex).setBiome(Tile.Biome.LAKE);
            lakeTiles.add(tiles.get(tileIndex));
            tileCount++;
            adjacentToLake.addAll(tiles.get(tileIndex).getAdjacentTiles());
        }

        // grow biomes

        while (tileCount < tiles.size()) {
            // tundra
            for (int i = 0; i < 4; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToTundra.iterator();
                int index = r.nextInt(adjacentToTundra.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED && t.getSite().getNumber() <= tiles.size() / 10) {
                    adjacentToTundra.remove(t);
                    t.setBiome(Tile.Biome.TUNDRA);
                    tundraTiles.add(t);
                    adjacentToTundra.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }
            
            // plains
            for (int i = 0; i < 3; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToPlains.iterator();
                int index = r.nextInt(adjacentToPlains.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED) {
                    adjacentToPlains.remove(t);
                    t.setBiome(Tile.Biome.PLAINS);
                    plainsTiles.add(t);
                    adjacentToPlains.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }

            // hill
            for (int i = 0; i < 1; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToHill.iterator();
                int index = r.nextInt(adjacentToHill.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED) {
                    adjacentToHill.remove(t);
                    t.setBiome(Tile.Biome.HILL);
                    hillTiles.add(t);
                    adjacentToHill.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }

            // mountain
            for (int i = 0; i < 1; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToMountain.iterator();
                int index = r.nextInt(adjacentToMountain.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED) {
                    adjacentToMountain.remove(t);
                    t.setBiome(Tile.Biome.MOUNTAIN);
                    mountainTiles.add(t);
                    adjacentToMountain.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }

            // forest
            for (int i = 0; i < 2; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToForest.iterator();
                int index = r.nextInt(adjacentToForest.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED) {
                    adjacentToForest.remove(t);
                    t.setBiome(Tile.Biome.FOREST);
                    forestTiles.add(t);
                    adjacentToForest.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }

            // desert
            for (int i = 0; i < 3; i++) {
                Tile t = tiles.get(0);
                Iterator it = adjacentToDesert.iterator();
                int index = r.nextInt(adjacentToDesert.size());
                for (int j = 0; j <= index; j++) {
                    t = (Tile) it.next();
                }
                
                if (t.getBiome() == Tile.Biome.UNASSIGNED) {
                    adjacentToDesert.remove(t);
                    t.setBiome(Tile.Biome.DESERT);
                    desertTiles.add(t);
                    adjacentToDesert.addAll(t.getAdjacentTiles());
                    tileCount++;
                }
            }
        }
    }
}
