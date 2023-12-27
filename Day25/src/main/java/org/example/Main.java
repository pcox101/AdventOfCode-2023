package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    static class Component
    {
        Component(String name){
            this.name = name;
        }
        String name;
        Map<String, Component> components = new HashMap<>();
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day25/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            Map<String, Component> components = new HashMap<>();

            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(":");

                Component c = components.getOrDefault(split[0], new Component(split[0]));
                for (String connected: split[1].trim().split(" "))
                {
                    Component linkedComponent = components.getOrDefault(connected, new Component(connected));
                    linkedComponent.components.put(c.name, c);
                    c.components.put(linkedComponent.name, linkedComponent);
                    components.put(linkedComponent.name, linkedComponent);
                }
                components.put(c.name, c);

                line = br.readLine();
            }

            // Determine which 3 can be cut to split the groups into 2
            // We do this randomly...
            // Let's pick 2 random points and identify which edges fall in the shortest path
            // Then do this again, repeatedly for some number of times.
            // As we have 2 interconnected graphs, the top 3 nodes in our list should
            // be the linking nodes between the two
            // So split by those and then count...
            List<String> componentNames = new ArrayList<>(components.keySet());
            Map<String, Integer> edgeCount = new HashMap<>();
            Random r = new Random();

            for (int i = 0; i < 100; i++)
            {
                Component start = components.get(componentNames.get(r.nextInt(componentNames.size())));
                Component end = components.get(componentNames.get(r.nextInt(componentNames.size())));

                getEdges(components, start, end, edgeCount);
                System.out.println(i);
            }

            String remove1 = Collections.max(edgeCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            edgeCount.remove(remove1);
            String remove2 = Collections.max(edgeCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            edgeCount.remove(remove2);
            String remove3 = Collections.max(edgeCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            edgeCount.remove(remove3);

            // Split the graphs by these
            removeEdge(remove1, components);
            removeEdge(remove2, components);
            removeEdge(remove3, components);

            // DFS on each side
            String[] split = remove1.split("-");
            Set<String> componentList1 = getAllLinked(split[0], components);
            Set<String> componentList2 = getAllLinked(split[1], components);

            outputPart1 = (long)componentList1.size() * (long)componentList2.size();

            System.out.printf("Output Part 1: %d%n", outputPart1);

            // No part 2 on day 25
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    static Set<String> getAllLinked(String startComponentName, Map<String, Component> map)
    {
        Set<String> visitedComponents = new HashSet<>();

        Queue<Component> nextComponents = new LinkedList<>();
        nextComponents.add(map.get(startComponentName));

        while (nextComponents.size() > 0) {
            Component thisEntry = nextComponents.remove();

            for (Map.Entry<String, Component> entry: thisEntry.components.entrySet())
            {
                if (!visitedComponents.contains(entry.getKey()))
                {
                    visitedComponents.add(entry.getKey());
                    nextComponents.add(entry.getValue());
                }
            }
        }

        return visitedComponents;
    }

    static void removeEdge(String edge, Map<String, Component> map)
    {
        String[] split = edge.split("-");

        map.get(split[1]).components.remove(split[0]);
        map.get(split[0]).components.remove(split[1]);
    }

    static void getEdges(Map<String, Component> components,
                         Component start,
                         Component end,
                         Map<String, Integer> edgeCount)
    {
        class DFSEntry
        {
            List<String> visitedComponents = new ArrayList<>();
            Component nextComponent;
        }

        if (start == end)
        {
            return;
        }

        Queue<DFSEntry> nextComponents = new LinkedList<>();
        DFSEntry e = new DFSEntry();
        e.nextComponent = start;
        nextComponents.add(e);

        while (true)
        {
            DFSEntry thisEntry = nextComponents.remove();

            if (thisEntry.nextComponent.name.equals(end.name))
            {
                // Add our path into the counters and quit
                String edge = start.name;
                for (int i = 1; i < thisEntry.visitedComponents.size(); i++)
                {
                    String vertex;
                    if (edge.compareTo(thisEntry.visitedComponents.get(i)) < 0)
                        vertex = String.format("%s-%s", edge, thisEntry.visitedComponents.get(i));
                    else
                        vertex = String.format("%s-%s", thisEntry.visitedComponents.get(i), edge);
                    if (edgeCount.containsKey(vertex))
                        edgeCount.put(vertex, edgeCount.get(vertex) + 1);
                    else
                        edgeCount.put(vertex, 1);
                    edge = thisEntry.visitedComponents.get(i);
                }
                break;
            }

            for (Map.Entry<String, Component> entry: thisEntry.nextComponent.components.entrySet())
            {
                if (!thisEntry.visitedComponents.contains(entry.getKey()))
                {
                    DFSEntry newEntry = new DFSEntry();
                    newEntry.nextComponent = entry.getValue();
                    newEntry.visitedComponents = new ArrayList<>(thisEntry.visitedComponents);
                    newEntry.visitedComponents.add(entry.getKey());
                    nextComponents.add(newEntry);
                }
            }
        }

        // Shortest path achieved
    }
}