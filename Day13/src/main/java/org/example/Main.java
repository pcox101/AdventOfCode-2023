package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day13/src/main/resources/input.txt"));

            String line = br.readLine();
            int outputPart1 = 0;
            int outputPart2 = 0;
            List<String> gameBoard = new ArrayList<>();
            while (line != null) {
                gameBoard.add(line);

                line = br.readLine();

                if ((line == null) || (line.isEmpty())) {
                    // End of a set, look for reflections
                    char[][] charGameBoard = getCharArrayGameBoard(gameBoard);

                    int part1 = getReflectionNumber(charGameBoard, 0);
                    System.out.println(String.format("Board has reflection number: %d", part1));
                    outputPart1 += part1;
                    int part2 = getSmudgedReflectionNumber(charGameBoard);
                    System.out.println(String.format("Board has smudged reflection number: %d", part2));
                    outputPart2 += part2;
                    gameBoard.clear();

                    if (line != null)
                    {
                        line = br.readLine();
                    }
                }
            }

            System.out.println(String.format("Output Part 1: %d", outputPart1));
            System.out.println(String.format("Output Part 2: %d", outputPart2));


        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static int getReflectionNumber(char[][] gameBoard, int originalReflection)
    {
        int reflection = 0;
        // For vertical reflections, work down checking for reflection after each row
        // don't check the last row
        for (int i = 0; i < gameBoard.length - 1; i++)
        {
            // Build a string for the minimum before and after
            String stringBefore = "", stringAfter = "";
            int numberToTest = Math.min(i + 1, gameBoard.length - i - 1);
            for (int j = 0; j < numberToTest; j++)
            {
                for (int column = 0; column < gameBoard[j].length; column++)
                {
                    char charBefore = gameBoard[i - j][column];
                    char charAfter = gameBoard[i + j + 1][column];
                    stringBefore += Character.toString(charBefore);
                    stringAfter += Character.toString(charAfter);
                }
            }
            //System.out.println(String.format("Line %d. Comparing %s with %s", i, stringBefore, stringAfter));

            if (stringBefore.equals(stringAfter))
            {
                reflection = 100 * (i + 1);
                if (reflection == originalReflection)
                    reflection = 0;
                else
                    break;
            }
        }

        if (reflection == 0)
        {
            // Horizontal reflection is the same, just the string is more difficult to build!
            int numberOfColumns = gameBoard[0].length;
            for (int i = 0; i < numberOfColumns - 1; i++)
            {
                // Build a string for the minimum before and after
                String stringBefore = "", stringAfter = "";
                int numberToTest = Math.min(i + 1, numberOfColumns - i - 1);
                for (int j = 0; j < numberToTest; j++)
                {
                    for (int row = 0; row < gameBoard.length; row++)
                    {
                        char charBefore = gameBoard[row][i - j];
                        char charAfter = gameBoard[row][i + j + 1];
                        stringBefore += Character.toString(charBefore);
                        stringAfter += Character.toString(charAfter);
                    }
                }
                //System.out.println(String.format("Column %d. Comparing %s with %s", i, stringBefore, stringAfter));

                if (stringBefore.equals(stringAfter))
                {
                    reflection = (i + 1);
                    if (reflection == originalReflection)
                        reflection = 0;
                    else
                        break;
                }
            }
        }

        //System.out.println(String.format("Board has reflection number: %d", reflection));

        return reflection;
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

    private static int getSmudgedReflectionNumber(char[][] gameBoard)
    {
        int unsmudgedReflection = getReflectionNumber(gameBoard, 0);

        for (int row = 0; row < gameBoard.length; row++)
        {
            for (int column = 0; column < gameBoard[0].length; column++)
            {
                // Smudge this position
                //System.out.println(String.format("Smudging %d,%d", row, column));

                char oldVal = gameBoard[row][column];
                if (oldVal == '#')
                {
                    gameBoard[row][column] = '.';
                }
                else
                {
                    gameBoard[row][column] = '#';
                }
                //outputGameBoard(gameBoard);
                int newReflection = getReflectionNumber(gameBoard, unsmudgedReflection);
                if (newReflection > 0)
                {
                    System.out.println(String.format("Smudge was at %d,%d", row, column));
                    return newReflection;
                }
                gameBoard[row][column] = oldVal;
            }
        }
        return 0;
    }

    private static void outputGameBoard(char[][] gameBoard) {
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[0].length; column++) {
                System.out.print(gameBoard[row][column]);
            }
            System.out.println();
        }
    }
}