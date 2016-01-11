import simplevoronoi.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test extends JPanel {
    private List<Tile> tiles;

    private void makeVoronoi() {
        double[] xVals = new double[1000];
        double[] yVals = new double[1000];
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            xVals[i] = 1280 * r.nextDouble();
            yVals[i] = 1024 * r.nextDouble();
        }
        Voronoi v = new Voronoi(0.00001f);

        tiles = v.generateVoronoi(xVals, yVals, 0, 1280, 0, 1024);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Tile t : tiles) {
            for (GraphEdge e : t.getEdges()) {
                g2d.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2));
            }
            g2d.draw(new Ellipse2D.Double(t.getSite().getX(), t.getSite().getY(), 1, 1));
        }
    }

    public static void main(String[] args) {
        final Test t = new Test();
        t.makeVoronoi();
        t.setFocusable(true);
        t.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                t.makeVoronoi();
                t.repaint();
            }
        });

        JFrame frame = new JFrame("Test");
        frame.add(t);
        frame.setSize(1280, 1024);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
