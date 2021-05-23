package ua.knu.csc.core;

public class Utils {
    private static void forwardGaussianElimination(double[][] a, int rows, int columns) {
        for (int k = 0; k < rows; k++) {
            for (int j = k + 1; j < columns; j++) {
                a[k][j] = a[k][j] / a[k][k];
            }

            a[k][k] = 1.0;

            for (int i = k + 1; i < rows; i++) {
                for (int j = k + 1; j < columns; j++) {
                    a[i][j] = a[i][j] - a[i][k] * a[k][j];
                }

                a[i][k] = 0.0;
            }
        }
    }

    private static void backGaussianSubstitution(double[][] a, int rows, int columns, double[] x) {
        for (int i = rows - 1; i >= 0; i--) {
            x[i] = a[i][columns - 1];

            for (int j = i + 1; j < columns - 1; j++) {
                x[i] = x[i] - a[i][j] * x[j];
            }
        }
    }

    public static void gaussianElimination(double[][] a, int rows, int columns, double[] x) {
        forwardGaussianElimination(a, rows, columns);
        backGaussianSubstitution(a, rows, columns, x);
    }
}
