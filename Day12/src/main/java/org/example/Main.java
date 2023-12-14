package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Map<String, Long> cache = new HashMap<>();

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

                List<Integer> newCheckSums = new ArrayList<>(checkSums);
                possibleWaysPart1 += calculatePossibleWays(split[0], newCheckSums);

                String part2Split = String.format("%s?%s?%s?%s?%s", split[0],split[0],split[0],split[0],split[0]);
                List<Integer> checkSumsPart2 = new ArrayList<>();
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);
                checkSumsPart2.addAll(checkSums);

                long i = calculatePossibleWays(part2Split, checkSumsPart2);
                System.out.println(String.format("Possible ways: %d", i));
                possibleWaysPart2 += i;

                line = br.readLine();
            }

            System.out.println(String.format("Output Part 1: %d", possibleWaysPart1));
            System.out.println(String.format("Output Part 2: %d", possibleWaysPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    public static long calculatePossibleWays(String gameBoard, List<Integer> checkSums) throws Exception {
        String cacheKey = gameBoard + "_";
        for (Integer i : checkSums) {
            cacheKey += Integer.toString(i) + "_";
        }
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        //outputGameBoard(gameBoard, checkSums);

        long value = GetCount(gameBoard, checkSums);
        cache.put(cacheKey, value);

        return value;
    }

    public static long GetCount(String gameBoard, List<Integer> groups) throws Exception
    {
        while (true) {
            if (groups.size() == 0) {
                // no more groups to match
                // If there are any springs left in our gameboard
                // this doesn't match
                if (gameBoard.contains("#")) {
                    return 0;
                } else {
                    return 1;
                }
            }

            if (gameBoard.isEmpty()) {
                // No more springs to match, although we still have groups
                // no match
                return 0;
            }

            if (gameBoard.startsWith(".")) {
                gameBoard = gameBoard.replaceAll("^\\.*","");
                continue;
            }

            if (gameBoard.startsWith("?")) {
                // recurse
                String newString = gameBoard.substring(1);
                List<Integer> newGroup1 = new ArrayList<Integer>(groups);
                List<Integer> newGroup2 = new ArrayList<Integer>(groups);

                return calculatePossibleWays("." + newString, newGroup1)
                        + calculatePossibleWays("#" + newString, newGroup2);
            }

            if (gameBoard.startsWith("#")) {
                // Start of a group, do we have any groups left?
                if (groups.size() == 0) {
                    return 0;
                }

                int firstGroup = groups.get(0);
                if (gameBoard.length() < firstGroup) {
                    // not enough characters left to match the group
                    return 0;
                }

                // group cannot contain dots for the given length
                // or it wouldn't be a whole group
                for (int i = 0; i < firstGroup; i++) {
                    if (gameBoard.charAt(i) == '.') {
                        return 0;
                    }
                }

                // group must be equal to the exact size of the group
                // if there is another group after it
                if (groups.size() > 1) {
                    if ((gameBoard.length() < firstGroup + 1) || (gameBoard.charAt(firstGroup) == '#')) {
                        return 0;
                    }

                    // Move to the next group
                    // Skip the character after the group
                    gameBoard = gameBoard.substring(firstGroup + 1);
                    groups.remove(0);
                    continue;
                }

                gameBoard = gameBoard.substring(groups.get(0));
                groups.remove(0);
                continue;
            }

            throw new Exception("Invalid input");
        }
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