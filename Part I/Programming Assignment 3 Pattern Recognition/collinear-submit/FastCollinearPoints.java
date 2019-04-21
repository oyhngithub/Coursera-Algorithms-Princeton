import java.util.Arrays;

public class FastCollinearPoints {
    private int N;
    private LineSegment[] result;
    private int num = 0;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new java.lang.IllegalArgumentException();
        N = points.length;
        result = new LineSegment[N * 100];
        Point[] aux = new Point[N];
        for (int i = 0; i < N; ++i) {
            if (points[i] == null) throw new java.lang.IllegalArgumentException();
            aux[i] = points[i];
        }
        if (N < 2) return;
        for (int i = 0; i < N; ++i) {
            Arrays.sort(aux, points[i].slopeOrder());
            //find all the nums that repeated more than 4 times
            Point max = points[i];
            Point min = points[i];
            Point last = aux[1];
            int length = 1;
            for (int j = 1; j < N; ++j) {
                if (points[i].compareTo(aux[j]) == 0) throw new java.lang.IllegalArgumentException();
                if (points[i].slopeTo(aux[j]) == points[i].slopeTo(last)) {
                    ++length;
                    max = max.compareTo(aux[j]) > 0 ? max : aux[j];
                    min = min.compareTo(aux[j]) < 0 ? min : aux[j];
                } else {
                    if (length >= 4 && min.compareTo(points[i]) == 0) {//only record from the smallest point(left-bottom)
                        result[num++] = new LineSegment(min, max);
                    }
                    length = 2;
                    last = aux[j];
                    max = points[i].compareTo(aux[j]) > 0 ? points[i] : aux[j];
                    min = points[i].compareTo(aux[j]) < 0 ? points[i] : aux[j];
                }
            }
            if (length >= 4 && points[i].compareTo(min) == 0) {//deal with end
                result[num++] = new LineSegment(min, max);
            }

        }
    }

    public int numberOfSegments() {                 // the number of lIllegalArgumentExceptionine segments
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
