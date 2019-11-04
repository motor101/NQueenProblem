import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private int queensCount;
    private List<Set<Integer>> queensOnTheSameColumn;
    private List<Set<Integer>> queensOnTheSameNormalDiagonal;
    private List<Set<Integer>> queensOnTheSameReverseDiagonal;
    private int[] queenPositionByColumn;

    private Main(int queensCount) {
        this.queensCount = queensCount;
        queensOnTheSameColumn = new ArrayList<Set<Integer>>();

//        Normal diagonals
//            0 1 2 3
//            _______
//        0 | 3 4 5 6
//        1 | 2 3 4 5
//        2 | 1 2 3 4
//        3 | 0 1 2 3
//        The element with index (0,0) is on diagonal 3
//        The element with index (2,3) is on diagonal 4
        queensOnTheSameNormalDiagonal = new ArrayList<Set<Integer>>();

//        Reversed diagonals
////            0 1 2 3
////            _______
////        0 | 0 1 2 3
////        1 | 1 2 3 4
////        2 | 2 3 4 5
////        3 | 3 4 5 6
////        The element with index (0,0) is on diagonal 0
////        The element with index (2,3) is on diagonal 5
        queensOnTheSameReverseDiagonal = new ArrayList<Set<Integer>>();

        queenPositionByColumn = new int[queensCount];

        for (int queenIndex = 0; queenIndex < queensCount; ++queenIndex) {
            queensOnTheSameColumn.add(new HashSet<Integer>());
        }

        for (int i = 0; i < (2 * queensCount - 1); i++) {
            queensOnTheSameNormalDiagonal.add(new HashSet<Integer>());
            queensOnTheSameReverseDiagonal.add(new HashSet<Integer>());
        }

        // paste all queens on the main diagonal
        for (int queenIndex = 0; queenIndex < queensCount; queenIndex++) {
            queenPositionByColumn[queenIndex] = queenIndex;

            queensOnTheSameColumn.get(queenIndex).add(queenIndex);

            queensOnTheSameNormalDiagonal.get(queensCount - 1).add(queenIndex);
            queensOnTheSameReverseDiagonal.get(2 * queenIndex).add(queenIndex);
        }
    }

    // how many conflicts would the current queen have if it was on this column
    private int getConflictsCount(int row, int column) {
        int conflictsCount = 0;
        int normalDiagonalIndex = column - row + (queensCount - 1);
        int reverseDiagonalIndex = column + row;

        final int currentQueenIsHere = (column == queenPositionByColumn[row]) ? 1 : 0;

        int conflictingQueensCount = queensOnTheSameColumn.get(column).size();
        if (conflictingQueensCount > 0) {
            conflictsCount += conflictingQueensCount - currentQueenIsHere;
        }

        conflictingQueensCount = queensOnTheSameNormalDiagonal.get(normalDiagonalIndex).size();
        if (conflictingQueensCount > 0) {
            conflictsCount += conflictingQueensCount - currentQueenIsHere;
        }

        conflictingQueensCount = queensOnTheSameReverseDiagonal.get(reverseDiagonalIndex).size();
        if (conflictingQueensCount > 0) {
            conflictsCount += conflictingQueensCount - currentQueenIsHere;
        }

        return conflictsCount;
    }

    public static void main(String[] args) {
        int queensCount = Integer.parseInt(args[0]);

        Main board = new Main(queensCount);

        boolean conflictsFound;

        do {
            conflictsFound = false;

            for (int queenIndex = 0; queenIndex < queensCount; queenIndex++) {
                int minConflictsColumn;
                int minConflictsCount;
                final int currentQueenColumn = board.queenPositionByColumn[queenIndex];
                int conflictsCount = board.getConflictsCount(queenIndex, currentQueenColumn);

                // if this queen has no conflicts, check the next queen
                if (conflictsCount != 0) {
                    conflictsFound = true;
                    minConflictsCount = conflictsCount;
                    minConflictsColumn = currentQueenColumn;

                    for (int column = 0; column < queensCount; ++column) {
                        if (column != currentQueenColumn) {
                            conflictsCount = board.getConflictsCount(queenIndex, column);

                            if (conflictsCount < minConflictsCount) {
                                minConflictsCount = conflictsCount;
                                minConflictsColumn = column;
                            }
                            if (minConflictsCount == 0) {
                                break;
                            }
                        }
                    }

                    // swap current value with min value
                    if (minConflictsColumn != currentQueenColumn) {

                        // evaluate current diagonal indexes
                        int normalDiagonalIndex = currentQueenColumn - queenIndex + (queensCount - 1);
                        int reverseDiagonalIndex = currentQueenColumn + queenIndex;

                        board.queensOnTheSameColumn.get(currentQueenColumn).remove(queenIndex);
                        board.queensOnTheSameNormalDiagonal.get(normalDiagonalIndex).remove(queenIndex);
                        board.queensOnTheSameReverseDiagonal.get(reverseDiagonalIndex).remove(queenIndex);

                        // evaluate new diagonal indexes
                        normalDiagonalIndex = minConflictsColumn - queenIndex + (queensCount - 1);
                        reverseDiagonalIndex = minConflictsColumn + queenIndex;

                        board.queensOnTheSameColumn.get(minConflictsColumn).add(queenIndex);
                        board.queensOnTheSameNormalDiagonal.get(normalDiagonalIndex).add(queenIndex);
                        board.queensOnTheSameReverseDiagonal.get(reverseDiagonalIndex).add(queenIndex);

                        board.queenPositionByColumn[queenIndex] = minConflictsColumn;
                    }
                }
            }
        } while (conflictsFound);

        // write solution on standard output stream
        for (int i = 0; i < queensCount; ++i) {
            System.out.println(board.queenPositionByColumn[i]);
        }

    }
}
