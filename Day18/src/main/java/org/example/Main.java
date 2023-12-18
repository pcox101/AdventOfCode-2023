package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static class MinMax
    {
        public int minX = Integer.MAX_VALUE;
        public int minY = Integer.MAX_VALUE;
        public int maxX = Integer.MIN_VALUE;
        public int maxY = Integer.MIN_VALUE;

        private void updateMinMax(int x, int y)
        {
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
    }

    private static class Coordinate
    {
        public int X;
        public int Y;

        public String toString()
        {
            return String.format("%d,%d", X, Y);
        }

        public Coordinate(int x, int y)
        {
            X = x; Y = y;
        }

        public Coordinate(String coord)
        {
            String[] split = coord.split(",");
            X = Integer.parseInt(split[0]);
            Y = Integer.parseInt(split[1]);
        }
    }
    public static void main(String[] args) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("Day18/src/main/resources/input.txt"));
            Pattern pattern = Pattern.compile("^([RLDU]) (\\d*) \\(#([0-9a-fA-F]{6})\\)$");

            String line = br.readLine();
            long outputPart1 = 0;
            long outputPart2 = 0;

            int currentX = 0;
            int currentY = 0;

            MinMax minMax = new MinMax();

            Map<String,String> gameBoard = new HashMap<>();
            gameBoard.put("0,0", "000000");
            while (line != null) {
                Matcher match = pattern.matcher(line);

                if (match.matches()) {
                    String direction = match.group(1);
                    int number = Integer.parseInt(match.group(2));
                    String colour = match.group(3);
                    int yOffset = direction.equals("D") ? -1 : direction.equals("U") ? 1 : 0;
                    int xOffset = direction.equals("L") ? -1 : direction.equals("R") ? 1 : 0;

                    for (int i = 0; i < number; i++)
                    {
                        currentY += yOffset;
                        currentX += xOffset;
                        minMax.updateMinMax(currentX, currentY);
                        gameBoard.put(String.format("%s,%s", currentX, currentY), colour);
                    }
                }
                else {
                    throw new Exception("No match: " + line);
                }

                line = br.readLine();
            }

            outputGameBoard(gameBoard, minMax);

            floodFillGameBoard(gameBoard, minMax);

            outputGameBoard(gameBoard, minMax);

            outputPart1 = gameBoard.size();

            System.out.println(String.format("Output Part 1: %d", outputPart1));
            System.out.println(String.format("Output Part 2: %d", outputPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static void floodFillGameBoard(Map<String, String> gameBoard, MinMax minMax)
    {
        Set<String> outside = new HashSet<>();
        for (int y = minMax.minY; y <= minMax.maxY; y++) {
            for (int x = minMax.minX; x <= minMax.maxX; x++) {
                String s = String.format("%d,%d", x, y);
                if (!outside.contains(s) && !gameBoard.containsKey(s))
                {
                    // not tested yet.
                    Set<String> tested = new HashSet<>();
                    Queue<Coordinate> nextToTest = new LinkedList<>();
                    boolean o = false;
                    nextToTest.add(new Coordinate(x,y));
                    tested.add(s);
                    while (nextToTest.size() > 0) {
                        Coordinate c = nextToTest.remove();
                        for (int newX = c.X - 1; newX <= c.X + 1; newX++) {
                            for (int newY = c.Y - 1; newY <= c.Y + 1; newY++) {
                                if ((newX < minMax.minX)
                                        || (newY < minMax.minY)
                                        || (newX > minMax.maxX)
                                        || (newY > minMax.maxY)) {
                                    o = true;
                                } else {
                                    Coordinate nc = new Coordinate(newX, newY);
                                    String st = nc.toString();
                                    if (!outside.contains(st)
                                        && !gameBoard.containsKey(st)
                                        && !tested.contains(st)) {
                                        tested.add(nc.toString());
                                        nextToTest.add(nc);
                                    }
                                }
                            }
                        }
                    }
                    // Flooded this
                    if (!o) {
                        for (String u : tested.stream().toList()) {
                            gameBoard.put(u, "0000000");
                        }
                    } else {
                        outside.addAll(tested);
                    }
                }
            }
        }
    }

    private static void outputGameBoard(Map<String, String> gameBoard, MinMax minMax)
    {
        System.out.println();
        for (int y = minMax.maxY; y >= minMax.minY; y--)
        {
            for (int x = minMax.minX; x <= minMax.maxX; x++)
            {
                String s = String.format("%d,%d", x, y);
                if (gameBoard.containsKey(s))
                {
                    System.out.print("#");
                }
                else
                {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}