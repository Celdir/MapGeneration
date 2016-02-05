import simplevoronoi.Tile;
import simplevoronoi.GraphEdge;

import java.util.List;
import java.util.ArrayList;
import java.lang.Double;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
//import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private RPGMap map;

    private void resetMap(int numTiles, double width, double height) {
        map = new RPGMap(numTiles, width, height);
    }

    private Group drawMap() {
        resetMap(500, 1280, 1024);

        Group root = new Group();

        Group tiles = new Group();
        for (Tile t : map.tiles) {
            Double[] pointsArray = new Double[2*t.getPoints().size()];
            for (int i = 0; i < t.getPoints().size(); i++) {
                pointsArray[2*i] = t.getPoints().get(i).getX();
                pointsArray[2*i+1] = t.getPoints().get(i).getY();
            }
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(pointsArray);
            
            Color color = Color.BLACK;
            switch (t.getBiome()) {
                case TUNDRA: color = Color.GHOSTWHITE; break;
                case PLAINS: color = Color.LAWNGREEN; break;
                case HILL: color = Color.GREY; break;
                case MOUNTAIN: color = Color.DARKGREY; break;
                case FOREST: color = Color.GREEN; break;
                case DESERT: color = Color.GOLD; break;
                case LAKE: color = Color.BLUE; break;
            }

            polygon.setFill(color);

            tiles.getChildren().add(polygon);
        }

        root.getChildren().add(tiles);

        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        final Group root = new Group();
        root.getChildren().add(drawMap());
        Scene scene = new Scene(root, 1280, 1024);

	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE: 
                        root.getChildren().clear();
                        root.getChildren().add(drawMap());
                }
            }
        });

        
        primaryStage.setTitle("RPGMap");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
