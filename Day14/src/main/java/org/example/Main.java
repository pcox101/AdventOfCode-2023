package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day14/src/main/resources/input.txt"));

            String line = br.readLine();
            long outputPart1 = 0;
            long outputPart2 = 0;

            List<String> gameBoard = new ArrayList<>();
            while (line != null) {
                gameBoard.add(line);

                line = br.readLine();
            }

            char[][] gameBoardArray = getCharArrayGameBoard(gameBoard);
            tiltGameBoard(gameBoardArray, -1, 0);
            outputPart1 = calculateLoad(gameBoardArray);
            System.out.println(String.format("Output Part 1: %d", outputPart1));

            // Reset the gameboard and spin!
            gameBoardArray = getCharArrayGameBoard(gameBoard);
            outputPart2 = spinGameBoard(gameBoardArray);

            System.out.println(String.format("Output Part 2: %d", outputPart2));


        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long spinGameBoard(char[][] gameBoard)
    {
        Map<String, Long> seenBefore = new HashMap<>();

        long enteredLoop = 0;
        // N, W, S, E
        int[] rowOffsets = { -1, 0, 1, 0};
        int[] columnOffsets = { 0, -1, 0, 1};

        long numberOfMoves = 1000000000L * 4;
        for (long counter = 0; counter < numberOfMoves; counter++)
        {
            int rowOffset = rowOffsets[(int)(counter % 4)];
            int columnOffset = columnOffsets[(int)(counter % 4)];

            tiltGameBoard(gameBoard, rowOffset, columnOffset);

            // Have we seen this board before?
            String boardAsString = gameBoardToString(gameBoard, rowOffset, columnOffset);
            if (seenBefore.containsKey(boardAsString))
            {
                if (enteredLoop == 0)
                {
                    System.out.println(String.format("Entered loop at %d", counter));
                    enteredLoop = counter;
                    seenBefore.clear();
                }
                else
                {
                    // We now know how long the loop is
                    long lengthofLoop = counter - enteredLoop;
                    System.out.println(String.format("Loop length is %d", lengthofLoop));
                    while (counter + lengthofLoop < numberOfMoves)
                    {
                        counter += lengthofLoop;
                    }
                    // It shouldn't take too long to play to the end
                    // so clear the cache and just let it go
                    seenBefore.clear();
                }
            }
            seenBefore.put(boardAsString, counter);

            //if ((counter % 4) == 3) outputGameBoard(gameBoard);
        }

        // Finished spinning, calculate the load and return it
        return calculateLoad(gameBoard);
    }

    private static long calculateLoad(char[][] gameBoard)
    {
        long sum = 0;
        int scoreForTopRow = gameBoard.length;
        // For each column, work up the rows until we reach a non O
        for (int column = 0; column < gameBoard[0].length; column++)
        {
            for (int row = 0; row < gameBoard.length; row++) {
                if (gameBoard[row][column] == 'O') {
                    sum += (scoreForTopRow - row);
                }
            }
        }
        return sum;
    }

    private static void tiltGameBoard(char[][] gameBoard, int rowOffset, int columnOffset)
    {
        // Loop until everything has finished moving
        boolean anythingMoved = true;
        while (anythingMoved)
        {
            anythingMoved = false;
            for (int row = 0; row < gameBoard.length; row++)
            {
                if (row + rowOffset < 0) continue;
                if (row + rowOffset >= gameBoard.length) continue;
                for (int column = 0; column < gameBoard[0].length; column++)
                {
                    if (column + columnOffset < 0) continue;
                    if (column + columnOffset >= gameBoard[0].length) continue;

                    if ((gameBoard[row + rowOffset][column + columnOffset] == '.')
                       && (gameBoard[row][column] == 'O'))
                    {
                        anythingMoved = true;
                        gameBoard[row + rowOffset][column + columnOffset] = 'O';
                        gameBoard[row][column] = '.';
                    }
                }
            }
        }
        //outputGameBoard(gameBoard);
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

    private static void outputGameBoard(char[][] gameBoard) {
        System.out.println("Game Board:");
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[0].length; column++) {
                System.out.print(gameBoard[row][column]);
            }
            System.out.println();
        }
    }

    private static String gameBoardToString(char[][] gameBoard, int rowOffset, int columnOffset)
    {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[0].length; column++) {
                sb.append(gameBoard[row][column]);
            }
        }
        sb.append(rowOffset);
        sb.append(columnOffset);
        return sb.toString();
    }
}