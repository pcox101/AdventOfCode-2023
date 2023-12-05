package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            Map<String, ResourceMap> resourceMaps = new HashMap<>();
            List<Long> seeds = new ArrayList<>();
            Pattern seedsPattern = Pattern.compile("^seeds: ([\\d ]*)$");
            Pattern resourcePattern = Pattern.compile("^(\\w*)-to-(\\w*) map:$");
            BufferedReader br = new BufferedReader(new FileReader("Day05/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {
                Matcher seedMatcher = seedsPattern.matcher(line);
                Matcher resourceMatcher = resourcePattern.matcher(line);
                if (seedMatcher.matches())
                {
                    String[] seedsList = seedMatcher.group(1).split(" ");
                    for (String seed: seedsList) {
                        seeds.add(Long.parseLong(seed));
                    }
                }
                else if (resourceMatcher.matches())
                {
                    ResourceMap map = new ResourceMap(resourceMatcher.group(1), resourceMatcher.group(2));
                    line = br.readLine();
                    while (line != null && !line.isBlank())
                    {
                        map.addResourceLine(line);
                        line = br.readLine();
                    }
                    resourceMaps.put(map.Source, map);
                }

                line = br.readLine();
            }

            Long lowestNumber = Long.MAX_VALUE;

            for (Long seed: seeds) {
                Long resourceValue = getResourceValueForSeed(resourceMaps, seed);
                if (resourceValue < lowestNumber)
                {
                    lowestNumber = resourceValue;
                }
            }

            // Part 2, seeds are ranges...
            Long lowestNumberPart2 = Long.MAX_VALUE;
            for (int counter = 0; counter < seeds.size(); counter = counter + 2)
            {
                Long startSeed = seeds.get(counter);
                Long endSeed = startSeed + seeds.get(counter + 1) - 1;
                System.out.println(String.format("Processing seeds %d to %d", startSeed, endSeed));

                for (Long seed = startSeed; seed < endSeed; seed++)
                {
                    Long resourceValue = getResourceValueForSeed(resourceMaps, seed);
                    if (resourceValue < lowestNumberPart2)
                    {
                        lowestNumberPart2 = resourceValue;
                    }
                }
            }

            System.out.println(String.format("Lowest value is: %d", lowestNumber));
            System.out.println(String.format("Lowest value Part 2 is: %d", lowestNumberPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }

    }

    private static Long getResourceValueForSeed(Map<String, ResourceMap> resourceMaps, Long startValue) {
        Long resourceValue = startValue;
        String resource = "seed";
        while (!resource.equals("location"))
        {
            Boolean foundSomething = false;
            ResourceMap thisMap = resourceMaps.get(resource);
            for (SourceOffset offset: thisMap.SourceOffsets) {
                Long newValue = offset.getDestinationValue(resourceValue);
                if (newValue != null)
                {
                    //System.out.println(String.format("Resource %s value %d maps to resource %s value %d",
                    //         resource,
                    //         resourceValue,
                    //         thisMap.Destination,
                    //         newValue));

                    resourceValue = newValue;
                    resource = thisMap.Destination;
                    foundSomething = true;
                    break;
                }
            }
            if (!foundSomething)
            {
                // no map means keep the same
                //System.out.println(String.format("Resource %s value %d maps to resource %s value %d",
                //        resource,
                //        resourceValue,
                //        thisMap.Destination,
                //        resourceValue));
                resource = thisMap.Destination;
            }
        }
        return resourceValue;
    }
}