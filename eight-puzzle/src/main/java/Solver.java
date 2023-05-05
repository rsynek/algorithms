import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {

    private final Board initialBoard;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.initialBoard = initial;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        MinPQ<SearchNode> originalQueue = initQueue(initialBoard);
        MinPQ<SearchNode> twinQueue = initQueue(initialBoard.twin());


        SearchNode originalCurrentStep, twinCurrentStep;
        boolean originalSolved, twinSolved;
        do {
            originalCurrentStep = solvingStep(originalQueue);
            twinCurrentStep = solvingStep(twinQueue);

            originalSolved = originalCurrentStep.getCurrentBoard().isGoal();
            twinSolved = twinCurrentStep.getCurrentBoard().isGoal();
        } while (!(originalSolved || twinSolved));

        return originalSolved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solve().getMoves() : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        SearchNode goal = solve();

        Stack<Board> solution = new Stack<>();
        SearchNode currentNode = goal;
        while (currentNode != null) {
            solution.push(currentNode.currentBoard);
            currentNode = currentNode.getPreviousSearchNode();
        }

        return solution;
    }

    private MinPQ<SearchNode> initQueue(Board initialBoard) {
        MinPQ<SearchNode> queue = new MinPQ<>(new SearchNodeComparator());
        queue.insert(new SearchNode(initialBoard));

        return queue;
    }

    private SearchNode solvingStep(MinPQ<SearchNode> queueOfMoves) {
        SearchNode currentSearchNode = queueOfMoves.delMin();

        for (Board nextBoard : currentSearchNode.getCurrentBoard().neighbors()) {
            SearchNode previousSearchNode = currentSearchNode.getPreviousSearchNode();
            if (previousSearchNode == null || !nextBoard.equals(previousSearchNode.getCurrentBoard())) {
                queueOfMoves.insert(new SearchNode(nextBoard, currentSearchNode));
            }
        }

        return currentSearchNode;
    }

    private SearchNode solve() {
        MinPQ<SearchNode> solvingQueue = initQueue(initialBoard);
        SearchNode currentSearchNode;
        do {
            currentSearchNode = solvingStep(solvingQueue);
        } while (!currentSearchNode.getCurrentBoard().isGoal());

        return currentSearchNode;
    }

    private final class SearchNode {
        private final Board currentBoard;
        private final SearchNode previousSearchNode;
        private final int moves;

        public SearchNode(Board initialBoard) {
            this.currentBoard = initialBoard;
            this.previousSearchNode = null;
            this.moves = 0;
        }

        public SearchNode(Board currentBoard, SearchNode previousSearchNode) {
            this.currentBoard = currentBoard;
            this.previousSearchNode = previousSearchNode;
            this.moves = previousSearchNode.getMoves() + 1;
        }

        public Board getCurrentBoard() {
            return currentBoard;
        }

        public SearchNode getPreviousSearchNode() {
            return previousSearchNode;
        }

        public int getMoves() {
            return moves;
        }

        public int getPriority() {
            return getCurrentBoard().manhattan() + moves;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder()
                    .append("priority: ").append(getPriority()).append(System.lineSeparator())
                    .append("moves: ").append(getMoves()).append(System.lineSeparator())
                    .append("manhattan: ").append(getCurrentBoard().manhattan()).append(System.lineSeparator())
                    .append(getCurrentBoard())
                    .append(System.lineSeparator());
            return sb.toString();
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode firstSearchNode, SearchNode secondSearchNode) {
            return firstSearchNode.getPriority() - secondSearchNode.getPriority();
        }
    }
}
