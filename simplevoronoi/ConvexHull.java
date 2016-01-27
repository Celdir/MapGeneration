/*
 * Copyright (c) 2007 Alexander Hristov.
 * http://www.ahristov.com
 * 
 * Feel free to use this code as you wish, as long as you keep this copyright
 * notice. The only limitation on use is that this code cannot be republished
 * on other web sites. 
 *
 * As usual, this code comes with no warranties of any kind.
 *
 * 
 */

package simplevoronoi;

import java.util.ArrayList;
import javafx.geometry.Point2D;

public class ConvexHull {
    public ArrayList<Point2D> quickHull(ArrayList<Point2D> points) {
        ArrayList<Point2D> convexHull = new ArrayList<Point2D>();
        if (points.size() < 3) return (ArrayList)points.clone();
        // find extremals
        int minPoint2D = -1, maxPoint2D = -1;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < minX) {
                minX = points.get(i).getX();
                minPoint2D = i;
            } 
            if (points.get(i).getX() > maxX) {
                maxX = points.get(i).getX();
                maxPoint2D = i;       
            }
        }
        Point2D A = points.get(minPoint2D);
        Point2D B = points.get(maxPoint2D);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<Point2D> leftSet = new ArrayList<Point2D>();
        ArrayList<Point2D> rightSet = new ArrayList<Point2D>();

        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            if (pointLocation(A,B,p) == -1)
                leftSet.add(p);
            else
                rightSet.add(p);
        }
        hullSet(A,B,rightSet,convexHull);
        hullSet(B,A,leftSet,convexHull);

        return convexHull;
    }

    /*
     * Computes the square of the distance of point C to the segment defined by points AB
     */
    public double distance(Point2D A, Point2D B, Point2D C) {
        double ABx = B.getX()-A.getX();
        double ABy = B.getY()-A.getY();
        double num = ABx*(A.getY()-C.getY())-ABy*(A.getX()-C.getX());
        if (num < 0) num = -num;
        return num;
    }

    public void hullSet(Point2D A, Point2D B, ArrayList<Point2D> set, ArrayList<Point2D> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0) return;
        if (set.size() == 1) {
            Point2D p = set.get(0);
            set.remove(p);
            hull.add(insertPosition,p);
            return;
        }
        double dist = Integer.MIN_VALUE;
        int furthestPoint2D = -1;
        for (int i = 0; i < set.size(); i++) {
            Point2D p = set.get(i);
            double distance  = distance(A,B,p);
            if (distance > dist) {
                dist = distance;
                furthestPoint2D = i;
            }
        }
        Point2D P = set.get(furthestPoint2D);
        set.remove(furthestPoint2D);
        hull.add(insertPosition,P);

        // Determine who's to the left of AP
        ArrayList<Point2D> leftSetAP = new ArrayList<Point2D>();
        for (int i = 0; i < set.size(); i++) {
            Point2D M = set.get(i);
            if (pointLocation(A,P,M)==1) {
                leftSetAP.add(M);
            }
        }

        // Determine who's to the left of PB
        ArrayList<Point2D> leftSetPB = new ArrayList<Point2D>();
        for (int i = 0; i < set.size(); i++) {
            Point2D M = set.get(i);
            if (pointLocation(P,B,M)==1) {
                leftSetPB.add(M);
            }
        }
        hullSet(A,P,leftSetAP,hull);
        hullSet(P,B,leftSetPB,hull);

    }

    public int pointLocation(Point2D A, Point2D B, Point2D P) {
        double cp1 = (B.getX()-A.getX())*(P.getY()-A.getY()) - (B.getY()-A.getY())*(P.getX()-A.getX());
        return (cp1>0)?1:-1;
    }
}
