public class BruteCollinearPoints {
    private int num = 0;
    private LineSegment[] result;
    private OwnSegment[] repeat;

    private class OwnSegment {
        Point max;
        Point min;

        OwnSegment(Point inMin, Point inMax) {
            max = inMax;
            min = inMin;
        }
    }

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) throw new java.lang.IllegalArgumentException();
        int N = points.length;
        result = new LineSegment[N * 100];
        repeat = new OwnSegment[N * 100];
        for (int i = 0; i < N; ++i) {
            if (points[i] == null) throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();
                for (int k = j + 1; k < N; ++k) {
                    for (int l = k + 1; l < N; ++l) {
                        if (l != i && l != j && l != k &&
                                points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                                points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])
                        ) {
                            Point min = points[i];
                            Point max = points[i];
                            min = min.compareTo(points[j]) < 0 ? min : points[j];
                            max = max.compareTo(points[j]) > 0 ? max : points[j];
                            min = min.compareTo(points[k]) < 0 ? min : points[k];
                            max = max.compareTo(points[k]) > 0 ? max : points[k];
                            min = min.compareTo(points[l]) < 0 ? min : points[l];
                            max = max.compareTo(points[l]) > 0 ? max : points[l];
                            boolean flag = true;
                            for (int n = 0; n < num; ++n) {
                                if (repeat[n].min.compareTo(min) == 0 && repeat[n].max.compareTo(max) == 0)
                                    flag = false;
                            }
                            if (flag) {
                                result[num] = new LineSegment(min, max);
                                repeat[num++] = new OwnSegment(min, max);
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {                 // the number of line segments
        return num;
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] tmp = new LineSegment[num];
        for (int i = 0; i < num; ++i) {
            tmp[i] = result[i];
        }
        return tmp;
    }
}