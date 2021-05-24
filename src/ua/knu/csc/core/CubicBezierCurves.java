package ua.knu.csc.core;

public class CubicBezierCurves {
    public static double calculateCubicBezierCurveValue(double a, double b, double c, double d, double t) {
        return (Math.pow(1.0d - t, 3.0d) * a + 3.0d * Math.pow(1.0d - t, 2.0d) * t * b
                + 3.0d * (1.0d - t) * Math.pow(t, 2.0d) * c + Math.pow(t, 3.0d) * d);
    }
}
