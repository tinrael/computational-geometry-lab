package ua.knu.csc.ui;

import java.util.ArrayList;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private final Point origin;

    private final Iterable<Point> points;
    private final ArrayList<Point> convexHullPoints;

    public DrawingPanel(Point origin, Iterable<Point> points, ArrayList<Point> convexHullPoints) {
        if (origin == null) {
            throw new NullPointerException("The specified origin is null.");
        }

        if (points == null) {
            throw new NullPointerException("The specified 'points' argument is null.");
        }

        if (convexHullPoints == null) {
            throw new NullPointerException("The specified 'convexHullPoints' argument is null.");
        }

        this.origin = origin;
        this.points = points;
        this.convexHullPoints = convexHullPoints;

        setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void drawCoordinateAxes(Graphics2D graphics2D) {
        final int AXIS_LENGTH = 350;

        graphics2D.setColor(Color.GRAY);

        float[] dash = {3f, 3f};
        Stroke stroke = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 2.0f);
        graphics2D.setStroke(stroke);

        graphics2D.drawLine(origin.x, origin.y, origin.x + AXIS_LENGTH, origin.y);
        graphics2D.drawLine(origin.x, origin.y, origin.x, origin.y - AXIS_LENGTH);
    }

    private void drawOrigin(Graphics2D graphics2D) {
        graphics2D.setColor(Color.ORANGE);
        graphics2D.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        graphics2D.drawLine(origin.x, origin.y, origin.x, origin.y);
    }

    private void drawPoint(Graphics2D graphics2D, Point point, Color color, float width) {
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        int x = origin.x + point.x;
        int y = origin.y - point.y;

        graphics2D.drawLine(x, y, x, y);
    }

    private void drawLine(Graphics2D graphics2D, Point point1, Point point2, Color color, float width) {
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(width));

        int x1 = origin.x + point1.x;
        int y1 = origin.y - point1.y;

        int x2 = origin.x + point2.x;
        int y2 = origin.y - point2.y;

        graphics2D.drawLine(x1, y1, x2, y2);
    }

    private void drawString(Graphics graphics, Color color, String text, int x, int y) {
        graphics.setColor(color);
        graphics.drawString(text, origin.x + x, origin.y - y);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900,600);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCoordinateAxes((Graphics2D) g);
        drawOrigin((Graphics2D) g);

        int size = convexHullPoints.size();

        int i = 0;
        for (; i < (size - 1); i++) {
            drawLine((Graphics2D) g, convexHullPoints.get(i), convexHullPoints.get(i + 1), Color.BLUE, 2f);
        }

        if (size > 2) {
            drawLine((Graphics2D) g, convexHullPoints.get(i), convexHullPoints.get(0), Color.BLUE, 2f);
        }

        for (Point point : points) {
            drawPoint((Graphics2D) g, point, Color.RED, 7f);
            drawString(g, Color.BLACK, "(" + point.x + ", " + point.y + ")", point.x + 7, point.y);
        }
    }
}
