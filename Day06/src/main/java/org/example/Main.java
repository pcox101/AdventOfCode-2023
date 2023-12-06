package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day06/src/main/resources/input.txt"));

            String line = br.readLine();
            List<Long> raceLengths = parseLine(line);
            Long raceLength = parseSingleLine(line);
            line = br.readLine();
            List<Long> raceTimes = parseLine(line);
            Long raceTime = parseSingleLine(line);

            Long numberOfWaysTotal = 1L;

            for (Integer i = 0; i < raceLengths.size(); i++)
            {
                Long minSolution = getMinSolution(raceTimes.get(i), raceLengths.get(i));
                Long maxSolution = getMaxSolution(raceTimes.get(i), raceLengths.get(i));

                System.out.println(String.format("Game %d has a min of %d and a max of %d", i, minSolution, maxSolution));

                numberOfWaysTotal *= (maxSolution - minSolution + 1);
            }

            System.out.println(String.format("Total number of ways: %d", numberOfWaysTotal));

            {
                Long maxSolution = getMaxSolution(raceTime, raceLength);
                Long minSolution = getMinSolution(raceTime, raceLength);
                System.out.println(String.format("Total game has a min of %d and a max of %d", minSolution, maxSolution));
                Long numberOfWaysSingle = maxSolution - minSolution + 1;

                System.out.println(String.format("Single game number of ways: %d", numberOfWaysSingle));

            }

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }

    }

    private static List<Long> parseLine(String line)
    {
        String[] fields = line.split(" ");
        List<Long> values = new ArrayList<>();
        for (String s: fields)
        {
            if (!s.isEmpty())
            {
                try {
                    values.add(Long.parseLong(s));
                }
                catch(Exception ex)
                {
                    // Ignore anything we can't parse...
                }
            }
        }
        return values;
    }

    private static Long parseSingleLine(String line)
    {
        Long totalValue = 0L;
        for (Integer i = 0; i < line.length(); i++)
        {
            Character val = line.charAt(i);
            if (Character.isDigit(val))
            {
                totalValue *= 10;
                totalValue += val - '0';
            }
        }
        return totalValue;
    }

    private static Long getMinSolution(Long minDistance, Long totalTime)
    {
        double sqrt = Math.sqrt(Math.pow(totalTime, 2) - (4 * minDistance));
        Double solution1 = ((-totalTime + sqrt) / -2);
        Double solution2 = ((-totalTime - sqrt) / -2);

        Double minSolution = Math.min(solution1, solution2);
        if ((minSolution % 1) == 0)
        {
            minSolution += 1;
        }

        return Math.round(Math.ceil(minSolution));
    }

    private static Long getMaxSolution(Long minDistance, Long totalTime)
    {
        double sqrt = Math.sqrt(Math.pow(totalTime, 2) - (4 * minDistance));
        Double solution1 = ((-totalTime + sqrt) / -2);
        Double solution2 = ((-totalTime - sqrt) / -2);

        Double maxSolution = Math.max(solution1, solution2);

        if ((maxSolution % 1) == 0)
        {
            maxSolution -= 1;
        }
        return Math.round(Math.floor(maxSolution));
    }
}