package ua.knu.csc.core;

import java.util.ArrayList;
import java.awt.Point;

public class CubicBezierCurves {
    private final ArrayList<Point> points;

    public CubicBezierCurves(ArrayList<Point> points) {
        if (points == null) {
            throw new NullPointerException("The specified argument 'points' is null.");
        }
        this.points = points;
    }

    public static double calculateCubicBezierCurveValue(double a, double b, double c, double d, double t) {
        return (Math.pow(1.0d - t, 3.0d) * a + 3.0d * Math.pow(1.0d - t, 2.0d) * t * b
                + 3.0d * (1.0d - t) * Math.pow(t, 2.0d) * c + Math.pow(t, 3.0d) * d);
    }
}
