import simplevoronoi.*;
import pcg.Pcg32;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

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
        
    }
}
