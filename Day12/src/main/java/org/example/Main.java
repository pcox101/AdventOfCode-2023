package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day12/src/main/resources/input.txt"));

            String line = br.readLine();
            int possibleWaysPart1 = 0;
            long possibleWaysPart2 = 0;
            while (line != null) {
                String[] split = line.split(" ");

                String[] split2 = split[1].split(",");
                List<Integer> checkSums = new ArrayList<>();
                for (String val: split2)
                    checkSums.add(Integer.parseInt(val));

                possibleWaysPart1 += calculatePossibleWays(split[0], checkSums);

                String part2Split = String.format("%s?%s?%s?%s?%s", split[0],split[0],split[0],split[0],split[0]);
                List<Integer> checkSumsPart2 = new ArrayList<>();
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);

                possibleWaysPart2 += calculatePossibleWays(part2Split, checkSumsPart2);

                line = br.readLine();
            }

            System.out.println(String.format("Output Part 1: %d", possibleWaysPart1));
            System.out.println(String.format("Output Part 2: %d", possibleWaysPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    public static int calculatePossibleWays(String gameBoard, List<Integer> checkSums)
    {
        List<String> possibleGameBoards = new ArrayList<>();
        int possibleWays = 0;
        for (int i = 0; i < gameBoard.length(); i++)
        {
            List<String> newPossibleGameBoards = new ArrayList<>();
            char ch = gameBoard.charAt(i);
            if (ch == '.')
            {
                if (i == 0)
                {
                    newPossibleGameBoards.add(".");
                }
                else {
                    for (int j = 0; j < possibleGameBoards.size(); j++) {
                        String newPossibleBoard = possibleGameBoards.get(j) + ".";
                        if (isPartialPossible(newPossibleBoard, checkSums)) {
                            newPossibleGameBoards.add(newPossibleBoard);
                        }
                    }
                }
            }
            else if (ch == '#') {
                if (i == 0) {
                    newPossibleGameBoards.add("#");
                } else
                    // Just add it
                    for (int j = 0; j < possibleGameBoards.size(); j++) {
                        newPossibleGameBoards.add(possibleGameBoards.get(j) + "#");
                    }
            }
            else {
                if (i == 0) {
                    // Add both to the starter
                    newPossibleGameBoards.add("#");
                    newPossibleGameBoards.add(".");
                } else {
                    // always add the hash (doesn't complete a group)
                    // may be more efficient to check if this doesn't exceed the possibles, but it'll be removed
                    // on the next . anyway
                    for (int j = 0; j < possibleGameBoards.size(); j++) {
                        newPossibleGameBoards.add(possibleGameBoards.get(j) + "#");
                    }
                    // Add the dot, only if this is possible
                    for (int j = 0; j < possibleGameBoards.size(); j++) {
                        String newPossibleBoard = possibleGameBoards.get(j) + ".";
                        if (isPartialPossible(newPossibleBoard, checkSums)) {
                            newPossibleGameBoards.add(newPossibleBoard);
                        }
                    }
                }
            }

            possibleGameBoards = newPossibleGameBoards;
        }

        // We should now have a list of possible boards which are checked up to the
        // last group, so hopefully not too many and we can just check them each
        // in turn
        for (int j = 0; j < possibleGameBoards.size(); j++)
        {
            if (isPossible(possibleGameBoards.get(j), checkSums))
            {
                possibleWays++;
            }
        }

        System.out.println(String.format("Possible ways for %s: %d", gameBoard, possibleWays));

        return possibleWays;
    }

    public static boolean isPossible(String gameBoard, List<Integer> checkSums)
    {
        //System.out.println("Final test");
        //outputGameBoard(gameBoard, checkSums);

        List<Integer> thisChecksum = new ArrayList<>();

        int damagedCount = 0;
        for (int i = 0; i < gameBoard.length(); i++)
        {
            char currentChar = gameBoard.charAt(i);
            if (currentChar == '.')
            {
                if (damagedCount != 0)
                {
                    thisChecksum.add(damagedCount);
                    damagedCount = 0;
                }
            }
            if (currentChar == '#')
            {
                damagedCount++;
            }
        }

        // Check the last one
        if (damagedCount > 0)
        {
            thisChecksum.add(damagedCount);
        }

        //outputGameBoard(gameBoard, thisChecksum);

        // Now check that our checksums match
        if (thisChecksum.size() == checkSums.size())
        {
            for (int i = 0; i < thisChecksum.size(); i++)
            {
                if (!thisChecksum.get(i).equals(checkSums.get(i)))
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    private static boolean isPartialPossible(String gameBoard, List<Integer> checkSums)
    {
        //outputGameBoard(gameBoard, checkSums);

        int damagedCount = 0;
        int currentCheckSumOffset = 0;
        for (int i = 0; i < gameBoard.length(); i++)
        {
            char currentChar = gameBoard.charAt(i);
            if (currentChar == '.')
            {
                if (damagedCount != 0)
                {
                    if (currentCheckSumOffset >= checkSums.size())
                    {
                        // Too many blocks
                        return false;
                    }
                    // Finished a block, check it matches
                    if (checkSums.get(currentCheckSumOffset) != damagedCount) {
                        //System.out.println("Not possible");
                        return false;
                    }
                    damagedCount = 0;
                    currentCheckSumOffset++;
                }
            }
            if (currentChar == '#')
            {
                damagedCount++;
            }
        }
        // it could be partial, so don't check the last one here.
        //System.out.println("Still possible");
        return true;
    }

    private static void outputGameBoard(String gameBoard, List<Integer> checkSums)
    {
        System.out.print(String.format("Game board: %s",gameBoard));
        System.out.print(" ");
        for (int cs: checkSums) {
            System.out.print(String.format("%d,",cs));
        }
        System.out.println();
    }

}