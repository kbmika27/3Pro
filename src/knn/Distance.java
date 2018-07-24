package knn;

import java.util.List;

import org.opencv.core.Point;

public interface Distance<T> {
    /**
     * Returns the distance measure between two objects.
     */
    public  double d(List<Point>x, List<Point> y);
}
