package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    static class Coordinate
    {
        int Row;
        int Column;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return Row == that.Row && Column == that.Column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Row, Column);
        }

        public Coordinate(int row, int column)
        {
            Row = row;
            Column = column;
        }
        public Coordinate clone()
        {
            return new Coordinate(Row, Column);
        }
    }

    static class Path
    {
        Coordinate currentCoordinate;
        Set<Coordinate> visitedCoordinates = new HashSet<>();

        public Path(Coordinate coordinate)
        {
            currentCoordinate = coordinate.clone();
        }

        public Path clone()
        {
            Path p = new Path(currentCoordinate);
            p.visitedCoordinates = new HashSet<>(this.visitedCoordinates);
            return p;
        }
    }
    static class Graph
    {
        Map<Coordinate,Node> Nodes = new HashMap<>();
        Node StartNode;
        Node EndNode;
    }

    static class Node
    {
        Coordinate NodeLocation;
        Map<Node, Integer> LinkedNodes = new HashMap<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(NodeLocation, node.NodeLocation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(NodeLocation);
        }

        public String toString()
        {
            return String.format("%d,%d (%d)",this.NodeLocation.Row, this.NodeLocation.Column, this.LinkedNodes.size());
        }
    }

    static class StackNodeEntry
    {
        Node CurrentNode;
        Set<Node> VisitedNodes = new HashSet<>();
        Integer TotalDistance = 0;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day23/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            String line = br.readLine();
            List<String> gb = new ArrayList<>();

            while (line != null) {
                gb.add(line);
                line = br.readLine();
            }

            char[][] gameBoardArray = getCharArrayGameBoard(gb);

            // Find the starting point
            Coordinate startingPoint = null;
            for (int column = 0; column < gameBoardArray[0].length; column++)
            {
                if (gameBoardArray[0][column] != '#')
                {
                    startingPoint = new Coordinate(0, column);
                    break;
                }
            }

            if (startingPoint == null)
            {
                throw new Exception("No starting point");
            }
            outputPart1 = getMaximumLength(gameBoardArray, startingPoint);
            System.out.printf("Output Part 1: %d%n", outputPart1);

            outputPart2 = getMaximumLengthPart2(gameBoardArray, startingPoint);
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long getMaximumLengthPart2(char[][] gameBoardArray, Coordinate startingPoint)
    {
        // Build our graph
        Graph graph = new Graph();
        Node startingNode = new Node();
        startingNode.NodeLocation = startingPoint;
        graph.Nodes.put(startingPoint, startingNode);
        graph.StartNode = startingNode;

        // DFS to identify all the nodes and distances between them
        Stack<Node> nextNodes = new Stack<>();
        nextNodes.add(startingNode);

        while (nextNodes.size() > 0)
        {
            Node n = nextNodes.pop();

            System.out.printf("Processing a node: %s%n", n.toString());

            // Now work our way down all the possible paths
            // from this node until we reach another node
            // This is done by DFS
            Stack<Path> possiblePaths = new Stack<>();
            possiblePaths.add(new Path(n.NodeLocation));

            while (possiblePaths.size() > 0) {
                Path p = possiblePaths.pop();

                // Can we move?
                boolean[] canMove = {
                        (p.currentCoordinate.Row != 0),
                        (p.currentCoordinate.Column != gameBoardArray[0].length - 1),
                        (p.currentCoordinate.Row != gameBoardArray.length - 1),
                        (p.currentCoordinate.Column != 0)};

                List<Path> nextPaths = new ArrayList<>();

                // N
                if (canMove[0]) {
                    Coordinate newPosition = new Coordinate(p.currentCoordinate.Row - 1, p.currentCoordinate.Column);
                    if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates)) {
                        Path newPath = p.clone();
                        newPath.currentCoordinate = newPosition;
                        newPath.visitedCoordinates.add(p.currentCoordinate);
                        nextPaths.add(newPath);
                    }
                }
                // E
                if (canMove[1]) {
                    Coordinate newPosition = new Coordinate(p.currentCoordinate.Row, p.currentCoordinate.Column + 1);
                    if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates)) {
                        Path newPath = p.clone();
                        newPath.currentCoordinate = newPosition;
                        newPath.visitedCoordinates.add(p.currentCoordinate);
                        nextPaths.add(newPath);
                    }
                }
                // S
                if (canMove[2]) {
                    Coordinate newPosition = new Coordinate(p.currentCoordinate.Row + 1, p.currentCoordinate.Column);
                    if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates)) {
                        Path newPath = p.clone();
                        newPath.currentCoordinate = newPosition;
                        newPath.visitedCoordinates.add(p.currentCoordinate);
                        nextPaths.add(newPath);
                    }
                }
                // W
                if (canMove[3]) {
                    Coordinate newPosition = new Coordinate(p.currentCoordinate.Row, p.currentCoordinate.Column - 1);
                    if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates)) {
                        Path newPath = p.clone();
                        newPath.currentCoordinate = newPosition;
                        newPath.visitedCoordinates.add(p.currentCoordinate);
                        nextPaths.add(newPath);
                    }
                }

                if (((nextPaths.size() > 1)
                        && (!n.NodeLocation.equals(p.currentCoordinate)))
                    || (p.currentCoordinate.Row == gameBoardArray.length - 1))
                {
                    // We have reached a node on this path
                    // (or the end of the map)
                    System.out.printf("Found a node: %s%n", p.currentCoordinate);
                    // Have we processed it already?
                    if (!graph.Nodes.containsKey(p.currentCoordinate)) {
                        Node node = new Node();
                        node.NodeLocation = p.currentCoordinate;
                        node.LinkedNodes.put(n, p.visitedCoordinates.size());
                        n.LinkedNodes.put(node, p.visitedCoordinates.size());
                        graph.Nodes.put(p.currentCoordinate, node);
                        // No need to reprocess the end node
                        if (!(p.currentCoordinate.Row == gameBoardArray.length - 1))
                            nextNodes.push(node);
                        else
                            graph.EndNode = node;
                    }
                    else
                    {
                        // Update the existing node's references, rather than
                        // using the newly created node...
                        Node node = graph.Nodes.get(p.currentCoordinate);
                        n.LinkedNodes.put(node, p.visitedCoordinates.size());
                        node.LinkedNodes.put(n, p.visitedCoordinates.size());
                    }
                }
                else
                {
                    // continue with the search
                    possiblePaths.addAll(nextPaths);
                }
            }
        }

        // We now have a graph.
        System.out.printf("We have a graph of %d nodes%n", graph.Nodes.size());

        // Compress the graph. Any node that has only 2 neighbours can be
        // removed
