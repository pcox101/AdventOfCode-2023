package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            Pattern numberPattern = Pattern.compile("^\\d$");
            Pattern symbolPattern = Pattern.compile("^[^\\d.]$");
            Pattern gearPattern = Pattern.compile("^[*]$");

            int schematicValue = 0;
            List<String> schematic = new ArrayList<String>();
            Map<String, List<Integer>> gears = new HashMap<String, List<Integer>>();

            BufferedReader br = new BufferedReader(new FileReader("Day03/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {
                schematic.add(line);
                line = br.readLine();
            }
            br.close();

            int currentValue = 0;
            Boolean includeCurrentValue = false;
            Boolean gearAdjacent = false;
            String gearPosition = "";
            for (int y = 0; y < schematic.size(); y++)
            {
                String parsingLine = schematic.get(y);
                for (int x = 0; x < parsingLine.length(); x++)
                {
                    // If it's a digit
                    String currentCharacter = parsingLine.substring(x,x + 1);
                    Matcher numberMatcher = numberPattern.matcher(currentCharacter);
                    if (numberMatcher.matches())
                    {
                        currentValue *= 10;
                        currentValue += Integer.parseInt(currentCharacter);

                        // See if there's a symbol adjacent
                        SymbolLookupResult symbolAdjacent = SymbolAdjacent(schematic, symbolPattern, x, y);
                        if (symbolAdjacent.SymbolMatches)
                        {
                            includeCurrentValue = true;
                        }
                        // See if there's a gear adjacent
                        SymbolLookupResult gearAdjacentSymbol = SymbolAdjacent(schematic, gearPattern, x, y);
                        if (gearAdjacentSymbol.SymbolMatches)
                        {
                            gearAdjacent = true;
                            gearPosition = gearAdjacentSymbol.SymbolLocation;
                        }
                    }
                    else
                    {
                        if (includeCurrentValue)
                        {
                            System.out.println(String.format("Include : %d", currentValue));
                            schematicValue += currentValue;
                        }
                        else if (currentValue > 0)
                        {
                            System.out.println(String.format("Exclude : %d", currentValue));
                        }
                        if (gearAdjacent)
                        {
                            System.out.println(String.format("Gear Adjacent : %d", currentValue));
                            if (gears.containsKey(gearPosition))
                            {
                                System.out.println(String.format("Adding to gear : %s", gearPosition));
                                List<Integer> gearsList = gears.get(gearPosition);
                                gearsList.add(currentValue);
                            }
                            else
                            {
                                System.out.println(String.format("New gear : %s", gearPosition));
                                List<Integer> gearsList = new ArrayList<>();
                                gearsList.add(currentValue);
                                gears.put(gearPosition, gearsList);
                            }
                        }
                        else if (currentValue > 0)
                        {
                            System.out.println(String.format("Gear Not Adjacent : %d", currentValue));
                        }
                        currentValue = 0;
                        includeCurrentValue = false;
                        gearPosition = "";
                        gearAdjacent = false;
                    }
                }
                // new line, reset the number
                if (includeCurrentValue)
                {
                    System.out.println(String.format("Include : %d", currentValue));
                    schematicValue += currentValue;
                }
                else if (currentValue > 0)
                {
                    System.out.println(String.format("Exclude : %d", currentValue));
                }
                if (gearAdjacent)
                {
                    System.out.println(String.format("Gear Adjacent : %d", currentValue));
                    if (gears.containsKey(gearPosition))
                    {
                        System.out.println(String.format("Adding to gear : %s", gearPosition));
                        List<Integer> gearsList = gears.get(gearPosition);
                        gearsList.add(currentValue);
                    }
                    else
                    {
                        System.out.println(String.format("New gear : %s", gearPosition));
                        List<Integer> gearsList = new ArrayList<>();
                        gearsList.add(currentValue);
                        gears.put(gearPosition, gearsList);
                    }
                }
                else if (currentValue > 0)
                {
                    System.out.println(String.format("Gear Not Adjacent : %d", currentValue));
                }
                currentValue = 0;
                includeCurrentValue = false;
                gearPosition = "";
                gearAdjacent = false;
            }

            System.out.println(String.format("Output: %s", schematicValue));
            // Calculate our gears
            int totalGears = 0;
            for (Map.Entry<String, List<Integer>> entry : gears.entrySet()) {
                List<Integer> gearsEntry = entry.getValue();
                if (gearsEntry.size() == 2) {

                    totalGears += (gearsEntry.get(0) * gearsEntry.get(1));
                }
            }
            System.out.println(String.format("Gear ratio sum: %s", totalGears));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static SymbolLookupResult SymbolAdjacent(List<String> schematic, Pattern symbolPattern, int x, int y) {
        // row above
        if (y != 0)
        {
            for (int newx = x - 1; newx < x + 2; newx++)
            {
                if ((newx != -1) && (newx != schematic.get(0).length()))
                {
                    if (matchesDigit(schematic, symbolPattern, y - 1, newx)) {
                        return new SymbolLookupResult(true, y - 1, newx);
                    }
                }
            }
        }
        // left
        if (x != 0)
        {
            if (matchesDigit(schematic, symbolPattern, y, x - 1))
                return new SymbolLookupResult(true, y, x - 1);
        }
        // right
        if (x != schematic.get(0).length() - 1)
        {
            if (matchesDigit(schematic, symbolPattern, y, x + 1))
                return new SymbolLookupResult(true, y, x + 1);
        }
        // row below
        if (y != schematic.size() - 1)
        {
            for (int newx = x - 1; newx < x + 2; newx++)
            {
                if ((newx != -1) && (newx != schematic.get(0).length()))
                {
                    if (matchesDigit(schematic, symbolPattern, y + 1, newx)) {
                        return new SymbolLookupResult(true, y + 1, newx);
                    }
                }
            }
        }
        return new SymbolLookupResult(false, 0, 0);
    }

    private static boolean matchesDigit(List<String> schematic, Pattern symbolPattern, int y, int x) {
        String testString = schematic.get(y).substring(x, x + 1);
        Matcher symbolMatcher = symbolPattern.matcher(testString);
        if (symbolMatcher.matches()){
            return true;
        }
        return false;
    }
}
