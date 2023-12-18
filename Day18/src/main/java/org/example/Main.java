package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static class Corner
    {
        public long X;
        public long Y;

        public Corner(long x, long y)
        {
            X = x;
            Y = y;
        }
    }
    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("Day18/src/main/resources/input.txt"));
            Pattern pattern = Pattern.compile("^([RLDU]) (\\d*) \\(#([0-9a-fA-F]{6})\\)$");

            String line = br.readLine();
            long outputPart1 = 0;
            long outputPart2 = 0;

            long p1currentX = 0;
            long p1currentY = 0;
            long p1perimeter = 0;

            long p2currentX = 0;
            long p2currentY = 0;
            long p2perimeter = 0;

            List<Corner> part1corners = new ArrayList<>();
            List<Corner> part2corners = new ArrayList<>();

            part1corners.add(new Corner(0,0));
            part2corners.add(new Corner(0,0));

            while (line != null) {
                Matcher match = pattern.matcher(line);

                if (match.matches()) {
                    String direction = match.group(1);
                    int number = Integer.parseInt(match.group(2));
                    String colour = match.group(3);

                    // Part 1

                    p1perimeter += number;
                    int yOffset = direction.equals("D") ? -number : direction.equals("U") ? number : 0;
                    int xOffset = direction.equals("L") ? -number : direction.equals("R") ? number : 0;

                    p1currentX = p1currentX + xOffset;
                    p1currentY = p1currentY + yOffset;

                    part1corners.add(new Corner(p1currentX, p1currentY));

                    // part 2
                    String hexDistance = colour.substring(0,5);
                    number = Integer.parseInt(hexDistance, 16);
                    direction = colour.substring(5,6);
                    p2perimeter += number;

                    yOffset = direction.equals("1") ? -number : direction.equals("3") ? number : 0;
                    xOffset = direction.equals("2") ? -number : direction.equals("0") ? number : 0;

                    p2currentX = p2currentX + xOffset;
                    p2currentY = p2currentY + yOffset;

                    part2corners.add(new Corner(p2currentX, p2currentY));
                }
                else {
                    throw new Exception("No match: " + line);
                }

                line = br.readLine();
            }

            outputPart1 = calculateArea(part1corners, p1perimeter);

            outputPart2 = calculateArea(part2corners, p2perimeter);

            System.out.println(String.format("Output Part 1: %d", outputPart1));
            System.out.println(String.format("Output Part 2: %d", outputPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculateArea(List<Corner> corners, long perimeter)
    {
        long counter = 0;

        for (int i = 0; i < corners.size() - 1; i++)
        {
            counter += corners.get(i).Y * corners.get(i + 1).X;
            counter -= corners.get(i).X * corners.get(i + 1).Y;
        }

        // add the perimeter
        counter += perimeter;

        counter = counter / 2 + 1;

        return counter;
    }
}