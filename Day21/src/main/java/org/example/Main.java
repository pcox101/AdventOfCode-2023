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

        Set<String> loopDetection = new HashSet<>();
        boolean discoveredLoop = false;
        long startLoop = 0;
        long loopLength = 0;
        Set<String> loopDetection2 = new HashSet<>();
        boolean discoveredLoop2 = false;
        long startLoop2 = 0;
        long loopLength2 = 0;
        Set<String> loopDetection3 = new HashSet<>();
        boolean discoveredLoop3 = false;
        long startLoop3 = 0;
        long loopLength3 = 0;

        int width = gameBoard[0].length;
        int height = gameBoard.length;
        for (int counter = 0; counter < 500; counter++)
        {
            if (counter % 100000 == 0)
                System.out.println(counter);

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

            if (!discoveredLoop) {
                String hash = generateHashForMap(gameBoard, positions, 0);
                // Check the main map for loops
                if (loopDetection.contains(hash)) {
                    if (startLoop == 0) {
                        startLoop = counter;
                        loopDetection.clear();
                        loopDetection.add(hash);
                    } else {
                        loopLength = counter - startLoop;
                        discoveredLoop = true;
                        System.out.printf("Found loop at %d of length %d (%s)%n", startLoop, loopLength, hash);
                    }
                } else {
                    loopDetection.add(hash);
                }
            }
            else if (!discoveredLoop2) {
                String hash = generateHashForMap(gameBoard, positions, gameBoard.length);
                if (loopDetection2.contains(hash)) {
                    if (startLoop2 == 0) {
                        startLoop2 = counter;
                        loopDetection2.clear();
                        loopDetection2.add(hash);
                    }
                    else
                    {
                        loopLength2 = counter - startLoop2;
                        discoveredLoop2 = true;
                        System.out.printf("Found loop 2 at %d of length %d (%s)%n",startLoop2, loopLength2, hash);
                    }
                }
                else {
                    loopDetection2.add(hash);
                }
            }
            else if (!discoveredLoop3) {
                String hash = generateHashForMap(gameBoard, positions, gameBoard.length * 2);
                if (loopDetection3.contains(hash)) {
                    if (startLoop3 == 0) {
                        startLoop3 = counter;
                        loopDetection3.clear();
                        loopDetection3.add(hash);
                    }
                    else
                    {
                        loopLength3 = counter - startLoop3;
                        discoveredLoop3 = true;
                        System.out.printf("Found loop 3 at %d of length %d (%s)%n",startLoop3, loopLength3, hash);
                    }
                }
                else {
                    loopDetection3.add(hash);
                }
            }
        }
        return positions.size();
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