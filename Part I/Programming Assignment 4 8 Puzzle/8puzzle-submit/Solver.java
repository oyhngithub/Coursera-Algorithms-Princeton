/* *****************************************************************************
 *  Name:
 *  Date:03/07/2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode originBoard;
    private Board initial;

    public Solver(
            Board initial) {          // find a solution to the initial board (using the A* algorithm)
        this.initial = initial;
        MinPQ<SearchNode> originPQ = new MinPQ<>();
        if (initial == null) throw new java.lang.IllegalArgumentException();
        originBoard = new SearchNode(initial); // insert initial node
        originPQ.insert(originBoard);

        SearchNode twinBoard = new SearchNode(initial.twin()); // insert twin initial node
        originPQ.insert(twinBoard);

        while (!originBoard.board.isGoal()) {
            originBoard = originPQ.delMin();

            Iterable<Board> stack = originBoard.board.neighbors();
            for (Board i : stack) {
                if (originBoard.predecessor == null || !originBoard.predecessor.board.equals(i)) {
                    SearchNode tmp = new SearchNode(i);
                    tmp.predecessor = originBoard;
                    tmp.setSteps();
                    originPQ.insert(tmp);
                }
            }
            originBoard = originPQ.min();
        }

    }

    public boolean isSolvable() {           // is the initial board solvable?
        return initial != null && initial.equals(getRoot(originBoard));
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if (originBoard == null) return 0;
        if (!isSolvable()) return -1;
        return originBoard.steps;
    }

    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable()) return null;
        return getPath();
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
        //Board twin = initial.twin();

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int manhattanP;
        int hammingP;
        int steps = 0;
        SearchNode predecessor;

        SearchNode(Board board) {
            this.board = board;
            this.manhattanP = board.manhattan();
            this.hammingP = board.hamming();
        }

        private void setSteps() {
            steps = predecessor.steps + 1;
            manhattanP += steps;
            hammingP += steps;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.manhattanP != o.manhattanP) {
                return this.manhattanP - o.manhattanP;
            } else {
                return o.steps - this.steps;
            }
        }
    }

    private Iterable<Board> getPath() {
        Stack<Board> result = new Stack<>();
        result.push(originBoard.board);
        SearchNode ith = originBoard;
        while (ith.predecessor != null) {
            result.push(ith.predecessor.board);
            ith = ith.predecessor;
        }
        return result;
    }

    private Board getRoot(SearchNode board) {
        SearchNode tmp = board;
        while (tmp.predecessor != null) {
            tmp = tmp.predecessor;
        }
        return tmp.board;
    }
}