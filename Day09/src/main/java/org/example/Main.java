package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day09/src/main/resources/input.txt"));

            Integer part1counter = 0;
            Integer part2counter = 0;
            String line = br.readLine();
            while (line != null) {
                String[] rowStrings = line.split(" ");
                List<Integer> rowValues = new ArrayList<>();
                for (String entry: rowStrings)
                {
                    rowValues.add(Integer.parseInt(entry));
                }

                Integer[] rowCalcs = getRowCalculations(rowValues);
                System.out.println(String.format("Part 1: %d", rowCalcs[0]));
                System.out.println(String.format("Part 2: %d", rowCalcs[1]));
                part1counter += rowCalcs[0];
                part2counter += rowCalcs[1];

                line = br.readLine();
            }
            System.out.println(String.format("Part 1: %d", part1counter));
            System.out.println(String.format("Part 2: %d", part2counter));

       } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    static Integer[] getRowCalculations(List<Integer> rowValues)
    {
        List<Integer> lastValues = new ArrayList<>();
        List<Integer> firstValues = new ArrayList<>();
        List<Integer> thisLine = new ArrayList<>(rowValues);

        Boolean done = false;
        while (!done)
        {
            //outputList(thisLine);
            List<Integer> nextLine = new ArrayList<>();
            Boolean anyNonZero = false;
            for (Integer i = 0; i < thisLine.size() - 1; i++)
            {
                Integer diff = thisLine.get(i + 1) - thisLine.get(i);
                if (diff != 0) { anyNonZero = true; }
                nextLine.add(diff);
            }
            lastValues.add(thisLine.get(thisLine.size() - 1));
            firstValues.add(thisLine.get(0));
            if (!anyNonZero) { done = true; }
            thisLine = nextLine;
        }

        Integer runningValuePart1 = 0;
        Integer runningValuePart2 = 0;
        //outputList(lastValues);
        //outputList(firstValues);

        for (Integer i = lastValues.size() - 1; i >= 0; i--) {
            runningValuePart1 = runningValuePart1 + lastValues.get(i);;
            runningValuePart2 = firstValues.get(i) - runningValuePart2;
        }

        Integer result[] = {runningValuePart1, runningValuePart2};
        return result;
    }

    static void outputList(List<Integer> list)
    {
        for (Integer val: list) {
            System.out.print(String.format("%d ", val));
        }
        System.out.println();
    }
}