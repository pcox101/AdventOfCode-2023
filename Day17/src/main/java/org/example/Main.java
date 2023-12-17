package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    private static class Vector
    {
        private Vector(int row, int column, long heatLoss, String last10Moves, Set<String> previousMoves)
        {
            Row = row;
            Column = column;
            HeatLoss = heatLoss;
            Last10Moves = last10Moves;
            PreviousMoves = previousMoves;
        }

        public int Row;
        public int Column;
        public long HeatLoss;
        public String Last10Moves;
        public Set<String> PreviousMoves;
    }

    private static int MAX_SET_SIZE = 10000;

    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("Day17/src/main/resources/input.txt"));

            String line = br.readLine();
            long outputPart1 = 0;
            long outputPart2 = 0;

            List<String> gameBoard = new ArrayList<>();
            while (line != null) {
                gameBoard.add(line);

                line = br.readLine();
            }

            char[][] gameBoardArray = getCharArrayGameBoard(gameBoard);

            outputPart1 = calculatePart1(gameBoardArray);
            System.out.println(String.format("Output Part 1: %d", outputPart1));


            outputPart2 = calculatePart2(gameBoardArray);
            System.out.println(String.format("Output Part 2: %d", outputPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculatePart1(char[][] gameBoard)
    {
        long lowestFinalScore = Long.MAX_VALUE;

        Map<String, Long> visitedPositions = new HashMap<>();

        List<Vector> nextMoves = new ArrayList<>();

        nextMoves.add(new Vector(0, 0, 0, "          ", new HashSet<>()));

        while (nextMoves.size() > 0)
        {
            List<Vector> newMoves = new ArrayList<>();

            for (Vector thisMove: nextMoves) {

                int movesSinceLastTurn = getMovesSinceLastTurn(thisMove.Last10Moves);
                char lastMoveDirection = thisMove.Last10Moves.charAt(9);

                // Can we move north?
                if ((thisMove.Row != 0)
                        && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'N'))
                        && (lastMoveDirection != 'S')) {
                    lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, -1, 0, "N", lowestFinalScore, false);
                }
                // E
                if ((thisMove.Column != gameBoard[0].length - 1)
                        && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'E'))
                        && (lastMoveDirection != 'W')) {
                    lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, 1, "E", lowestFinalScore, false);
                }
                // S
                if ((thisMove.Row != gameBoard.length - 1)
                        && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'S'))
                        && (lastMoveDirection != 'N')) {
                    lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 1, 0, "S", lowestFinalScore, false);
                }
                // W
                if ((thisMove.Column != 0)
                        && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'W'))
                        && (lastMoveDirection != 'E')) {
                    lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, -1, "W", lowestFinalScore, false);
                }
            }

            // Trim the set to the best options
            nextMoves = trimSet(lowestFinalScore, newMoves);
            System.out.println(String.format("Looping with %d positions to try.", nextMoves.size()));
        }

        return lowestFinalScore;
    }

    private static List<Vector> trimSet(long lowestFinalScore, List<Vector> newMoves) {
        List<Vector> nextMoves;
        newMoves.sort(Comparator.comparingLong(s -> s.HeatLoss));
        int lastPossible = newMoves.size();
        for (int i = 0; i < newMoves.size(); i++)
        {
            if (newMoves.get(i).HeatLoss > lowestFinalScore)
            {
                lastPossible = i;
                break;
            }
        }

        nextMoves = newMoves.subList(0, Math.min(lastPossible, MAX_SET_SIZE));
        return nextMoves;
    }

    private static long calculatePart2(char[][] gameBoard)
    {
        long lowestFinalScore = Long.MAX_VALUE;

        List<Vector> nextMoves = new ArrayList<>();
        Map<String, Long> visitedPositions = new HashMap<>();

        nextMoves.add(new Vector(0,0, 0, "          ", new HashSet<>()));

        while (nextMoves.size() != 0)
        {
            List<Vector> newMoves = new ArrayList<>();

            for (Vector thisMove: nextMoves) {
                int movesSinceLastTurn = getMovesSinceLastTurn(thisMove.Last10Moves);
                char lastMoveDirection = thisMove.Last10Moves.charAt(9);

                // We may have to move in one direction
                if (movesSinceLastTurn < 4) {
                    if ((thisMove.Row != 0) && (lastMoveDirection == 'N'))
                    {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, -1, 0, "N", lowestFinalScore, true);
                    }
                    else if ((thisMove.Column != gameBoard[0].length - 1) && (lastMoveDirection == 'E'))
                    {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, 1, "E", lowestFinalScore, true);
                    }
                    else if ((thisMove.Row != gameBoard.length - 1) && (lastMoveDirection == 'S'))
                    {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 1, 0, "S", lowestFinalScore, true);
                    }
                    else if ((thisMove.Column != 0) && (lastMoveDirection == 'W'))
                    {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, -1, "W", lowestFinalScore, true);
                    }
                }
                else // we are free to move
                {
                    // Can we move north?
                    if ((thisMove.Row != 0)
                            && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'N'))
                            && (lastMoveDirection != 'S')) {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, -1, 0, "N", lowestFinalScore, true);
                    }
                    // E
                    if ((thisMove.Column != gameBoard[0].length - 1)
                            && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'E'))
                            && (lastMoveDirection != 'W')) {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, 1, "E", lowestFinalScore, true);
                    }
                    // S
                    if ((thisMove.Row != gameBoard.length - 1)
                            && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'S'))
                            && (lastMoveDirection != 'N')) {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 1, 0, "S", lowestFinalScore, true);
                    }
                    // W
                    if ((thisMove.Column != 0)
                            && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'W'))
                            && (lastMoveDirection != 'E')) {
                        lowestFinalScore = updatePositions(gameBoard, thisMove, newMoves, visitedPositions, 0, -1, "W", lowestFinalScore, true);
                    }
                }
            }

            // Trim the set to the best options
            nextMoves = trimSet(lowestFinalScore, newMoves);
            System.out.println(String.format("Looping with %d positions to try.", nextMoves.size()));
        }

        return lowestFinalScore;
    }

    public static int getMovesSinceLastTurn(String last10Moves)
    {
        char previousMove = last10Moves.charAt(9);
        for (int i = 8 ; i >= 0; i--)
        {
            if (last10Moves.charAt(i) != previousMove)
            {
                return 9 - i;
            }
        }
        return 10;
    }

    private static long updatePositions(char[][] gameBoard,
                                        Vector thisMove,
                                        List<Vector> newMoves,
                                        Map<String, Long> visitedPositions,
                                        int rowOffset,
                                        int columnOffset,
                                        String direction,
                                        long lowestFinalScore,
                                        boolean part2)
    {
        int newRow = thisMove.Row + rowOffset;
        int newColumn = thisMove.Column + columnOffset;
        long newHeatLoss = thisMove.HeatLoss + (long)(gameBoard[newRow][newColumn] - '0');
        String newLast10Moves = thisMove.Last10Moves.substring(1,10) + direction;

        // Is this the final position?
        if ((newRow == gameBoard.length - 1) && (newColumn == gameBoard[0].length - 1)) {
            if (part2)
            {
                String finishing = newLast10Moves.substring(6,10);
                if (finishing.equals("SSSS") || finishing.equals("EEEE")) {
                    //outputGameBoardWithHistory(gameBoard, thisMove.PreviousMoves);
                    return Math.min(newHeatLoss, lowestFinalScore);
                }
            }
            else
            {
                //outputGameBoardWithHistory(gameBoard, thisMove.PreviousMoves);
                return Math.min(newHeatLoss, lowestFinalScore);
            }
        }

        // No point visiting somewhere we've already visited on this journey
        String newHistoryPosition = String.format("%s,%s", newRow, newColumn);
        if (!thisMove.PreviousMoves.contains(newHistoryPosition)) {

            Set<String> newHistory = new HashSet<>(thisMove.PreviousMoves);
            newHistory.add(newHistoryPosition);
            Vector nextMove = new Vector(newRow, newColumn, newHeatLoss, newLast10Moves, newHistory);

            String positionVector = String.format("%d,%d_%d_%s", newRow, newColumn, getMovesSinceLastTurn(newLast10Moves), direction);
            if (visitedPositions.containsKey(positionVector)) {
                long previousBestAtThisPosition = visitedPositions.get(positionVector);
                if (newHeatLoss < previousBestAtThisPosition) {
                    // Better than the last time we were here, replace it and carry on
                    visitedPositions.put(positionVector, newHeatLoss);
                    newMoves.add(nextMove);
                }
            } else {
                visitedPositions.put(positionVector, newHeatLoss);
                newMoves.add(nextMove);
            }
        }

        return lowestFinalScore;
    }

    private static void outputGameBoardWithHistory(char[][] gameBoard, Set<String> history)
    {
        System.out.println("Game Board:");
        for (int row = 0; row < gameBoard.length; row++)
        {
            for (int column = 0; column < gameBoard[0].length; column++) {
                if (history.contains(String.format("%d,%d", row, column)))
                {
                    System.out.print("*");
                }
                else
                {
                    System.out.print(gameBoard[row][column]);
                }
            }
            System.out.println();
        }
    }

    private static char[][] getCharArrayGameBoard(List<String> gameBoard)
    {
        char[][] output = new char[gameBoard.size()][gameBoard.get(0).length()];
        for (int row = 0; row < gameBoard.size(); row++)
        {
            for (int column = 0; column < gameBoard.get(0).length(); column++) {
                output[row][column] = gameBoard.get(row).charAt(column);
            }
        }
        return output;
    }
}