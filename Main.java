import simplevoronoi.Tile;
import simplevoronoi.GraphEdge;

import java.util.List;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private RPGMap map;

    private void resetMap(int numTiles, double width, double height) {
        map = new RPGMap(numTiles, width, height);
    }

    private Group drawMap() {
        resetMap(1000, 1280, 1024);

        Group root = new Group();

        Group tiles = new Group();
        Group points = new Group();
        for (Tile t : map.tiles) {
            for (GraphEdge e : t.getEdges()) {
                Line line = new Line(e.x1, e.y1, e.x2, e.y2);
                tiles.getChildren().add(line);
            }

            Color color = Color.BLACK;
            switch (t.getBiome()) {
                case PLAINS: color = Color.LAWNGREEN; break;
                case MOUNTAIN: color = Color.DARKGREY; break;
                case HILL: color = Color.GREY; break;
                case FOREST: color = Color.GREEN; break;
                case DESERT: color = Color.GOLD; break;
                case TUNDRA: color = Color.GHOSTWHITE; break;
            }
            Circle c = new Circle(t.getSite().getX(), t.getSite().getY(), 2, color);
            points.getChildren().add(c);
        }

        root.getChildren().add(tiles);
        root.getChildren().add(points);

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
