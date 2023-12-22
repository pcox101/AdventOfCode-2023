package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    private static class Coordinate
    {
        public int X;
        public int Y;
        public int Z;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return X == that.X && Y == that.Y && Z == that.Z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(X, Y, Z);
        }

        public Coordinate clone()
        {
            Coordinate c = new Coordinate();
            c.X = this.X;
            c.Y = this.Y;
            c.Z = this.Z;
            return c;
        }

        public String toString()
        {
            return String.format("%d,%d,%d", X, Y, Z);
        }
    }

    private static class Brick
    {

        Coordinate startCoordinate;
        Coordinate endCoordinate;

        Integer reference;
        public Brick clone()
        {
            Brick b = new Brick();
            b.startCoordinate = this.startCoordinate.clone();
            b.endCoordinate = this.endCoordinate.clone();
            b.reference = this.reference;
            return b;
        }

        public boolean isIntersecting(Brick other)
        {
            if (Math.max(this.startCoordinate.X, other.startCoordinate.X)
                    > Math.min(this.endCoordinate.X, other.endCoordinate.X))
                return false;

            if (Math.max(this.startCoordinate.Y, other.startCoordinate.Y)
                    > Math.min(this.endCoordinate.Y, other.endCoordinate.Y))
                return false;

            if (Math.max(this.startCoordinate.Z, other.startCoordinate.Z)
                    > Math.min(this.endCoordinate.Z, other.endCoordinate.Z))
                return false;

            return true;
        }

        public String toString()
        {
            return String.format("%d - %s~%s",
                    reference,
                    startCoordinate.toString(),
                    endCoordinate.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Brick brick = (Brick) o;
            return reference.equals(brick.reference);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reference);
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day22/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            List<Brick> bricks = new ArrayList<>();

            String line = br.readLine();
            int counter = 0;
            while (line != null) {
                String[] split1 = line.split("~");
                String[] startSplit = split1[0].split(",");
                String[] endSplit = split1[1].split(",");

                Coordinate startCoordinate = new Coordinate();
                startCoordinate.X = Integer.parseInt(startSplit[0]);
                startCoordinate.Y = Integer.parseInt(startSplit[1]);
                startCoordinate.Z = Integer.parseInt(startSplit[2]);

                Coordinate endCoordinate = new Coordinate();
                endCoordinate.X = Integer.parseInt(endSplit[0]);
                endCoordinate.Y = Integer.parseInt(endSplit[1]);
                endCoordinate.Z = Integer.parseInt(endSplit[2]);

                Brick brick = new Brick();
                brick.startCoordinate = startCoordinate;
                brick.endCoordinate = endCoordinate;
                brick.reference = counter;

                bricks.add(brick);

                counter++;
                line = br.readLine();
            }

            // Settle the bricks
            settleBricks(bricks);

            // Build our brick trees
            Map<Brick, List<Brick>> supportingBricks = new HashMap<>();
            Map<Brick, List<Brick>> supportedBricks = new HashMap<>();

            for (Brick b: bricks) {
                supportingBricks.put(b, new ArrayList<>());
                supportedBricks.put(b, new ArrayList<>());
            }

            for (Brick supportedBrick: bricks) {
                for (Brick supportingBrick : bricks) {
                    if (!supportingBrick.equals(supportedBrick)) {
                        Brick newb = supportingBrick.clone();
                        newb.startCoordinate.Z = supportingBrick.startCoordinate.Z - 1;
                        newb.endCoordinate.Z = supportingBrick.endCoordinate.Z - 1;

                        if (newb.isIntersecting(supportedBrick)) {
                            supportingBricks.get(supportedBrick).add(supportingBrick);
                            supportedBricks.get(supportingBrick).add(supportedBrick);
                        }
                    }
                }
            }

            outputPart1 = calculatePart1(bricks, supportingBricks, supportedBricks);

            outputPart2 = calculatePart2(bricks, supportingBricks, supportedBricks);

            System.out.printf("Output Part 1: %d%n", outputPart1);
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static void settleBricks(List<Brick> bricks)
    {
        // Settle the bricks (which means moving them all downwards until
        // none are moving
        long counter = 0;
        boolean anyBricksMoved = true;
        while (anyBricksMoved)
        {
            System.out.printf("Settling loop %d%n", counter);
            counter++;

            anyBricksMoved = false;

            for (Brick b: bricks)
            {
                if ((b.startCoordinate.Z != 1) && (b.endCoordinate.Z != 1))
                {
                    boolean canMove = true;
                    for (Brick o: bricks)
                    {
                        if (!o.equals(b))
                        {
                            Brick newb = b.clone();
                            newb.startCoordinate.Z = b.startCoordinate.Z - 1;
                            newb.endCoordinate.Z = b.endCoordinate.Z - 1;
                            if (newb.isIntersecting(o)) {
                                canMove = false;
                                break;
                            }
                        }
                    }
                    if (canMove)
                    {
                        anyBricksMoved = true;
                        b.startCoordinate.Z -= 1;
                        b.endCoordinate.Z -= 1;
                    }
                }
            }
        }
    }

    private static long calculatePart2(List<Brick> bricks, Map<Brick, List<Brick>> supportingBricks, Map<Brick, List<Brick>> supportedBricks) {
        System.out.println("Calculating part 1");
        long totalNumberOfFallingBricks = 0;

        for (Brick brickToDisintegrate : bricks) {
            Set<Brick> fallingBrickList = new HashSet<>();
            getNumberOfBricksImpacted(brickToDisintegrate, supportingBricks, supportedBricks, fallingBrickList);
            totalNumberOfFallingBricks += fallingBrickList.size() - 1;
        }

        return totalNumberOfFallingBricks;
    }

    private static void getNumberOfBricksImpacted(Brick brickToDisintegrate,
                                                        Map<Brick, List<Brick>> supportingBricks,
                                                        Map<Brick, List<Brick>> supportedBricks,
                                                        Set<Brick> fallingBrickList)
    {
        fallingBrickList.add(brickToDisintegrate);

        // If we disintegrated this brick, how many bricks above would fall?
        for (Brick supportedBrick : supportingBricks.get(brickToDisintegrate)) {
            // Look at the bricks supported by this brick, would they fall?
            List<Brick> siblingBricks = new ArrayList<Brick>(supportedBricks.get(supportedBrick));
            // Remove any bricks that we've identified will fall (including this one)
            // from the set of siblings
            siblingBricks.removeAll(fallingBrickList);
            // If there's nothing supporting this brick, it too shall fall
            if (siblingBricks.size() == 0) {
                fallingBrickList.add(supportedBrick);
                //System.out.printf("Brick %d would fall.%n", supportedBrick.reference);
                getNumberOfBricksImpacted(supportedBrick,
                        supportingBricks,
                        supportedBricks,
                        fallingBrickList);
            }
        }

        //System.out.printf("Brick %d would cause %d to fall%n", brickToDisintegrate.reference, fallingBrickList.size() - 1);

    }

    private static long calculatePart1(List<Brick> bricks, Map<Brick, List<Brick>> supportingBricks, Map<Brick, List<Brick>> supportedBricks)
    {
        System.out.println("Calculating part 2");

        long numberOfBricks = 0;

        for (Brick brickToDisintegrate: bricks) {
            //System.out.printf("Brick %d is supporting bricks:", brickToDisintegrate.reference);
            boolean canDisintegrate = true;
            if (supportingBricks.get(brickToDisintegrate).size() > 0) {
                for (Brick supportedBrick : supportingBricks.get(brickToDisintegrate)) {
                    //System.out.printf("%d (%d),", supportedBrick.reference, supportedBricks.get(supportedBrick).size());
                    if (supportedBricks.get(supportedBrick).size() == 1) {

                        canDisintegrate = false;
                    }
                }
            }

            if (canDisintegrate)
            {
                numberOfBricks++;
            }
            //System.out.println();
        }

        return numberOfBricks;
    }
}