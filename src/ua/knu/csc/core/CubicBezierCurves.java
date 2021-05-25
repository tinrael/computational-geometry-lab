package ua.knu.csc.core;

import java.util.ArrayList;
import java.awt.geom.Point2D;

public class CubicBezierCurves {
    private final ArrayList<Point2D> points;

    private double[] aX = null;
    private double[] aY = null;

    private double[] bX = null;
    private double[] bY = null;

    public CubicBezierCurves(ArrayList<Point2D> points) {
        if (points == null) {
            throw new NullPointerException("The specified argument 'points' is null.");
        }
        this.points = points;
    }

    public void calculateCubicBezierCurvesCoefficients() {
        int n = points.size() - 1;

        double[][] cX = new double[n][n + 1];
        double[][] cY = new double[n][n + 1];

        for (int i = 1; i < (n - 1); i++) {
            cX[i][i - 1] = 1.0d;
            cX[i][i] = 4.0d;
            cX[i][i + 1] = 1.0d;
        }

        cX[0][0] = 2.0d;
        cX[0][1] = 1.0d;

        cX[n - 1][n - 2] = 2.0d;
        cX[n - 1][n - 1] = 7.0d;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cY[i][j] = cX[i][j];
            }
        }

        for (int i = 1; i < (n - 1); i++) {
            cX[i][n] = 2.0d * (2.0d * points.get(i).getX() + points.get(i + 1).getX());
            cY[i][n] = 2.0d * (2.0d * points.get(i).getY() + points.get(i + 1).getY());
        }

        cX[0][n] = points.get(0).getX() + 2.0d * points.get(1).getX();
        cX[n - 1][n] = 8.0d * points.get(n - 1).getX() + points.get(n).getX();

        cY[0][n] = points.get(0).getY() + 2.0d * points.get(1).getY();
        cY[n - 1][n] = 8.0d * points.get(n - 1).getY() + points.get(n).getY();

        double[] aX = new double[n];
        double[] aY = new double[n];

        Utils.gaussianElimination(cX, n, n + 1, aX);
        Utils.gaussianElimination(cY, n, n + 1, aY);

        double[] bX = new double[n];
        double[] bY = new double[n];

        for (int i = 0; i < (n - 1); i++) {
            bX[i] = 2.0d * points.get(i + 1).getX() - aX[i + 1];
            bY[i] = 2.0d * points.get(i + 1).getY() - aY[i + 1];
        }

        bX[n - 1] = (aX[n - 1] + points.get(n).getX()) / 2.0d;
        bY[n - 1] = (aY[n - 1] + points.get(n).getY()) / 2.0d;

        this.aX = aX;
        this.aY = aY;

        this.bX = bX;
        this.bY = bY;
    }

    public ArrayList<Point2D.Double> getCubicBezierCurvesPoints(int numberOfPointsToSliceEachCubicCurve) {
        if ((aX == null) || (aY == null) || (bX == null) || (bY == null)) {
            throw new IllegalStateException("The coefficients (a_{i}, b_{i}) did not compute. " +
                    "Call the 'calculateCubicBezierCurvesCoefficients' method to compute the coefficients.");
        }

        ArrayList<Point2D.Double> cubicBezierCurvesPoints = new ArrayList<>();

        double step = 1.0d / (numberOfPointsToSliceEachCubicCurve - 1);

        for (int i = 0; i < (points.size() - 1); i++) {
            double t;
            for (int j = 0; j < numberOfPointsToSliceEachCubicCurve; j++) {
                t = j * step;

                double x = calculateCubicBezierCurveValue(points.get(i).getX(), aX[i], bX[i], points.get(i + 1).getX(), t);
                double y = calculateCubicBezierCurveValue(points.get(i).getY(), aY[i], bY[i], points.get(i + 1).getY(), t);

                cubicBezierCurvesPoints.add(new Point2D.Double(x, y));
            }
        }

        return cubicBezierCurvesPoints;
    }

    public static double calculateCubicBezierCurveValue(double a, double b, double c, double d, double t) {
        return (Math.pow(1.0d - t, 3.0d) * a + 3.0d * Math.pow(1.0d - t, 2.0d) * t * b
                + 3.0d * (1.0d - t) * Math.pow(t, 2.0d) * c + Math.pow(t, 3.0d) * d);
    }
}
