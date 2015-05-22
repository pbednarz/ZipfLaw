import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Rodrigo
 */
public class GraphPanel extends JPanel {

    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private int[] scores;

    public GraphPanel(int[] scores) {
        this.scores = scores;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scores != null && scores.length > 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.length - 1);
            double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

            List<Point> graphPoints = new ArrayList<Point>();
            for (int i = 0; i < scores.length; i++) {
                int x1 = (int) (i * xScale + padding + labelPadding);
                int y1 = (int) ((getMaxScore() - scores[(i)]) * yScale + padding);
                graphPoints.add(new Point(x1, y1));
            }

            // draw white background
            g2.setColor(Color.WHITE);
            g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
            g2.setColor(Color.BLACK);

            // create hatch marks and grid lines for y axis.
            for (int i = 0; i < numberYDivisions + 1; i++) {
                int x0 = padding + labelPadding;
                int x1 = pointWidth + padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                int y1 = y0;
                if (scores.length > 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                    g2.setColor(Color.BLACK);
                    String yLabel = ((int) ((getMaxScore() * ((i * 1.0) / numberYDivisions)))) + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }

            // and for x axis
            for (int i = 0; i < scores.length; i++) {
                if (scores.length > 1) {
                    int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.length - 1) + padding + labelPadding;
                    int x1 = x0;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = y0 - pointWidth;
                    if ((i % ((scores.length / 20) + 1)) == 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                        g2.setColor(Color.BLACK);
                        String xLabel = i + "";
                        FontMetrics metrics = g2.getFontMetrics();
                        int labelWidth = metrics.stringWidth(xLabel);
                        g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    }
                    g2.drawLine(x0, y0, x1, y1);
                }
            }

            // create x and y axes
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

            Stroke oldStroke = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setStroke(oldStroke);
            g2.setColor(pointColor);
            for (int i = 0; i < graphPoints.size(); i++) {
                int x = graphPoints.get(i).x - pointWidth / 2;
                int y = graphPoints.get(i).y - pointWidth / 2;
                int ovalW = pointWidth;
                int ovalH = pointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        }
    }

    private int getMinScore() {
        int minScore = Integer.MAX_VALUE;
        for (int score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private int getMaxScore() {
        int maxScore = Integer.MIN_VALUE;
        for (int score : scores) {
            maxScore = Math.max(maxScore, score);
        }

        int mod = maxScore % 10;
        return maxScore + (10 - mod);
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }
}