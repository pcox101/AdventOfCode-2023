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
        Hailstone()
        {
            X = 0;
            Y = 0;
            Z = 0;
            vX = 0;
            vY = 0;
            vZ = 0;
        }
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

                //System.out.printf("Hailstones collide at %.3f,%.3f ", c.X, c.Y);

                // If x is outside the limit
                if ((c.X > upperBoundary) ||
                    (c.X < lowerBoundary) ||
                    (c.Y > upperBoundary) ||
                    (c.Y < lowerBoundary))
                {
                    //System.out.println(": Outside");
                    return false;
                }

                // Check when the collision occurred
                float t = ((float)c.X - (float)this.X) / (float)this.vX;
                float t1 = ((float)c.X - (float)other.X) / (float)other.vX;

                if (t < 0 || t1 < 0)
                {
                    //System.out.println(": In the Past");
                    return false;
                }

                System.out.println();
                return true;
            } catch (Exception ex) {
                //System.out.printf("Hailstones don't collide%n");
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

            // Part 2. We need to:
            // Identify enough equations for intersecting lines
            // and solve them using gaussian elimination
            outputPart2 = calculatePart2(hailstones);

            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculatePart2(ArrayList<Hailstone> hailstones)
    {
        // Let the rock be:
        // rx, ry, rz, t(vrx, vry, vrz)
        // The intersection point with a single hailstone
        // h1x, h1y, h1z, t(vh1z, vh1y, vh1z)
        // Is solved by the following equations:
        // rx + (t * vrx) = h1x + (t * vh1x)
        // ry + (t * vry) = h1y + (t * vh1y)
        // rz + (t * vrz) = h1z + (t * vh1z)

        // Solve each of them for t
        // t * vrx - t * vh1x = h1x - rx
        // t = (h1x - rx) / (vrx - vh1x)
        // t = (h1y - ry) / (vry - vh1y)
        // t = (h1z - rz) / (vrz - vh1z)

        // Equate two of them and cross multiply
        // (h1x - rx) / (vrx - vh1x) = (h1y - ry) / (vry - vh1y)
        // (h1x - rx) * (vry - vh1y) = (h1y - ry) * (vrx - vh1x)
        // Then expand
        // h1x * vry - h1x * vh1y - rx * vry + rx * vh1y = h1y * vrx - h1y * vh1x - ry * vrx + ry * vh1x
        // Move anything that is a constant in the frame of the rock
        // to the left and anything that isn't to the right
        // ry * vrx - rx * vry = h1y * vrx - h1y * vh1x + ry * vh1x - h1x * vry + h1x * vh1y - rx * vh1y

        // Now consider the above for a second hailstone and set them equal
        // h1y * vrx - h1y * vh1x + ry * vh1x - h1x * vry + h1x * vh1y - rx * vh1y
        //     = h2y * vrx - h2y * vh2x + ry * vh2x - h2x * vry + h2x * vh2y - rx * vh2y
        // Simplify...
        // (h1y - h2y) vrx + (vh1x - vh2x) ry + (h2x - h1x) vry + (vh2y - vh1y) rx
        //     = h1y * vh1x - h1x * vh1y - h2y * vh2x + h2x * vh2y

        // Do the same for X-Z and Y-Z gives
        // (h1z - h2z) vrx + (vh1x - vh2x) rz + (h2x - h1x) vrz + (vh2z - vh1z) rx
        //     = h1z * vh1x - h1x * vh1z - h2z * vh2x + h2x * vh2z
        // (h1z - h2z) vry + (vh1y - vh2y) rz + (h2y - h1y) vrz + (vh2z - vh1z) ry
        //     = h1z * vh1y - h1y * vh1z - h2z * vh2y + h2y * vh2z

        // 3 equations, 6 unknowns. With 3 hailstones we can get all the unknowns

        // So build an array of the following, with 6 rows,
        // where a, b, c, etc. are calculated from the above
        // a * rx , b * ry, c * rz, d * vrx, e * vry, f * vrz, const
        double[][] matrix = populateMatrix(hailstones);
        double[] constants = populateConstants(hailstones);

        double[] solution = solveMatrix(matrix, constants);

        return (long)(Math.round(solution[0]) + Math.round(solution[1]) + Math.round(solution[2]));

        // Keeping this as it was my first attempt, but didn't work as there were
        // no parallel lines in the input set, and calculating for intersections
        // may not have resulted in any either, so switched to using simultaneous
        // equations and Guassian elimination
        // There was plenty of work here though, so didn't feel like just deleting
        // it!
/*        Hailstone p1 = null;
        Hailstone p2 = null;

        // Scan the set for parallel lines
        for (int i = 0; i < hailstones.size() - 1; i++)
        {
            Hailstone h1 = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++)
            {
                Hailstone h2 = hailstones.get(j);

                float xFactor = (float)h1.vX / (float)h2.vX;
                float yFactor = (float)h1.vY / (float)h2.vY;
                float zFactor = (float)h1.vZ / (float)h2.vZ;

                if ((xFactor == yFactor) && (xFactor == zFactor))
                {
                    p1 = h1;
                    p2 = h2;
                    System.out.printf("Found parallel lines, lines %d - %d%n", i, j);
                    break;
                }

                // If not parallel, see if they intersect (also valid for a plane)



            }
            if (p1 != null)
            {
                break;
            }
        }

        // Now we have 2 parallel lines, we can construct the plane

        // parametric equation =
        // (x,y,z) = (x1, y1, z1) + t(xv1, yv1, zv1) + u(x1-x2, y1-y2, z1-z2)

        // Cartesian equation is the cross product of the two vectors
        // i.e (ix + jy + kz = l, where
        //   i = (yv1 * (z1-z2)) - (zv1 * (y1-y2))
        //   j = (zv1 * (x1-x2)) - (xv1 * (z1-z2))
        //   k = (xv1 * (y1-y2)) - (yv1 * (x1-x2))
        //   l = i * x1 + j * y1 + k * z1

        long i = (p1.vY * (p1.Z - p2.Z)) - (p1.vZ * (p1.Y - p2.Y));
        long j = (p1.vZ * (p1.X - p2.X)) - (p1.vX * (p1.Z - p2.Z));
        long k = (p1.vX * (p1.Y - p2.Y)) - (p1.vY * (p1.X - p2.X));
        long l = (i * p1.X) + (j * p1.Y) + (k * p1.Z);

        // Pick 2 more lines that are not on the plane
        // (i.e. not parallel to the 2 we picked earlier...
        Hailstone perp1 = null;
        Hailstone perp2 = null;
        for (int m = 0; m < hailstones.size(); m++) {
            Hailstone h = hailstones.get(m);

            float xFactor = h.vX / p1.vX;
            float yFactor = h.vY / p1.vY;
            float zFactor = h.vZ / p1.vZ;

            if ((xFactor != yFactor) || (xFactor != zFactor)) {
                if (perp1 == null)
                    perp1 = h;
                else
                {
                    perp2 = h;
                    break;
                }
            }
        }

        // These two points intersect with the plane at two points along
        // the rock's trajectory
        // This will give us 2 values for t while we're at it.
        // To solve for t1, t2...
        // i(x + vx * t) + j(y + vy * t) + k(z + vz * t) = l
        // (i * x) + (i * vx  * t) + (j * y) + (j * vy * t) + (k * z) + (k * vz * t) = l
        // ((i * vx) + (j * vy) + (k * vz)) * t = l - (i * x) - (j * y) - (k * z)
        // t = (l - (i * x) - (j * y) - (k * z)) / ((i * vx) + (j * vy) + (k * vz))


        long num1 = l - (i * perp1.X) - (j * perp1.Y) - (k * perp1.Z);
        long den1 = (i * perp1.vX) + (j * perp1.vY) + (k * perp1.vZ);
        long num2 = l - (i * perp2.X) - (j * perp2.Y) - (k * perp2.Z);
        long den2 = (i * perp2.vX) + (j * perp2.vY) + (k * perp2.vZ);

        float t1 = (float)num1 / (float)den1;
        float t2 = (float)num2 / (float)den2;

        // Substitute into the equation of our line to give our X/Y/Z coordinates
        float x1 = perp1.X + (perp1.vX * t1);
        float y1 = perp1.Y + (perp1.vY * t1);;
        float z1 = perp1.Z + (perp1.vZ * t1);
        float x2 = perp2.X + (perp2.vX * t2);
        float y2 = perp2.Y + (perp2.vY * t2);
        float z2 = perp2.Z + (perp2.vZ * t2);

        // Find the equation of the line passing through these two points...
        // We know it passes x1,y1,z1 at t=t1 and x2,y2,z2 at t=t2

        // So assume a line that passes x2,y2,z2 at time t=0 and x1,y1,z1 at time t=1
        float rX = x2;
        float rY = y2;
        float rZ = z2;

        float rvX = x1 - x2;
        float rvY = y1 - y2;
        float rvZ = z1 - z2;

        // Now adjust so that the line passes at the right times
        float vectorFactor = 1 / (t2 - t1);
        rvX = rvX * vectorFactor;
        rvY = rvY * vectorFactor;
        rvZ = rvZ * vectorFactor;

        rX = rX + (rvX * t2);
        rY = rY + (rvY * t2);
        rZ = rZ + (rvZ * t2);

        return (long)(rX + rY + rZ);*/
    }

    // stolen from https://introcs.cs.princeton.edu/java/95linear/GaussianElimination.java.html
    private static double[] solveMatrix(double[][] A, double[] b)
    {
        final double EPSILON = 1e-10;

        // Gaussian elimination with partial pivoting
        int n = b.length;

        for (int p = 0; p < n; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            // pivot within A and b
            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

    private static double[][] populateMatrix(List<Hailstone> hailstones)
    {
        double[][] result = new double[6][6];

        populateRow(result, 0, getCoefficientsXY(hailstones.get(0), hailstones.get(1)));
        populateRow(result, 1, getCoefficientsXZ(hailstones.get(0), hailstones.get(1)));
        populateRow(result, 2, getCoefficientsYZ(hailstones.get(0), hailstones.get(1)));
        populateRow(result, 3, getCoefficientsXY(hailstones.get(0), hailstones.get(2)));
        populateRow(result, 4, getCoefficientsXZ(hailstones.get(0), hailstones.get(2)));
        populateRow(result, 5, getCoefficientsYZ(hailstones.get(0), hailstones.get(2)));

        return result;
    }

    private static double[] populateConstants(List<Hailstone> hailstones)
    {
        double[] result = new double[6];
        Hailstone h0 = hailstones.get(0);
        Hailstone h1 = hailstones.get(1);
        Hailstone h2 = hailstones.get(2);

        // h1y * vh1x - h1x * vh1y - h2y * vh2x + h2x * vh2y
        // h1z * vh1x - h1x * vh1z - h2z * vh2x + h2x * vh2z
        // h1z * vh1y - h1y * vh1z - h2z * vh2y + h2y * vh2z

        result[0] = (h0.Y * h0.vX) - (h0.X * h0.vY) - (h1.Y * h1.vX) + (h1.X * h1.vY);
        result[1] = (h0.Z * h0.vX) - (h0.X * h0.vZ) - (h1.Z * h1.vX) + (h1.X * h1.vZ);
        result[2] = (h0.Z * h0.vY) - (h0.Y * h0.vZ) - (h1.Z * h1.vY) + (h1.Y * h1.vZ);
        result[3] = (h0.Y * h0.vX) - (h0.X * h0.vY) - (h2.Y * h2.vX) + (h2.X * h2.vY);
        result[4] = (h0.Z * h0.vX) - (h0.X * h0.vZ) - (h2.Z * h2.vX) + (h2.X * h2.vZ);
        result[5] = (h0.Z * h0.vY) - (h0.Y * h0.vZ) - (h2.Z * h2.vY) + (h2.Y * h2.vZ);

        return result;
    }

    private static void populateRow(double[][] matrix, int rowId, double[] coeffs)
    {
        for (int i = 0; i < 6; i++)
        {
            matrix[rowId][i] = coeffs[i];
        }
    }

    private static double[] getCoefficientsXZ(Hailstone h1, Hailstone h2)
    {
        // (h1z - h2z) vrx + (vh1x - vh2x) rz + (h2x - h1x) vrz + (vh2z - vh1z) rx
        //     = h1z * vh1x - h1x * vh1z - h2z * vh2x + h2x * vh2z
        double[] result = new double[7];
        result[0] = h2.vZ - h1.vZ;
        result[1] = 0;
        result[2] = h1.vX - h2.vX;
        result[3] = h1.Z - h2.Z;
        result[4] = 0;
        result[5] = h2.X - h1.X;

        return result;
    }

    private static double[] getCoefficientsYZ(Hailstone h1, Hailstone h2)
    {
        // (h1z - h2z) vry + (vh1y - vh2y) rz + (h2y - h1y) vrz + (vh2z - vh1z) ry
        //     = h1z * vh1y - h1y * vh1z - h2z * vh2y + h2y * vh2z
        double[] result = new double[7];
        result[0] = 0;
        result[1] = h2.vZ - h1.vZ;
        result[2] = h1.vY - h2.vY;
        result[3] = 0;
        result[4] = h1.Z - h2.Z;
        result[5] = h2.Y - h1.Y;

        return result;
    }

    private static double[] getCoefficientsXY(Hailstone h1, Hailstone h2)
    {
        // (h1y - h2y) vrx + (vh1x - vh2x) ry + (h2x - h1x) vry + (vh2y - vh1y) rx
        //     = h1y * vh1x - h1x * vh1y - h2y * vh2x + h2x * vh2y
        double[] result = new double[7];
        result[0] = h2.vY - h1.vY;
        result[1] = h1.vX - h2.vX;
        result[2] = 0;
        result[3] = h1.Y - h2.Y;
        result[4] = h2.X - h1.X;
        result[5] = 0;

        return result;
    }
}