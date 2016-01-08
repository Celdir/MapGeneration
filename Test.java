import java.util.List;
import java.util.Random;
import java.lang.Math;
import simplevoronoi.*;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Test extends JPanel {
    private List<GraphEdge> allEdges;

    private void makeVoronoi() {
        double[] xVals = new double[500];
        double[] yVals = new double[500];
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            xVals[i] = r.nextInt(1280);
            yVals[i] = r.nextInt(1024);
        }
        Voronoi v = new Voronoi(1);
        allEdges = v.generateVoronoi(xVals, yVals, 0, 1280, 0, 1024);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (GraphEdge e : allEdges) {
            g2d.draw(new Line2D.Double(e.x1, e.y1, e.x2, e.y2));
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
