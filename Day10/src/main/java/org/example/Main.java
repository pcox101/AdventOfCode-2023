package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {


    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day10/src/main/resources/input.txt"));
            List<String> puzzleInput = new ArrayList<>();

            String line = br.readLine();
            while (line != null) {
                puzzleInput.add(line);
                line = br.readLine();
            }
            int width = puzzleInput.get(0).length();
            int height = puzzleInput.size();

            Character[][] gameBoard = new Character[width][height];
            Coordinate starting = new Coordinate();
            for (int y = 0; y < height; y++) {
                String thisLine = puzzleInput.get(y);
                for (int x = 0; x < width; x++) {
                    Character charAt = thisLine.charAt(x);
                    if (charAt.equals('S')) {
                        starting.X = x;
                        starting.Y = y;
                    }
                    gameBoard[x][y] = charAt;
                }
            }

            // Now given the starting location, work out what our next two
            // locations could be
            Coordinate[] next = new Coordinate[2];
            gameBoard[starting.X][starting.Y] = populateDirectionsFromStart(gameBoard, starting, next, height, width);


            // Now play our way through the game
            boolean done = false;
            int numberOfSteps = 0;

            Set<String> pipeLocations = new HashSet<>();
            pipeLocations.add(String.format("%d_%d", starting.X, starting.Y));
            while (!done) {
                //outputGameBoard(gameBoard, height, width, next);
                done = movePositions(gameBoard, height, width, next);
                pipeLocations.add(String.format("%d_%d", next[0].X, next[0].Y));
                pipeLocations.add(String.format("%d_%d", next[1].X, next[1].Y));

                numberOfSteps++;
            }

            System.out.println(String.format("It took %d steps", numberOfSteps));

            // Find the area inside the loop
            int areaInsideLoop = getAreaInsideLoop(gameBoard, height, width, pipeLocations);
            System.out.println(String.format("There are %d spaces inside the loop", areaInsideLoop));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static int getAreaInsideLoop(Character[][] gameBoard, int height, int width, Set<String> pipeLocations) {
        Set<String> possibleInsideLocations = new HashSet<>();

        // Left to right
        for (int y = 0; y < height; y++) {
            boolean insideArea = false;
            Character startOfPipe = ' ';
            for (int x = 0; x < width; x++) {
                String position = String.format("%d_%d", x, y);
                // Do we need to consider it
                if (pipeLocations.contains(position)) {
                    if (gameBoard[x][y].equals('|')) {
                        insideArea = !insideArea;
                    } else if (gameBoard[x][y].equals('-')) {
                        // Nothing changes
                    } else if (!gameBoard[x][y].equals('.')) {
                        if (startOfPipe == ' ') {
                            startOfPipe = gameBoard[x][y];
                        } else {
                            // leaving a pipe, do we stay inside?
                            if (startOfPipe.equals('F')) {
                                if (gameBoard[x][y].equals('J')) {
                                    // pipe continues, moving inside to outside or vice versa
                                    insideArea = !insideArea;
                                    startOfPipe = ' ';
                                } else if (gameBoard[x][y].equals('7')) {
                                    // pipe changes direction, no change
                                    startOfPipe = ' ';
                                }
                            } else if (startOfPipe.equals('L')) {
                                if (gameBoard[x][y].equals('J')) {
                                    // pipe changes direction, no change
                                    startOfPipe = ' ';
                                } else if (gameBoard[x][y].equals('7')) {
                                    // pipe continues, moving inside to outside or vice versa
                                    insideArea = !insideArea;
                                    startOfPipe = ' ';
                                }
                            }
                        }
                    }
                } else {
                    if (insideArea)
                        possibleInsideLocations.add(position);
                }
            }
        }

        outputGameBoard(gameBoard, height, width, pipeLocations, possibleInsideLocations);
        return possibleInsideLocations.size();
    }

    private static boolean movePositions(Character[][] gameBoard, int height, int width, Coordinate[] positions) throws
            Exception {
        // Move each in turn
        for (int i = 0; i < 2; i++) {
            Coordinate workingCoord = positions[i];
            //gameBoard[workingCoord.X][workingCoord.Y] = '*';

            switch (workingCoord.direction) {
                case Up:
                    if (workingCoord.Y > 0) {
                        Coordinate newPosition = new Coordinate();
                        newPosition.X = workingCoord.X;
                        newPosition.Y = workingCoord.Y - 1;

                        Character moveChar = gameBoard[newPosition.X][newPosition.Y];
                        if (moveChar.equals('|')) {
                            newPosition.direction = Coordinate.Direction.Up;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('F')) {
                            newPosition.direction = Coordinate.Direction.Right;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('7')) {
                            newPosition.direction = Coordinate.Direction.Left;
                            positions[i] = newPosition;
                            continue;
                        }
                    }
                    break;
                case Right:
                    if (workingCoord.X < width) {
                        Coordinate newPosition = new Coordinate();
                        newPosition.X = workingCoord.X + 1;
                        newPosition.Y = workingCoord.Y;

                        Character moveChar = gameBoard[newPosition.X][newPosition.Y];
                        if (moveChar.equals('-')) {
                            newPosition.direction = Coordinate.Direction.Right;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('J')) {
                            newPosition.direction = Coordinate.Direction.Up;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('7')) {
                            newPosition.direction = Coordinate.Direction.Down;
                            positions[i] = newPosition;
                            continue;
                        }
                    }
                    break;
                case Down:
                    if (workingCoord.Y < height) {
                        Coordinate newPosition = new Coordinate();
                        newPosition.X = workingCoord.X;
                        newPosition.Y = workingCoord.Y + 1;

                        Character moveChar = gameBoard[newPosition.X][newPosition.Y];
                        if (moveChar.equals('|')) {
                            newPosition.direction = Coordinate.Direction.Down;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('J')) {
                            newPosition.direction = Coordinate.Direction.Left;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('L')) {
                            newPosition.direction = Coordinate.Direction.Right;
                            positions[i] = newPosition;
                            continue;
                        }
                    }
                    break;
                case Left:
                    if (workingCoord.X > 0) {
                        Coordinate newPosition = new Coordinate();
                        newPosition.X = workingCoord.X - 1;
                        newPosition.Y = workingCoord.Y;

                        Character moveChar = gameBoard[newPosition.X][newPosition.Y];
                        if (moveChar.equals('-')) {
                            newPosition.direction = Coordinate.Direction.Left;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('L')) {
                            newPosition.direction = Coordinate.Direction.Up;
                            positions[i] = newPosition;
                            continue;
                        }
                        if (moveChar.equals('F')) {
                            newPosition.direction = Coordinate.Direction.Down;
                            positions[i] = newPosition;
                            continue;
                        }
                    }
                    break;
            }
            outputGameBoard(gameBoard, height, width, positions);
            throw new Exception(String.format("Couldn't find location to move to from (%d,%d) direction %s", workingCoord.X, workingCoord.Y, workingCoord.direction));
        }

        if (positions[0].equals(positions[1])) {
            return true;
        }
        return false;
    }

    private static Character populateDirectionsFromStart(Character[][] gameBoard, Coordinate starting, Coordinate[] next,
                                                         int height, int width) throws Exception {
        int counter = 0;
        if (starting.Y > 0) {
            Character above = gameBoard[starting.X][starting.Y - 1];
            if (above.equals('|') || above.equals('7') || above.equals('F')) {
                next[counter] = new Coordinate();
                next[counter].X = starting.X;
                next[counter].Y = starting.Y;
                next[counter].direction = Coordinate.Direction.Up;
                counter++;
            }
        }
        if (starting.X < width) {
            Character right = gameBoard[starting.X + 1][starting.Y];
            if (right.equals('-') || right.equals('7') || right.equals('J')) {
                next[counter] = new Coordinate();
                next[counter].X = starting.X;
                next[counter].Y = starting.Y;
                next[counter].direction = Coordinate.Direction.Right;
                counter++;
            }
        }
        if (starting.Y < height) {
            Character below = gameBoard[starting.X][starting.Y + 1];
            if (below.equals('|') || below.equals('J') || below.equals('L')) {
                next[counter] = new Coordinate();
                next[counter].X = starting.X;
                next[counter].Y = starting.Y;
                next[counter].direction = Coordinate.Direction.Down;
                counter++;
            }
        }
        if (starting.X > 0) {
            Character left = gameBoard[starting.X - 1][starting.Y];
            if (left.equals('-') || left.equals('F') || left.equals('L')) {
                next[counter] = new Coordinate();
                next[counter].X = starting.X;
                next[counter].Y = starting.Y;
                next[counter].direction = Coordinate.Direction.Left;
                counter++;
            }
        }

        if (counter != 2)
            throw new Exception("More or less than 2 positions to move from start");

        // What is our starting pipe type?
        switch (next[0].direction) {
            case Up:
                switch (next[1].direction) {
                    case Down:
                        return '|';
                    case Left:
                        return 'J';
                    case Right:
                        return 'L';
                }
            case Left:
                switch (next[1].direction) {
                    case Up:
                        return 'J';
                    case Right:
                        return '-';
                    case Down:
                        return '7';
                }
            case Down:
                switch (next[1].direction) {
                    case Up:
                        return '|';
                    case Right:
                        return 'F';
                    case Left:
                        return '7';
                }
            case Right:
                switch (next[1].direction) {
                    case Up:
                        return 'L';
                    case Left:
                        return '-';
                    case Down:
                        return 'F';
                }
        }
        return 'S';
    }

    private static void outputGameBoard(Character[][] gameBoard, int height, int width, Coordinate[] positions) {
        System.out.println("Game Board : ");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isCurrentPosition = false;
                for (int i = 0; i < 2; i++) {
                    if ((x == positions[i].X) && (y == positions[i].Y))
                        isCurrentPosition = true;
                }
                if (isCurrentPosition) System.out.print("\u001B[31m");
                System.out.print(gameBoard[x][y]);
                if (isCurrentPosition) System.out.print("\u001B[0m");
            }
            System.out.println();
        }
    }

    private static void outputGameBoard(Character[][] gameBoard, int height, int width,
                                        Set<String> pipeLocations,
                                        Set<String> insideLocations) {
        System.out.println("Game Board : ");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String thisLocation = String.format("%d_%d", x, y);
                boolean isInside = insideLocations.contains(thisLocation);
                boolean isPipe = pipeLocations.contains(thisLocation);
                if (isInside) System.out.print("\u001B[31m");
                if (isPipe) System.out.print("\u001B[33m");
                System.out.print(gameBoard[x][y]);
                if (isInside || isPipe) System.out.print("\u001B[0m");
            }
            System.out.println();
        }
    }
}

