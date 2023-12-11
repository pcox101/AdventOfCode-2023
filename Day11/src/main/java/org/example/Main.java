package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {


    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day11/src/main/resources/input.txt"));
            List<String> puzzleInput = new ArrayList<>();

            String line = br.readLine();
            while (line != null) {
                puzzleInput.add(line);
                line = br.readLine();
            }

            int height = puzzleInput.size();
            int width = puzzleInput.get(0).length();
            Set<Integer> columnsNeedingExpanding = new HashSet<>();

            // expansion factor - for part 1 use 2 (2 times larger...)
            int expansionFactor = 1000000;

            // Which columns need expanding?
            for (int x = 0; x < width; x++)
            {
                boolean columnContainsGalaxy = false;
                for (int y = 0; y < height; y++)
                {
                    char value = puzzleInput.get(y).charAt(x);
                    if (value == '#') {
                        columnContainsGalaxy = true;
                        break;
                    }
                }
                if (!columnContainsGalaxy)
                {
                    columnsNeedingExpanding.add(x);
                }
            }

            // Build our x/y of galaxies
            List<String> galaxies = buildGalaxies(puzzleInput, columnsNeedingExpanding, expansionFactor);

            //outputGameBoard(galaxies);

            long output = calculateDistances(galaxies);

            System.out.println(String.format("Output: %d", output));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculateDistances(List<String> galaxies)
    {
        long counter = 0;
        for (int i = 0; i < galaxies.size(); i++)
        {
            for (int j = i + 1; j < galaxies.size(); j++)
            {
                counter += calculateDistance(galaxies.get(i), galaxies.get(j));
            }
        }

        return counter;
    }

    private static long calculateDistance(String starter, String ender)
    {
        String startSplit[] = starter.split("_");
        String endSplit[] = ender.split("_");
        long startX = Long.parseLong(startSplit[0]);
        long startY = Long.parseLong(startSplit[1]);
        long endX = Long.parseLong(endSplit[0]);
        long endY = Long.parseLong(endSplit[1]);

        // Distance between these two points is the
        // manhattan distance
        long distance = Math.abs(startX - endX) + Math.abs(startY - endY);

        return distance;
    }

    private static List<String> buildGalaxies(List<String> puzzleInput, Set<Integer> columnsNeedingExpanding, int expansionFactor)
    {
        List<String> result = new ArrayList<>();
        long y = 0;

        for (String line: puzzleInput) {
            if (line.contains("#")) {
                long x = 0;
                for (int origx = 0; origx < line.length(); origx++) {
                    if (columnsNeedingExpanding.contains(origx))
                    {
                        x += expansionFactor - 1;
                    }
                    else {
                        char field = line.charAt(origx);
                        if (field == '#')
                        {
                            String location = String.format("%d_%d",x,y);
                            result.add(location);
                        }
                    }
                    x++;
                }
            }
            else {
                // No galaxies, move on
                y += expansionFactor - 1;
            }
            y++;
        }
        return result;
    }

    private static void outputGameBoard(List<String> galaxies)
    {
        long maxX = 0, maxY = 0;
        for (String galaxy: galaxies)
        {
            String[] split = galaxy.split("_");
            maxX = Math.max(maxX, Integer.parseInt(split[0]));
            maxY = Math.max(maxY, Integer.parseInt(split[1]));
        }

        for (long y = 0; y < maxY + 1; y++)
        {
            for (long x = 0; x < maxX + 1; x++)
            {
                String location = String.format("%d_%d",x,y);
                if (galaxies.contains(location))
                {
                    System.out.print("#");
                }
                else System.out.print(".");
            }
            System.out.println();
        }
    }
}
