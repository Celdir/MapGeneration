import simplevoronoi.*;
import pcg.Pcg32;

import java.util.List;

public class RPGMap {
    public List<Tile> tiles;

    public RPGMap(int numTiles, double width, double height) {
        generateTiles(numTiles, width, height);
        assignBiomes();
    }

    private void generateTiles(int numTiles, double width, double height) {
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

    private void assignBiomes() {
        
    }
}
