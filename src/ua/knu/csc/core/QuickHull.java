package ua.knu.csc.core;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import java.awt.Point;

public class QuickHull {
    public enum PointAndLineLocationType {
        LEFT,
        RIGHT,
        BEYOND,
        BEHIND,
        BETWEEN,
        ORIGIN,
        DESTINATION
    }

    // Returns the leftmost point (minimum x-coordinate).
    public static Point getLeftmostPoint(Iterable<Point> points) {
        if (points == null) {
            throw new NullPointerException("The specified 'points' argument is null.");
        }

        Point leftmostPoint = null;

        Iterator<Point> iterator = points.iterator();

        if (iterator.hasNext()) {
            leftmostPoint = iterator.next();

            while (iterator.hasNext()) {
                Point currentPoint = iterator.next();

                if (currentPoint.x < leftmostPoint.x) {
                    leftmostPoint = currentPoint;
                }
            }
        }

        return leftmostPoint;
    }

    // Returns the rightmost point (maximum x-coordinate).
    public static Point getRightmostPoint(Iterable<Point> points) {
        if (points == null) {
            throw new NullPointerException("The specified 'points' argument is null.");
        }

        Point rightmostPoint = null;

        Iterator<Point> iterator = points.iterator();

        if (iterator.hasNext()) {
            rightmostPoint = iterator.next();

            while (iterator.hasNext()) {
                Point currentPoint = iterator.next();

                if (currentPoint.x > rightmostPoint.x) {
                    rightmostPoint = currentPoint;
                }
            }
        }

        return rightmostPoint;
    }

    public static double calculateVectorLength(Point p) {
        return Math.sqrt(p.x * p.x + p.y * p.y);
    }

    // Determines the location of the point 'p2' concerning the oriented from 'p0' to 'p1' straight line segment.
    public static PointAndLineLocationType classify(Point p0, Point p1, Point p2) {
        Point a = new Point(p1.x - p0.x, p1.y - p0.y); // p1 - p0 (vector subtraction)
        Point b = new Point(p2.x - p0.x, p2.y - p0.y); // p2 - p0 (vector subtraction)

        int sign = a.x * b.y - b.x * a.y;

        if (sign > 0) {
            return PointAndLineLocationType.LEFT;
        }

        if (sign < 0) {
            return PointAndLineLocationType.RIGHT;
        }

        if ((a.x * b.x < 0) || (a.y * b.y < 0)) { // if vectors 'a' and 'b' are point in the opposite directions
            return PointAndLineLocationType.BEHIND;
        }

        if (calculateVectorLength(a) < calculateVectorLength(b)) {
            return PointAndLineLocationType.BEYOND;
        }

        if ((p0.x == p2.x) && (p0.y == p2.y)) { // p0 == p2
            return PointAndLineLocationType.ORIGIN;
        }

        if ((p1.x == p2.x) && (p1.y == p2.y)) { // p1 == p2
            return PointAndLineLocationType.DESTINATION;
        }

        return PointAndLineLocationType.BETWEEN;
    }

    // Calculates unsigned (>= 0) twice the area of a triangle.
    public static int calculateTwiceAreaOfTriangle(Point p1, Point p2, Point p3) {
        return Math.abs((p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y));
    }

    // From the given 'points', finds the farthest point from the line segment PQ.
    public static Point findFarthestPointFromLineSegment(Iterable<Point> points, Point p, Point q) {
        if (points == null) {
            throw new NullPointerException("The specified 'points' argument is null.");
        }

        Point farthestPoint = null;

        Iterator<Point> iterator = points.iterator();

        if (iterator.hasNext()) {
            farthestPoint = iterator.next();

            int twiceAreaOfTriangle = calculateTwiceAreaOfTriangle(p, q, farthestPoint);

            while (iterator.hasNext()) {
                Point currentPoint = iterator.next();

                int currentTwiceAreaOfTriangle = calculateTwiceAreaOfTriangle(p, q, currentPoint);

                if (currentTwiceAreaOfTriangle > twiceAreaOfTriangle) {
                    farthestPoint = currentPoint;
                    twiceAreaOfTriangle = currentTwiceAreaOfTriangle;
                }
            }
        }

        return farthestPoint;
    }

    // TODO: fix the problem. If all the given points lie on a vertical line, the function returns an incorrect convex hull.
    // In the returned resulted ArrayList (convexHullPoints) the points are sorted in clockwise order.
    public static ArrayList<Point> findConvexHull(Iterable<Point> points) {
        if (points == null) {
            throw new NullPointerException("The specified 'points' argument is null.");
        }

        if (!points.iterator().hasNext()) {
            return new ArrayList<>();
        }

        ArrayList<Point> convexHullPoints = new ArrayList<>();

        Point leftmostPoint = getLeftmostPoint(points);
        Point rightmostPoint = getRightmostPoint(points);

        convexHullPoints.add(leftmostPoint);
        convexHullPoints.add(rightmostPoint);

        Set<Point> s1 = new HashSet<>();
        Set<Point> s2 = new HashSet<>();

        for (Point point : points) {
            switch (classify(leftmostPoint, rightmostPoint, point)) {
                case LEFT:
                    s1.add(point);
                    break;
                case RIGHT:
                    s2.add(point);
                    break;
                default:
            }
        }

        findConvexHull(convexHullPoints, s1, leftmostPoint, rightmostPoint);
        findConvexHull(convexHullPoints, s2, rightmostPoint, leftmostPoint);

        return convexHullPoints;
    }

    /* From the points 's1' (these points must be on the left side of the oriented line from 'leftmostPoint' to 'rightmostPoint')
     * finds points on convex hull.
     */
    private static void findConvexHull(ArrayList<Point> convexHullPoints, Iterable<Point> s1, Point leftmostPoint, Point rightmostPoint) {
        if (!s1.iterator().hasNext()) {
            return;
        }

        Point farthestPoint = findFarthestPointFromLineSegment(s1, leftmostPoint, rightmostPoint);

        convexHullPoints.add(convexHullPoints.indexOf(rightmostPoint), farthestPoint);

        Set<Point> s11 = new HashSet<>();
        Set<Point> s12 = new HashSet<>();

        for (Point p : s1) {
            PointAndLineLocationType pointAndLineLocationType = classify(leftmostPoint, farthestPoint, p);

            if (pointAndLineLocationType == PointAndLineLocationType.LEFT) {
                s11.add(p);
            } else if (pointAndLineLocationType == PointAndLineLocationType.RIGHT) {
                if (classify(farthestPoint, rightmostPoint, p) == PointAndLineLocationType.LEFT) {
                    s12.add(p);
                }
            }
        }

        findConvexHull(convexHullPoints, s11, leftmostPoint, farthestPoint);
        findConvexHull(convexHullPoints, s12, farthestPoint, rightmostPoint);
    }
}
