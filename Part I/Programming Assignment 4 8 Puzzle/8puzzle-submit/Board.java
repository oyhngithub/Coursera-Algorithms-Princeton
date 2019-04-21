/* *****************************************************************************
 *  Name:
 *  Date:07/03/2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

public class Board {
    private int zeroRow;
    private int zeroCol;
    private char[][] blocks;
    private int N;
    private int hammingDistance = 0;
    private int manhattanDistance = 0;

    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
        if (blocks == null) throw new java.lang.IllegalArgumentException();
        N = blocks.length;
        this.blocks = new char[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
                char val = (char) blocks[i][j];
                this.blocks[i][j] = val;
                if (val != i * N + j + 1 && val != 0) {
                    hammingDistance += 1;
                    int row = (val - 1) / N;
                    int col = val - row * N - 1;
                    manhattanDistance += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                // board dimension n
        return N;
    }

    public int hamming() {                  // number of blocks out of place
        return hammingDistance;
    }

    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        return manhattanDistance;
    }

    public boolean isGoal() {               // is this board the goal board?
        return hammingDistance == 0;
    }

    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int r1, r2;
        int c1, c2;
        if (zeroRow == 0) {
            r1 = zeroRow + 1;
            r2 = zeroRow + 1;
        }
        else {
            r1 = zeroRow - 1;
            r2 = zeroRow - 1;
        }
        c1 = zeroCol;
        c2 = c1 == 0 ? c1 + 1 : c1 - 1;
        int tmp = blocks[r1][c1];
        int[][] result = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                result[i][j] = blocks[i][j];
            }
        }
        result[r1][c1] = result[r2][c2];
        result[r2][c2] = tmp;
        return new Board(result);
    }

    public boolean equals(Object y) {       // does this board equal y?
        if (y == null || y.getClass() != this.getClass()) return false;
        if (((Board) y).N != this.N) return false;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (((Board) y).blocks[i][j] != blocks[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {    // all neighboring boards
        Stack<Board> stack = new Stack<>();
        if (zeroRow != 0) stack.push(new Board(exch(zeroRow - 1, zeroCol)));
        if (zeroRow != N - 1) stack.push(new Board(exch(zeroRow + 1, zeroCol)));
        if (zeroCol != 0) stack.push((new Board(exch(zeroRow, zeroCol - 1))));
        if (zeroCol != N - 1) stack.push(new Board(exch(zeroRow, zeroCol + 1)));
        if (stack.isEmpty()) return null;
        return stack;
    }

    private int[][] exch(int row, int col) {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                result[i][j] = blocks[i][j];
            }
        }
        result[zeroRow][zeroCol] = result[row][col];
        result[row][col] = 0;
        return result;
    }

    public String toString() {              // string representation of this board (in the output format specified below)
        StringBuilder sb = new StringBuilder();
        sb.append(N + "\n");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                sb.append(String.format("%2d  ", (int) blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)

    }
}
