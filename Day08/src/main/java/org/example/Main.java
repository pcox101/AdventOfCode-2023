package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("Day08/src/main/resources/input.txt"));

            String line = br.readLine();
            String LR = line;
            br.readLine(); // blank line
            line = br.readLine();

            Map<String, String> leftMap = new HashMap<>();
            Map<String, String> rightMap = new HashMap<>();

            while (line != null) {

                String source = line.substring(0, 3);
                String lDest = line.substring(7, 10);
                String rDest = line.substring(12, 15);

                leftMap.put(source, lDest);
                rightMap.put(source, rDest);

                line = br.readLine();
            }

            //HowManySteps("AAA", false, LR, leftMap, rightMap);

            DoPartTwo(LR, leftMap, rightMap);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static Integer HowManySteps(String location, Boolean fullZ, String LR, Map<String, String> leftMap, Map<String, String> rightMap) {
        Integer counter = 0;
        Integer lrOffset = 0;
        while ((fullZ && !location.equals("ZZZ")) || (!fullZ && !location.endsWith("Z"))) {
            String leftOrRight = LR.substring(lrOffset, lrOffset + 1);
            if (leftOrRight.equals("L")) {
                location = leftMap.get(location);
            } else {
                location = rightMap.get(location);
            }
            counter++;
            lrOffset++;
            if (lrOffset + 1 > LR.length()) {
                lrOffset = 0;
            }
        }

        System.out.println(String.format("Number of steps for location %s: %d", location, counter));
        return counter;
    }

    private static void DoPartTwo(String LR, Map<String, String> leftMap, Map<String, String> rightMap) {
        List<String> locations = new ArrayList<>();
        for (Map.Entry<String, String> starts : leftMap.entrySet()) {
            if (starts.getKey().endsWith("A")) {
                locations.add(starts.getKey());
            }
        }

        System.out.println(String.format("Running %d locations simultaneously.", locations.size()));

        List<Integer> numberOfStepsList = new ArrayList<>();
        for (Integer i = 0; i < locations.size(); i++) {
            numberOfStepsList.add(HowManySteps(locations.get(i), false, LR, leftMap, rightMap));
        }

        // Brute force the LCM...
        Integer maxNumber = 0;
        Long lcm = 0L;
        for (Integer i = 0; i < numberOfStepsList.size(); i++)
        {
            maxNumber = Math.max(maxNumber, numberOfStepsList.get(i));
        }

        lcm = Long.valueOf(maxNumber);
        while (true)
        {
            Boolean foundNonMultiple = false;
            for (Integer i = 0; i < numberOfStepsList.size(); i++)
            {
                if (lcm % numberOfStepsList.get(i) != 0)
                {
                    foundNonMultiple = true;
                    break;
                }
            }
            if (!foundNonMultiple)
                break;
            else
                lcm += maxNumber;
        }

        System.out.println(String.format("Lowest common multiple is %d.", lcm));


    }
}