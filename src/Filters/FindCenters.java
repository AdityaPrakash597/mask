package Filters;
import Interfaces.PixelFilter;
import core.DImage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class FindCenters implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        double minVariance = Double.MAX_VALUE;
        int optimalK = 0;
        l:
        for (int k = 2; k <=6; k++) {
            ArrayList<Point> points = this.initializePointList(grid);
            if (points.size() == 0) break;
            Cluster[] clusters = this.initializeClusters(points, k);
            boolean stable;
            do {
                for (Cluster cluster : clusters) cluster.clear();
                for (Point point : points) {
                    Cluster closest = point.returnClosestCluster(clusters);
                    closest.addPoint(point);
                }
                stable = this.recalculateClusterCenters(clusters);
            } while (!stable);
            this.stabilizeClusters(clusters, points, grid);
            if (adjacentClusters(clusters)) continue l;
            if (variance(clusters) < minVariance) {
                minVariance = variance(clusters);
                optimalK = k;
            }
        }

        if (optimalK == 0) return img;
        ArrayList<Point> points = this.initializePointList(grid);
        Cluster[] clusters = this.initializeClusters(points, optimalK);
        boolean stable;
        do {
            for (Cluster cluster : clusters) cluster.clear();
            for (Point point : points) {
                Cluster closest = point.returnClosestCluster(clusters);
                closest.addPoint(point);
            }
            stable = this.recalculateClusterCenters(clusters);
        } while (!stable);
        this.stabilizeClusters(clusters, points, grid);



        return this.setImage(red, green, blue, clusters, img);
    }

    private ArrayList<Point> initializePointList(short[][] grid) {
        ArrayList<Point> pointList = new ArrayList<Point>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 255) pointList.add(new Point(i, j));
            }
        } return pointList;
    }

    private Cluster[] initializeClusters(ArrayList<Point> points, int k) {
        Cluster[] clusters = new Cluster[k]; // create list of clusters
        // loop k times and run the constructor for each Cluster to generate a random center
        for (int i = 0; i < k; i++) clusters[i] = new Cluster(points);
        return clusters;
    }

    private boolean recalculateClusterCenters(Cluster[] clusters) {
        boolean stable = true;
        for (Cluster cluster : clusters) {
            boolean x = cluster.center();
            if (!x) stable = false;
        } return stable;
    }

    private DImage setImage(short[][] red, short[][] green, short[][] blue, Cluster[] clusters, DImage img) {
        for (Cluster cluster : clusters) {
            for (int i = cluster.getCenter().getX() - 5; i < cluster.getCenter().getX() + 5; i++) {
                for (int j = cluster.getCenter().getY() - 5; j < cluster.getCenter().getY() + 5; j++) {
                    if (i > 0 && i < img.getHeight() && j > 0 && j < img.getWidth()) {
                        red[i][j] = 255;
                        green[i][j] = blue[i][j] = 0;
                    }
                }
            }
        } img.setColorChannels(red, green, blue);
        return img;

    }

    private double variance(Cluster[] clusters) {
        double mean = 0;
        for (Cluster cluster : clusters) {
            mean += cluster.size();
        } mean /= clusters.length;
        double variance = 0;
        for (Cluster cluster : clusters) {
            variance += Math.pow((mean - cluster.size()), 2);
        } return variance / clusters.length;
    }


    private void stabilizeClusters(Cluster[] clusters, ArrayList<Point> points, short[][] grid) {
        for (Cluster cluster : clusters) {
            if (grid[cluster.getCenter().getX()][cluster.getCenter().getY()] == 255) continue;
            Point closest = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            for (Point point : points) {
                if (point.distance(cluster.getCenter()) < closest.distance(cluster.getCenter())) closest = point;
            } cluster.setCenter(closest);
        }
    }

    private boolean adjacentClusters(Cluster[] clusters) {
        for (int i = 0; i < clusters.length; i++) {
            for (int j = i+1; j < clusters.length; j++) {
                for (Point p1 : clusters[i].getPoints()) {
                    for (Point p2 : clusters[j].getPoints()) {
                        if (p1.distance(p2) <= Math.sqrt(2) && !p1.equals(p2)) return true;

                    }
                }
            }
        } return false;
    }
}

class Point {
    private int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Cluster returnClosestCluster(Cluster[] clusters) {
        double minDist = Double.MAX_VALUE;
        Cluster closest = null;
        for (Cluster cluster : clusters) {
            if (distance(cluster.getCenter()) < minDist) {
                minDist = distance(cluster.getCenter());
                closest = cluster;
            }
        } return closest;
    }

    public double distance(Point p) {
        return Math.sqrt(
                (this.x - p.getX()) * (this.x - p.getX())
                + (this.y - p.getY()) * (this.y - p.getY())
        );
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}

class Cluster {
    private ArrayList<Point> points = new ArrayList<Point>();
    private Point center;
    public Cluster(ArrayList<Point> points) {
        Point point = points.get((int) (Math.random() * points.size()));
        this.center = new Point(point.getX(), point.getY());
    }
    public void addPoint(Point point) {
        points.add(point);
    }

    public ArrayList<Point> getPoints() {
        return this.points;
    }

    public void clear() {
        this.points.clear();
    }

    public Point getCenter() { return this.center; }
    public void setCenter(Point point) { this.center = point; }

    public int size() {return this.points.size();}


    public boolean center() {
        if (points.size() == 0) return false;
        int x, y;
        x = y = 0;
        for (Point point : points) {
            x += point.getX();
            y += point.getY();
        }
        Point newCenter = new Point(x/points.size(), y/points.size());
        if (this.center.equals(newCenter)) return true;
        else this.center = newCenter;

        return false;
    }

}

