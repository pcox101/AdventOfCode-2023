package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static class Module
    {
        String name;
        List<String> destinations = new ArrayList<>();
        Set<String> sources = new HashSet<>();
        public void addDestinations(String dests)
        {
            String[] split = dests.split(",");
            for (String s: split) {
                destinations.add(s.trim());
            }
        }
    }

    private static class FlipFlopModule extends Module
    {
    }

    private static class ConjunctionModule extends Module
    {
    }

    private static class Pulse
    {
        public Pulse(String src, String dest, boolean hp) {
            source = src;
            destination = dest;
            highPulse = hp;
        }

        String source;
        String destination;
        boolean highPulse;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day20/src/main/resources/input.txt"));

            long outputPart1;
            long outputPart2;

            Map<String, Module> modules = new HashMap<>();

            String line = br.readLine();
            while (line != null) {
                String[] split = line.split(" -> ");

                if (split[0].charAt(0) == '%')
                {
                    FlipFlopModule mod = new FlipFlopModule();
                    mod.name = split[0].substring(1);
                    mod.addDestinations(split[1].trim());
                    modules.put(mod.name, mod);
                }
                else if (split[0].charAt(0) == '&')
                {
                    ConjunctionModule mod = new ConjunctionModule();
                    mod.name = split[0].substring(1);
                    mod.addDestinations(split[1].trim());
                    modules.put(mod.name, mod);
                }
                else
                {
                    // Just an input module, probably called "broadcaster"
                    Module mod = new Module();
                    mod.name = split[0];
                    mod.addDestinations(split[1].trim());
                    modules.put(mod.name, mod);
                }

                line = br.readLine();
            }

            // Populate our sources (we need to know all the inputs that
            // a conjunction module has)
            for (Map.Entry<String, Module> entry: modules.entrySet())
            {
                for (String s: entry.getValue().destinations)
                {
                    if (modules.containsKey(s))
                    {
                        modules.get(s).sources.add(entry.getKey());
                    }
                }
            }

            outputPart1 = calculatePart1(modules);
            outputPart2 = calculatePart2(modules);

            System.out.printf("Output Part 1: %d%n", outputPart1);
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculatePart1(Map<String, Module> moduleMap)
    {
        Map<String, Boolean> currentFlipFlopStates = new HashMap<>();
        Map<String, Map<String, Boolean>> currentConjunctionStates = new HashMap<>();

        long highPulses = 0;
        long lowPulses = 0;

        // Press the button 1000 times
        for (int i = 0; i < 1000; i++)
        {
            Queue<Pulse> queue = new LinkedList<>();
            queue.add(new Pulse("","broadcaster",false));
            lowPulses++;
            while (queue.size() != 0) {
                Pulse processing = queue.remove();
                //System.out.printf("Processing %s with highPulse=%b%n", processing.destination, processing.highPulse);
                // output modules are ignored
                if (moduleMap.containsKey(processing.destination))
                {
                    Module mod = moduleMap.get(processing.destination);

                    if (mod instanceof FlipFlopModule)
                    {
                        boolean currentState = false;
                        if (currentFlipFlopStates.containsKey(mod.name))
                        {
                            currentState = currentFlipFlopStates.get(mod.name);
                        }
                        // If we receive a high pulse, it is ignored
                        // If we receive a low pulse we flip
                        if (!processing.highPulse) {
                            currentState = !currentState;
                            currentFlipFlopStates.put(processing.destination, currentState);
                            // Turns on and sends a high pulse
                            if (currentState)
                            {
                                for (String next: mod.destinations)
                                {
                                    //System.out.printf("%s highPulse=%b-> %s%n", mod.name, true, next);
                                    queue.add(new Pulse(mod.name, next, true));
                                    highPulses++;
                                }
                            }
                            else {
                                // turns off and sends a low pulse
                                for (String next : mod.destinations) {
                                    //System.out.printf("%s highPulse=%b-> %s%n", mod.name, false, next);
                                    queue.add(new Pulse(mod.name, next, false));
                                    lowPulses++;
                                }
                            }
                            currentFlipFlopStates.put(mod.name, currentState);
                        }
                    }
                    else if (mod instanceof ConjunctionModule)
                    {
                        // When a pulse is received, the conjunction module first updates
                        // its memory for that input
                        Map<String, Boolean> inputState;
                        if (currentConjunctionStates.containsKey(mod.name))
                        {
                            inputState = currentConjunctionStates.get(mod.name);
                        }
                        else {
                            inputState = new HashMap<>();
                            currentConjunctionStates.put(mod.name, inputState);
                            for (String s : mod.sources) {
                                inputState.put(s, false);
                            }
                        }
                        inputState.put(processing.source, processing.highPulse);

                        // Then, if it remembers high pulses for all inputs,
                        boolean anyLowPulse = false;
                        for (Map.Entry<String, Boolean> entry: inputState.entrySet()) {
                            if (!entry.getValue()) {
                                anyLowPulse = true;
                                break;
                            }
                        }
                        // it sends a low pulse; otherwise, it sends a high pulse.
                        for (String next: mod.destinations)
                        {
                            if (anyLowPulse)
                            {
                                //System.out.printf("%s highPulse=%b-> %s%n", mod.name, true, next);
                                queue.add(new Pulse(mod.name, next, true));
                                highPulses++;
                            }
                            else {
                                //System.out.printf("%s highPulse=%b-> %s%n", mod.name, false, next);
                                queue.add(new Pulse(mod.name, next, false));
                                lowPulses++;
                            }
                        }
                    }
                    else
                    {
                        // Must be broadcast module, nothing changes
                        // Sends the same pulse to all its destination modules
                        for (String next: mod.destinations)
                        {
                            //System.out.printf("%s highPulse=%b-> %s%n", mod.name, processing.highPulse, next);
                            queue.add(new Pulse(mod.name, next, processing.highPulse));
                            if (processing.highPulse)
                                highPulses++;
                            else
                                lowPulses++;
                        }
                    }
                }
            }
        }

        return lowPulses * highPulses;
    }

    private static long calculatePart2(Map<String, Module> moduleMap)
    {

        // This is a pretty filthy set of loops.
        // The logic is:
        // In order to get a low pulse into rx, you need high pulses
        // on all inputs to jq
        // jq receives inputs from gt, vr, nl, lr
        // Those are fed from 4 different loops

        // So what we need to know is the period of output from those four so that
        // we can then find the LCM of all the periods

        // So, run the simulation until we know when those will receive a high pulse
        Map<String, Long> periods = new HashMap<>();
        for (Map.Entry<String, Module> entry: moduleMap.entrySet())
        {
            if (entry.getValue().destinations.contains("rx")) {
                for (String grandParent : entry.getValue().sources) {
                    periods.put(grandParent, 0L);
                }
            }
        }

        Map<String, Boolean> currentFlipFlopStates = new HashMap<>();
        Map<String, Map<String, Boolean>> currentConjunctionStates = new HashMap<>();

        long counter = 0;

        // How many steps until we have information about all the periods
        boolean done = false;
        while (!done)
        {
            // Button press
            counter++;
            if ((counter % 10000) == 0)
                System.out.printf("Counter %d%n", counter);

            Queue<Pulse> queue = new LinkedList<>();
            queue.add(new Pulse("","broadcaster",false));
            while (queue.size() != 0) {
                Pulse processing = queue.remove();
                //System.out.printf("Processing %s with highPulse=%b%n", processing.destination, processing.highPulse);

                // Is this a module we care about that we don't have a period for?
                if (periods.containsKey(processing.destination)
                  && (periods.get(processing.destination) == 0)
                  && (!processing.highPulse))
                {
                    System.out.printf("Got low pulse on %s (counter=%d)%n", processing.destination, counter);
                    periods.put(processing.destination, counter);
                }

                // Do we have a period for everything
                AtomicLong minCounter = new AtomicLong(Long.MAX_VALUE);
                periods.forEach((s, l) -> { minCounter.set(Math.min(minCounter.get(), l)); });
                if (minCounter.get() > 0)
                {
                    done = true;
                    break;
                }

                // output modules are ignored
                if (moduleMap.containsKey(processing.destination))
                {
                    Module mod = moduleMap.get(processing.destination);

                    if (mod instanceof FlipFlopModule)
                    {
                        boolean currentState = false;
                        if (currentFlipFlopStates.containsKey(mod.name))
                        {
                            currentState = currentFlipFlopStates.get(mod.name);
                        }
                        // If we receive a high pulse, it is ignored
                        // If we receive a low pulse we flip
                        if (!processing.highPulse) {
                            currentState = !currentState;
                            currentFlipFlopStates.put(processing.destination, currentState);
                            // Turns on and sends a high pulse
                            if (currentState)
                            {
                                for (String next: mod.destinations)
                                {
                                    //System.out.printf("%s highPulse=%b-> %s%n", mod.name, true, next);
                                    queue.add(new Pulse(mod.name, next, true));
                                }
                            }
                            else {
                                // turns off and sends a low pulse
                                for (String next : mod.destinations) {
                                    //System.out.printf("%s highPulse=%b-> %s%n", mod.name, false, next);
                                    queue.add(new Pulse(mod.name, next, false));
                                }
                            }
                            currentFlipFlopStates.put(mod.name, currentState);
                        }
                    }
                    else if (mod instanceof ConjunctionModule)
                    {
                        // When a pulse is received, the conjunction module first updates
                        // its memory for that input
                        Map<String, Boolean> inputState;
                        if (currentConjunctionStates.containsKey(mod.name))
                        {
                            inputState = currentConjunctionStates.get(mod.name);
                        }
                        else {
                            inputState = new HashMap<>();
                            currentConjunctionStates.put(mod.name, inputState);
                            for (String s : mod.sources) {
                                inputState.put(s, false);
                            }
                        }
                        inputState.put(processing.source, processing.highPulse);

                        // Then, if it remembers high pulses for all inputs,
                        boolean anyLowPulse = false;
                        for (Map.Entry<String, Boolean> entry: inputState.entrySet()) {
                            if (!entry.getValue()) {
                                anyLowPulse = true;
                                break;
                            }
                        }
                        // it sends a low pulse; otherwise, it sends a high pulse.
                        for (String next: mod.destinations)
                        {
                            if (anyLowPulse)
                            {
                                //System.out.printf("%s highPulse=%b-> %s%n", mod.name, true, next);
                                queue.add(new Pulse(mod.name, next, true));
                            }
                            else {
                                //System.out.printf("%s highPulse=%b-> %s%n", mod.name, false, next);
                                queue.add(new Pulse(mod.name, next, false));
                            }
                        }
                    }
                    else
                    {
                        // Must be broadcast module, nothing changes
                        // Sends the same pulse to all its destination modules
                        for (String next: mod.destinations)
                        {
                            //System.out.printf("%s highPulse=%b-> %s%n", mod.name, processing.highPulse, next);
                            queue.add(new Pulse(mod.name, next, processing.highPulse));
                        }
                    }
                }
            }
        }

        // Get the LCM
        Long lc = 0L;
        Long maxNumber = 0L;
        Long lcm = 0L;
        for (Map.Entry<String, Long> entry: periods.entrySet())
        {
            maxNumber = Math.max(maxNumber, entry.getValue());
        }

        // Find the GCF
        Long gcf = maxNumber;
        while (gcf > 0)
        {
            boolean allFactors = true;
            for (Map.Entry<String, Long> entry: periods.entrySet())
            {
                if (entry.getValue() % gcf != 0)
                {
                    allFactors = false;
                }
            }

            if (allFactors) break;
            gcf--;
        }

        lcm = 1L;
        for (Map.Entry<String, Long> entry: periods.entrySet())
        {
            lcm *= entry.getValue();
        }
        lcm /= gcf;

        return lcm;
    }

}