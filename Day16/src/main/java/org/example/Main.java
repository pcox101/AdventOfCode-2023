package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    private static class Vector
    {
        public Vector(int row, int column, int xOffset, int yOffset)
        {
            Row = row;
            Column = column;
            YOffset = yOffset;
            XOffset = xOffset;
        }

        public String toString()
        {
            return String.format("%d,%d,%d,%d", Row, Column, XOffset, YOffset);
        }
        public int Row;
        public int Column;
        public int XOffset;
        public int YOffset;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day16/src/main/resources/input.txt"));

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

    private static long calculatePart1(char[][] gameBoard){

        Vector initialVector = new Vector(0, -1, 1, 0);
        return getScoreForInitialVector(gameBoard, initialVector);
    }

    private static long calculatePart2(char[][] gameBoard) {
        long highestValue = 0;

        // 4 edges, 4 directions
        for (int i = 0; i < gameBoard.length; i++) {
            Vector initialVectorRight = new Vector(i, -1, 1, 0);
            Vector initialVectorLeft = new Vector(i, gameBoard[0].length, -1, 0);
            highestValue = Math.max(highestValue, getScoreForInitialVector(gameBoard, initialVectorRight));
            highestValue = Math.max(highestValue, getScoreForInitialVector(gameBoard, initialVectorLeft));
        }

        for (int i = 0; i < gameBoard[0].length; i++) {
            Vector initialVectorDown = new Vector(-1, i, 0, 1);
            Vector initialVectorUp = new Vector(gameBoard[0].length, i, 0, -1);
            highestValue = Math.max(highestValue, getScoreForInitialVector(gameBoard, initialVectorDown));
            highestValue = Math.max(highestValue, getScoreForInitialVector(gameBoard, initialVectorUp));

        }

        return highestValue;
    }

    private static int getScoreForInitialVector(char[][] gameBoard, Vector initialVector) {
        List<Vector> lightPositions = new ArrayList<>();
        lightPositions.add(initialVector);

        Set<String> visitedPositions = new HashSet<>();
        Set<String> visitedVectors = new HashSet<>();

        // Play through the list until we have no lights moving
        while (lightPositions.size() > 0)
        {
            List<Vector> newPositions = new ArrayList<>();
            for (Vector photon: lightPositions)
            {
                visitedPositions.add(String.format("%d,%d",photon.Row, photon.Column));
                // Have we been here already?
                if (visitedVectors.contains(photon.toString()))
                    continue;
                visitedVectors.add(photon.toString());

                int newRow = photon.Row + photon.YOffset;
                int newColumn = photon.Column + photon.XOffset;

                if ((newRow >= 0) && (newRow < gameBoard.length)
                    && (newColumn >= 0) && (newColumn < gameBoard[0].length))
                {
                    char newPosition = gameBoard[newRow][newColumn];

                    if (newPosition == '.')
                    {
                        newPositions.add(new Vector(newRow, newColumn, photon.XOffset, photon.YOffset));
                    }
                    else if (newPosition == '/')
                    {
                        int newxOffset = -photon.YOffset;
                        int newyOffset = -photon.XOffset;
                        newPositions.add(new Vector(newRow, newColumn, newxOffset, newyOffset));
                    }
                    else if (newPosition == '\\')
                    {
                        int newxOffset = photon.YOffset;
                        int newyOffset = photon.XOffset;
                        newPositions.add(new Vector(newRow, newColumn, newxOffset, newyOffset));
                    }
                    else if (newPosition == '-')
                    {
                        if (photon.XOffset == 0)
                        {
                            newPositions.add(new Vector(newRow, newColumn, -1, 0));
                            newPositions.add(new Vector(newRow, newColumn, 1, 0));
                        }
                        else
                        {
                            newPositions.add(new Vector(newRow, newColumn, photon.XOffset, photon.YOffset));
                        }
                    }
                    else if (newPosition == '|')
                    {
                        if (photon.YOffset == 0)
                        {
                            newPositions.add(new Vector(newRow, newColumn, 0, -1));
                            newPositions.add(new Vector(newRow, newColumn, 0, 1));
                        }
                        else
                        {
                            newPositions.add(new Vector(newRow, newColumn, photon.XOffset, photon.YOffset));
                        }
                    }
                }
            }
            lightPositions = newPositions;

            //outputGameBoard(gameBoard, visitedPositions);
        }

        return visitedPositions.size() - 1;
    }

    private static void outputGameBoard(char[][] gameBoard, Set<String> visitedPositions) {
        System.out.println("Game Board:");
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[row].length; column++) {
                String s = String.format("%s,%s", row, column);
                char output = gameBoard[row][column];
                if (visitedPositions.contains(s)) {
                    output = '*';
                }
                System.out.print(output);
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