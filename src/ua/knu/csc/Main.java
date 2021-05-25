package ua.knu.csc;

import ua.knu.csc.core.CubicBezierCurves;
import ua.knu.csc.core.QuickHull;
import ua.knu.csc.ui.DrawingPanel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {

    public static void main(String[] args) {
        Point origin = new Point(50, 500);

        Set<Point> points = new HashSet<>();
        points.add(new Point(60, 110));
        points.add(new Point(150, 45));
        points.add(new Point(300, 210));
        points.add(new Point(40, 300));
        points.add(new Point(50, 50));
        points.add(new Point(85, 90));
        points.add(new Point(170, 210));
        points.add(new Point(150, 180));
        points.add(new Point(130, 290));
        points.add(new Point(10, 200));
        points.add(new Point(300, 175));
        points.add(new Point(170, 210));
        points.add(new Point(335, 75));

        ArrayList<Point> convexHullPoints = QuickHull.findConvexHull(points);

        // Duplicate the first point to the end of the list to draw the cubic Bezier curve from the last to the first point.
        convexHullPoints.add(convexHullPoints.get(0));

        System.out.print("[");
        int size = convexHullPoints.size();
        for (int i = 0; i < size; i++) {
            System.out.print("(" + convexHullPoints.get(i).x + ", " + convexHullPoints.get(i).y + ")");

            if (i != (size - 1)) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        CubicBezierCurves cubicBezierCurves = new CubicBezierCurves(convexHullPoints);
        cubicBezierCurves.calculateCubicBezierCurvesCoefficients();

        ArrayList<Point2D.Double> cubicBezierCurvesPointsInDoublePrecision = cubicBezierCurves.getCubicBezierCurvesPoints(5);
        ArrayList<Point> cubicBezierCurvesPointsInIntegerPrecision = new ArrayList<>(cubicBezierCurvesPointsInDoublePrecision.size());

        for (Point2D.Double pointInDoublePrecision : cubicBezierCurvesPointsInDoublePrecision) {
            Point pointInIntegerPrecision = new Point();
            pointInIntegerPrecision.setLocation(pointInDoublePrecision);

            cubicBezierCurvesPointsInIntegerPrecision.add(pointInIntegerPrecision);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(origin, points, convexHullPoints, cubicBezierCurvesPointsInIntegerPrecision);
            }
        });
    }

    private static void createAndShowGUI(Point origin, Set<Point> points, ArrayList<Point> convexHullPoints, ArrayList<Point> cubicBezierCurvesPoints) {
        JFrame mainWindow = new JFrame("lab");
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainWindow.add(new DrawingPanel(origin, points, convexHullPoints, cubicBezierCurvesPoints));

        mainWindow.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainWindow.setLocation(screenSize.width / 2 - mainWindow.getSize().width / 2, screenSize.height / 2 - mainWindow.getSize().height / 2);

        mainWindow.setVisible(true);
    }
}
