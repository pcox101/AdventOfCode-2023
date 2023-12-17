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

        private Vector(char[][] gameBoard, Vector otherVector, int rowOffset, int columnOffset, String direction) {
            Row = otherVector.Row + rowOffset;
            Column = otherVector.Column + columnOffset;
            HeatLoss = otherVector.HeatLoss + (long)(gameBoard[Row][Column] - '0');
            Last10Moves = otherVector.Last10Moves.substring(1,10) + direction;
            PreviousMoves = new HashSet<>(otherVector.PreviousMoves);
            PreviousMoves.add(String.format("%s,%s", Row, Column));
        }

        public String getVisitedKey()
        {
            String direction = Last10Moves.substring(9,10);
            return String.format("%d,%d_%d_%s", Row, Column, getMovesSinceLastTurn(), direction);
        }

        public int getMovesSinceLastTurn()
        {
            char previousMove = Last10Moves.charAt(9);
            for (int i = 8 ; i >= 0; i--)
            {
                if (Last10Moves.charAt(i) != previousMove)
                {
                    return 9 - i;
                }
            }
            return 10;
        }

        public int Row;
        public int Column;
        public long HeatLoss;
        public String Last10Moves;
        public Set<String> PreviousMoves;
    }

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

    private static long calculatePart1(char[][] gameBoard) {
        boolean foundSolution = false;
        Set<String> visitedPositions = new HashSet<>();
        PriorityQueue<Vector> frontier = new PriorityQueue<>((o1, o2) -> Long.compare(o1.HeatLoss, o2.HeatLoss));

        frontier.add(new Vector(0, 0, 0, "          ", new HashSet<>()));

        long result = 0;
        while (!foundSolution) {
            Vector thisMove = frontier.remove();

            // Is this the finishing position?
            if ((thisMove.Row == gameBoard.length - 1) && (thisMove.Column == gameBoard[0].length - 1)) {
                result = thisMove.HeatLoss;
                foundSolution = true;
            }

            int movesSinceLastTurn = thisMove.getMovesSinceLastTurn();
            char lastMoveDirection = thisMove.Last10Moves.charAt(9);

            // Can we move north?
            if ((thisMove.Row != 0)
                    && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'N'))
                    && (lastMoveDirection != 'S')) {
                addMove(gameBoard, visitedPositions, frontier, thisMove, -1, 0, "N");
            }
            // E
            if ((thisMove.Column != gameBoard[0].length - 1)
                    && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'E'))
                    && (lastMoveDirection != 'W')) {
                addMove(gameBoard, visitedPositions, frontier, thisMove, 0, 1, "E");
            }
            // S
            if ((thisMove.Row != gameBoard.length - 1)
                    && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'S'))
                    && (lastMoveDirection != 'N')) {
                addMove(gameBoard, visitedPositions, frontier, thisMove, 1, 0, "S");
            }
            // W
            if ((thisMove.Column != 0)
                    && !((movesSinceLastTurn >= 3) && (lastMoveDirection == 'W'))
                    && (lastMoveDirection != 'E')) {
                addMove(gameBoard, visitedPositions, frontier, thisMove, 0, -1, "W");
            }
        }
        return result;
    }

    private static void addMove(char[][] gameBoard,
                                Set<String> visitedPositions,
                                PriorityQueue<Vector> frontier,
                                Vector thisMove,
                                int rowOffset,
                                int columnOffset,
                                String direction) {
        Vector nextMove = new Vector(gameBoard, thisMove, rowOffset, columnOffset, direction);
        // Check we've not already visited it
        String visitedKey = nextMove.getVisitedKey();
        if (!visitedPositions.contains(visitedKey)) {
            visitedPositions.add(visitedKey);
            frontier.add(nextMove);
        }
    }

    private static long calculatePart2(char[][] gameBoard)
    {
        long result = 0;
        boolean foundSolution = false;
        Set<String> visitedPositions = new HashSet<>();
        PriorityQueue<Vector> frontier = new PriorityQueue<>((o1, o2) -> Long.compare(o1.HeatLoss, o2.HeatLoss));

        frontier.add(new Vector(0, 0, 0, "          ", new HashSet<>()));

        while (!foundSolution)
        {
            Vector thisMove = frontier.remove();

            if ((thisMove.Row == gameBoard.length - 1) && (thisMove.Column == gameBoard[0].length - 1)) {
                // Also check that it's a 4 character finish
                String finishingRun = thisMove.Last10Moves.substring(6,10);
                if (finishingRun.equals("SSSS") || finishingRun.equals("EEEE")) {
                    result = thisMove.HeatLoss;
                    foundSolution = true;
                }
                else {
                    continue;
                }
            }

            // We may have to move in one direction
            char lastMoveDirection = thisMove.Last10Moves.charAt(9);
            int movesSinceLastTurn = thisMove.getMovesSinceLastTurn();

            if (thisMove.getMovesSinceLastTurn() < 4)
            {
                if ((thisMove.Row != 0) && (lastMoveDirection == 'N'))
                {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, -1, 0, "N");
                }
                else if ((thisMove.Column != gameBoard[0].length - 1) && (lastMoveDirection == 'E'))
                {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 0, 1, "E");
                }
                else if ((thisMove.Row != gameBoard.length - 1) && (lastMoveDirection == 'S'))
                {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 1, 0, "S");
                }
                else if ((thisMove.Column != 0) && (lastMoveDirection == 'W'))
                {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 0, -1, "W");
                }
            }
            else // we are free to move
            {
                // Can we move north?
                if ((thisMove.Row != 0)
                        && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'N'))
                        && (lastMoveDirection != 'S')) {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, -1, 0, "N");
                }
                // E
                if ((thisMove.Column != gameBoard[0].length - 1)
                        && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'E'))
                        && (lastMoveDirection != 'W')) {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 0, 1, "E");
                }
                // S
                if ((thisMove.Row != gameBoard.length - 1)
                        && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'S'))
                        && (lastMoveDirection != 'N')) {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 1, 0, "S");
                }
                // W
                if ((thisMove.Column != 0)
                        && !((movesSinceLastTurn >= 10) && (lastMoveDirection == 'W'))
                        && (lastMoveDirection != 'E')) {
                    addMove(gameBoard, visitedPositions, frontier, thisMove, 0, -1, "W");
                }
            }
        }

        return result;
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