package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static class Hailstone
    {
        long X;
        long Y;
        long Z;
        long vX;
        long vY;
        long vZ;
        Hailstone(String row) throws Exception
        {
            Matcher m = pattern.matcher(row);
            if (m.matches()) {
                X = Long.parseLong(m.group(1));
                Y = Long.parseLong(m.group(2));
                Z = Long.parseLong(m.group(3));
                vX = Long.parseLong(m.group(4));
                vY = Long.parseLong(m.group(5));
                vZ = Long.parseLong(m.group(6));
            }
            else {
                throw new Exception(String.format("No match for : %s", row));
            }
        }

        static class Coordinate {
            float X;
            float Y;
        }

        boolean intersectsInTheXYPlane(Hailstone other) {
            try {
                Coordinate c = intersect(X, Y, X + vX, Y + vY,
                                         other.X, other.Y, other.X + other.vX, other.Y + other.vY);

                System.out.printf("Hailstones collide at %.3f,%.3f ", c.X, c.Y);

                // If x is outside the limit
                if ((c.X > upperBoundary) ||
                    (c.X < lowerBoundary) ||
                    (c.Y > upperBoundary) ||
                    (c.Y < lowerBoundary))
                {
                    System.out.println(": Outside");
                    return false;
                }

                // Check when the collision occurred
                float t = ((float)c.X - (float)this.X) / (float)this.vX;
                float t1 = ((float)c.X - (float)other.X) / (float)other.vX;

                if (t < 0 || t1 < 0)
                {
                    System.out.println(": In the Past");
                    return false;
                }

                System.out.println();
                return true;
            } catch (Exception ex) {
                System.out.printf("Hailstones don't collide%n");
                return false;
            }
        }

        private Coordinate intersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4)
                throws Exception
        {

            // Check if none of the lines are of length 0
            if ((x1 == x2 && y1 == y2) || (x3 == x4 && y3 == y4)) {
                throw new Exception("Lines are of length 0");
            }

            long denominator = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

            // Lines are parallel
            if (denominator == 0) {
                System.out.print("Parallel: ");
                throw new Exception("Parallel Lines");
            }

            float ua = (float)((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / (float)denominator;
            //float ub = (float)((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / (float)denominator;

            // Return an object with the x and y coordinates of the intersection
            Coordinate c = new Coordinate();
            c.X = (float)x1 + ua * (float)(x2 - x1);
            c.Y = (float)y1 + ua * (float)(y2 - y1);

            return c;
        }
    }

    static Pattern pattern = Pattern.compile("^(\\d*),\\s*(\\d*),\\s*(\\d*)\\s*@\\s*(-?\\d*),\\s*(-?\\d*),\\s*(-?\\d*)$");
    static long lowerBoundary = 200000000000000L;
    static long upperBoundary = 400000000000000L;

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day24/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            ArrayList<Hailstone> hailstones = new ArrayList<>();

            String line = br.readLine();

            while (line != null) {
                hailstones.add(new Hailstone(line));
                line = br.readLine();
            }

            for (int i = 0; i < hailstones.size() - 1; i++)
            {
                for (int j = i + 1; j < hailstones.size(); j++)
                {
                    if (hailstones.get(i).intersectsInTheXYPlane(hailstones.get(j)))
                    {
                        outputPart1++;
                    }
                }
            }

            System.out.printf("Output Part 1: %d%n", outputPart1);

            // For part 2 - no idea!

            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }

    }
}