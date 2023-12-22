package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PseudoColumnUsage;
import java.util.*;

public class Main {

    static class Position
    {
        public Position(int row, int column)
        {
            Row = row;
            Column = column;
        }
        int Row;
        int Column;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Row == position.Row && Column == position.Column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Row, Column);
        }
    }
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day21/src/main/resources/input.txt"));

            long outputPart1;
            long outputPart2;

            List<String> gameBoard = new ArrayList<>();

            String line = br.readLine();
            while (line != null) {
                gameBoard.add(line);
                line = br.readLine();
            }

            // Get starting position
            char[][] gameBoardArray = getCharArrayGameBoard(gameBoard);
            int startingRow = 0, startingColumn = 0;

            for (int row = 0; row < gameBoardArray.length; row++)
            {
                for (int column = 0; column < gameBoardArray[row].length; column++)
                {
                    if (gameBoardArray[row][column] == 'S')
                    {
                        startingRow = row;
                        startingColumn = column;
                    }
                }
            }

            outputPart1 = calculatePart1(gameBoardArray, startingRow, startingColumn);
            outputPart2 = calculatePart2(gameBoardArray, startingRow, startingColumn);

            System.out.printf("Output Part 1: %d%n", outputPart1);
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculatePart1(char[][] gameBoard, int startingRow, int startingColumn)
    {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(startingRow, startingColumn));

        for (int counter = 0; counter < 64; counter++)
        {
            Set<Position> newPositions = new HashSet<>();
            for (Position position: positions)
            {
                // N
                if (position.Row != 0) {
                    Position newPosition = new Position(position.Row - 1, position.Column);
                    if (gameBoard[newPosition.Row][newPosition.Column] != '#') {
                        newPositions.add(newPosition);
                    }
                }
                // E
                if (position.Column != gameBoard[0].length - 1) {
                    Position newPosition = new Position(position.Row, position.Column + 1);
                    if (gameBoard[newPosition.Row][newPosition.Column] != '#') {
                        newPositions.add(newPosition);
                    }
                }
                // S
                if (position.Row != gameBoard.length - 1) {
                    Position newPosition = new Position(position.Row + 1, position.Column);
                    if (gameBoard[newPosition.Row][newPosition.Column] != '#') {
                        newPositions.add(newPosition);
                    }
                }
                // W
                if (position.Column != 0) {
                    Position newPosition = new Position(position.Row, position.Column - 1);
                    if (gameBoard[newPosition.Row][newPosition.Column] != '#') {
                        newPositions.add(newPosition);
                    }
                }
            }
            positions = newPositions;
            //outputGameBoard(gameBoard, positions);
        }
        return positions.size();
    }

    private static long calculatePart2(char[][] gameBoard, int startingRow, int startingColumn)
    {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(startingRow, startingColumn));

        int width = gameBoard[0].length;
        int height = gameBoard.length;

        int[] fofn = new int[3];

        for (int counter = 0; counter < 500; counter++)
        {
            Set<Position> newPositions = new HashSet<>();
            for (Position position: positions)
            {
                // N
                Position newPosition = new Position(position.Row - 1, position.Column);
                if (gameBoard[((newPosition.Row % height) + height) % height][((newPosition.Column % width) + width) % width] != '#') {
                    newPositions.add(newPosition);
                }
                // E
                newPosition = new Position(position.Row, position.Column + 1);
                if (gameBoard[((newPosition.Row % height) + height) % height][((newPosition.Column % width) + width) % width] != '#') {
                    newPositions.add(newPosition);
                }
                // S
                newPosition = new Position(position.Row + 1, position.Column);
                if (gameBoard[((newPosition.Row % height) + height) % height][((newPosition.Column % width) + width) % width] != '#') {
                    newPositions.add(newPosition);
                }
                // W
                newPosition = new Position(position.Row, position.Column - 1);
                if (gameBoard[((newPosition.Row % height) + height) % height][((newPosition.Column % width) + width) % width] != '#') {
                    newPositions.add(newPosition);
                }
            }

            positions = newPositions;
            //outputGameBoard(gameBoard, positions);

            // Pick some numbers
            // We need 26501365 steps. Which is 65 + (131 * 202300)
            // 65 + 0 * 131 = 65
            // 65 + 1 * 131 = 196
            // 65 + 2 * 131 = 327
            if ((counter + 1) == 65) // n = 0
            {
                System.out.printf("Value at %d (n=%d) is %d%n", counter + 1, 0, positions.size());
                fofn[0] = positions.size();
            }
            if ((counter + 1) == 196) // n = 1
            {
                System.out.printf("Value at %d (n=%d) is %d%n", counter + 1, 1, positions.size());
                fofn[1] = positions.size();
            }
            if ((counter + 1) == 327) // n = 2
            {
                System.out.printf("Value at %d (n=%d) is %d%n", counter + 1, 2, positions.size());
                fofn[2] = positions.size();
            }
            if ((counter + 1) == 458) // n = 3 (for verification only)
                System.out.printf("Value at %d (n=%d) is %d%n", counter + 1, 3, positions.size());

        }

        // Use lagrange (2nd order) to calculate the polynomial
        // https://mathworld.wolfram.com/LagrangeInterpolatingPolynomial.html
        // We want to solve for x = 202300
        long sum = 0;
        long x = 202300;
        // n = 3...
        for (int j = 0; j < 3; j++)
        {
            sum += getInner(fofn, x, j);
        }

        return sum;
    }

    private static long getInner(int[] fofn, long x, int j)
    {
        long dividend = 1;
        long divisor = 1;

        for (long k = 0; k < 3; k++)
        {
            if (k == (long)j)
                continue;
            dividend *= (x - k);
            divisor *= ((long)j - k);
        }

        return fofn[j] * (dividend / divisor);
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

    private static String generateHashForMap(char[][] gameBoard,
                                             Set<Position> positions,
                                             int positionRowOffset)
    {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < gameBoard.length; row++)
        {
            for (int column = 0; column < gameBoard[0].length; column++) {

                if (positions.contains(new Position(row + positionRowOffset, column)))
                    sb.append('O');
                else
                    sb.append(gameBoard[row][column]);
            }
        }
        return sb.toString();
    }

    private static void outputGameBoard(char[][] gameBoard, Set<Position> positions)
    {
        for (int row = 0; row < gameBoard.length; row++)
        {
            for (int column = 0; column < gameBoard[0].length; column++) {

                if (positions.contains(new Position(row, column)))
                    System.out.print('O');
                else
                    System.out.print(gameBoard[row][column]);
            }
            System.out.println();
        }
    }
}