//        ArrayList<Node> nodesToRemove = new ArrayList<>();
//        for (Map.Entry<Coordinate,Node> entry: graph.Nodes)
//        {
//            if (entry.getValue().LinkedNodes.size() == 2)
//            {
//                // Directly link node 1 with node 2
//                int distanceSum = 0;
//                int counter = 0;
//                Node[] siblingNodes = new Node[2];
//                for (Map.Entry<Node, Integer> entry2: entry.getValue().LinkedNodes.entrySet()) {
//                    siblingNodes[counter] = entry2.getKey();
//                    distanceSum += entry2.getValue();
//                    counter++;
//                }
//
//                siblingNodes[0].LinkedNodes.remove(entry.getValue());
//                siblingNodes[1].LinkedNodes.remove(entry.getValue());
//
//                siblingNodes[0].LinkedNodes.put(siblingNodes[1], distanceSum);
//                siblingNodes[1].LinkedNodes.put(siblingNodes[0], distanceSum);
//
//                nodesToRemove.add(entry.getValue());
//            }
//        }
//        graph.Nodes.removeAll(nodesToRemove);

        System.out.printf("We now have a graph of %d nodes%n", graph.Nodes.size());

        // DFS through the map (may not be very fast)
        Stack<StackNodeEntry> nodesToProcess = new Stack<>();
        StackNodeEntry startEntry = new StackNodeEntry();
        startEntry.CurrentNode = graph.StartNode;
        startEntry.VisitedNodes.add(graph.StartNode);
        nodesToProcess.add(startEntry);

        long maximumLength = 0;
        while (nodesToProcess.size() != 0)
        {
            StackNodeEntry nodeToProcess = nodesToProcess.pop();

            // Is this the end node?
            if (nodeToProcess.CurrentNode.equals(graph.EndNode))
            {
                maximumLength = Math.max(maximumLength, nodeToProcess.TotalDistance);
                System.out.printf("Reached an end at %d. Max is %d.%n", nodeToProcess.TotalDistance, maximumLength);
            }

            for (Map.Entry<Node, Integer> entry: nodeToProcess.CurrentNode.LinkedNodes.entrySet())
            {
                if (!nodeToProcess.VisitedNodes.contains(entry.getKey()))
                {
                    StackNodeEntry nextEntry = new StackNodeEntry();
                    nextEntry.CurrentNode = entry.getKey();
                    nextEntry.VisitedNodes = new HashSet<>(nodeToProcess.VisitedNodes);
                    nextEntry.VisitedNodes.add(entry.getKey());
                    nextEntry.TotalDistance = nodeToProcess.TotalDistance + entry.getValue();
                    nodesToProcess.add(nextEntry);
                }
            }
        }

        return maximumLength;
    }

    private static long getMaximumLength(char[][] gameBoardArray, Coordinate startingPoint) {
        long maximumLength = 0;

        Stack<Path> possiblePaths = new Stack<>();
        possiblePaths.add(new Path(startingPoint));

        long counter = 0;
        while (possiblePaths.size() > 0)
        {
            Path p = possiblePaths.pop();

            // Is this on the finishing row?
            if (p.currentCoordinate.Row == gameBoardArray.length - 1)
            {
                System.out.printf("Finished a path. Length %d%n", p.visitedCoordinates.size());
                maximumLength = Math.max(p.visitedCoordinates.size(), maximumLength);
            }

            // Are we on a slope?
            char currentPosition = gameBoardArray[p.currentCoordinate.Row][p.currentCoordinate.Column];
            boolean[] canMove = {
                    (p.currentCoordinate.Row != 0),
                    (p.currentCoordinate.Column != gameBoardArray[0].length - 1),
                    (p.currentCoordinate.Row != gameBoardArray.length - 1),
                    (p.currentCoordinate.Column != 0)};
            switch (currentPosition) {
                case '^':
                    canMove[1] = false;
                    canMove[2] = false;
                    canMove[3] = false;
                    break;
                case '>':
                    canMove[0] = false;
                    canMove[2] = false;
                    canMove[3] = false;
                    break;
                case 'v':
                    canMove[0] = false;
                    canMove[1] = false;
                    canMove[3] = false;
                    break;
                case '<':
                    canMove[0] = false;
                    canMove[1] = false;
                    canMove[2] = false;
                    break;
                default:
                    break;
            }

            // N
            if (canMove[0]) {
                Coordinate newPosition = new Coordinate(p.currentCoordinate.Row - 1, p.currentCoordinate.Column);
                if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates))
                {
                    Path newPath = p.clone();
                    newPath.currentCoordinate = newPosition;
                    newPath.visitedCoordinates.add(p.currentCoordinate);
                    possiblePaths.add(newPath);
                }
            }
            // E
            if (canMove[1]) {
                Coordinate newPosition = new Coordinate(p.currentCoordinate.Row, p.currentCoordinate.Column + 1);
                if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates))
                {
                    Path newPath = p.clone();
                    newPath.currentCoordinate = newPosition;
                    newPath.visitedCoordinates.add(p.currentCoordinate);
                    possiblePaths.add(newPath);
                }
            }
            // S
            if (canMove[2]) {
                Coordinate newPosition = new Coordinate(p.currentCoordinate.Row + 1, p.currentCoordinate.Column);
                if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates))
                {
                    Path newPath = p.clone();
                    newPath.currentCoordinate = newPosition;
                    newPath.visitedCoordinates.add(p.currentCoordinate);
                    possiblePaths.add(newPath);
                }
            }
            // W
            if (canMove[3]) {
                Coordinate newPosition = new Coordinate(p.currentCoordinate.Row, p.currentCoordinate.Column - 1);
                if (okayToMove(gameBoardArray, newPosition, p.visitedCoordinates))
                {
                    Path newPath = p.clone();
                    newPath.currentCoordinate = newPosition;
                    newPath.visitedCoordinates.add(p.currentCoordinate);
                    possiblePaths.add(newPath);
                }
            }
            counter++;
            if ((counter % 1000) == 0)
                System.out.printf("Running %d paths%n", possiblePaths.size());
        }
        return maximumLength;
    }

    private static boolean okayToMove(char[][] gameBoard,
                                      Coordinate newPosition,
                                      Set<Coordinate> visitedPaths)
    {
        if (gameBoard[newPosition.Row][newPosition.Column] == '#')
            return false;

        return !visitedPaths.contains(newPosition);
    }

    private static char[][] getCharArrayGameBoard(List<String> gameBoard)
    {
        char[][] output = new char[gameBoard.size()][gameBoard.get(0).length()];
        for (int row = 0; row < gameBoard.size(); row++)
        {
            for (int column = 0; column < gameBoard.get(0).length(); column++) {
                output[row][column] = gameBoard.get(row).charAt(column);
            }
        }
        return output;
    }

}