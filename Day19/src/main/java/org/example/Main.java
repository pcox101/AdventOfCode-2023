package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    private static class Part
    {
        public int x;
        public int m;
        public int a;
        public int s;

        public int getSum()
        {
            return x + m + a + s;
        }

        public int getValue(String variableName)
        {
            return switch (variableName) {
                case "x" -> x;
                case "m" -> m;
                case "a" -> a;
                case "s" -> s;
                default -> 0;
            };

        }
    }

    private static class Workflow
    {
        public Workflow(String ourName, String workflow)
        {
            name = ourName;
            String[] steps = workflow.split(",");
            for (int i = 0; i < steps.length - 1; i++)
            {
                WorkflowStep wfs = new WorkflowStep();
                String[] stepPart = steps[i].split(":");
                wfs.destinationName = stepPart[1];
                wfs.variableName = stepPart[0].substring(0,1);
                wfs.ruleCondition = stepPart[0].substring(1,2);
                wfs.comparator = Integer.parseInt(stepPart[0].substring(2));
                workflowSteps.add(wfs);
            }
            WorkflowStep lastStep = new WorkflowStep();
            lastStep.destinationName = steps[steps.length - 1];
            workflowSteps.add(lastStep);
        }
        public List<WorkflowStep> workflowSteps = new ArrayList<>();
        public String name;
    }

    private static class WorkflowStep
    {
        public String ruleCondition = ">";
        public String variableName = "x";
        public int comparator = 0;

        public String destinationName;
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day19/src/main/resources/input.txt"));

            Map<String, Workflow> workflowMap = new HashMap<>();
            List<Part> parts = new ArrayList<>();

            long outputPart1;
            long outputPart2;

            String line = br.readLine();
            while (line != null) {
                while (!line.isEmpty())
                {
                    String[] s1 = line.split("\\{");
                    Workflow wf = new Workflow(s1[0], s1[1].substring(0,s1[1].length() - 1));
                    workflowMap.put(wf.name, wf);

                    line = br.readLine();
                }

                line = br.readLine();
                while (line != null) {
                    String[] vars = line.substring(1,line.length() - 1).split(",");
                    Part part = new Part();
                    part.x = Integer.parseInt(vars[0].substring(2));
                    part.m = Integer.parseInt(vars[1].substring(2));
                    part.a = Integer.parseInt(vars[2].substring(2));
                    part.s = Integer.parseInt(vars[3].substring(2));

                    parts.add(part);

                    line = br.readLine();
                }
            }

            outputPart1 = calculatePart1(parts, workflowMap);

            System.out.printf("Output Part 1: %d%n", outputPart1);
            outputPart2 = calculatePart2(workflowMap);

            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long calculatePart1(List<Part> parts, Map<String, Workflow> workflowMap)
    {
        long result = 0;

        for(Part part: parts)
        {
            //System.out.println("Next Part");
            String nextWorkflow = "in";
            while(!nextWorkflow.equals("A") && !nextWorkflow.equals("R"))
            {
                Workflow workflow = workflowMap.get(nextWorkflow);
                for (WorkflowStep step: workflow.workflowSteps)
                {
                    if (step.ruleCondition.equals(">")) {
                        if (part.getValue(step.variableName) > step.comparator)
                        {
                            nextWorkflow = step.destinationName;
                            break;
                        }
                    }
                    else
                    {
                        if (part.getValue(step.variableName) < step.comparator)
                        {
                            nextWorkflow = step.destinationName;
                            break;
                        }
                    }
                }
                //System.out.printf("Next rule: %s%n", nextWorkflow);
            }

            if (nextWorkflow.equals("A"))
            {
                //System.out.printf("xmas sum: %d%n", part.getSum());
                result += part.getSum();
            }
        }

        return result;
    }

    private static long calculatePart2(Map<String, Workflow> workflowMap)
    {
        Map<String, Set<Integer>> allowedValues = new HashMap<>();
        allowedValues.put("x", new HashSet<>());
        allowedValues.put("m", new HashSet<>());
        allowedValues.put("a", new HashSet<>());
        allowedValues.put("s", new HashSet<>());

        for (int i = 1; i <= 4000; i++)
        {
            allowedValues.get("x").add(i);
            allowedValues.get("m").add(i);
            allowedValues.get("a").add(i);
            allowedValues.get("s").add(i);
        }

        long result = numberAcceptedByRule(workflowMap, "in", allowedValues, 0);

        return result;
    }

    public static long numberAcceptedByRule(Map<String, Workflow> workflowMap,
                                            String ruleName,
                                            Map<String, Set<Integer>> allowedValues,
                                            int step)
    {
        long result = 0;

        System.out.printf("Processing rule: %s%n", ruleName);

        if (ruleName.equals("R"))
        {
            System.out.printf("Result %d%n", 0);
            return 0;
        }

        if (ruleName.equals("A"))
        {
            result = (long)allowedValues.get("x").size()
                * (long)allowedValues.get("m").size()
                * (long)allowedValues.get("a").size()
                * (long)allowedValues.get("s").size();
            System.out.printf("Result %d%n", result);
            return result;

        }

        // Process this step
        Workflow wf = workflowMap.get(ruleName);
        WorkflowStep wfs = wf.workflowSteps.get(step);

        if (wfs.comparator == 0) {
            // Recurse
            return numberAcceptedByRule(workflowMap, wfs.destinationName, allowedValues, 0);
        }

        // Inclusive Set
        {
            Map<String, Set<Integer>> newAllowed = new HashMap<>(allowedValues);

            Set<Integer> passOnSet = new HashSet<>(newAllowed.get(wfs.variableName));

            if (wfs.ruleCondition.equals(">")) {
                for (int i = 1; i <= wfs.comparator; i++)
                    passOnSet.remove(i);
            }
            if (wfs.ruleCondition.equals("<")) {
                for (int i = wfs.comparator; i <= 4000; i++)
                    passOnSet.remove(i);
            }

            newAllowed.put(wfs.variableName, passOnSet);

            result += numberAcceptedByRule(workflowMap, wfs.destinationName, newAllowed, 0);
        }

        // Exclusive Set
        {
            Map<String, Set<Integer>> newAllowed = new HashMap<>(allowedValues);
            Set<Integer> otherSet = new HashSet<>(newAllowed.get(wfs.variableName));

            if (wfs.ruleCondition.equals(">")) {
                for (int i = 1; i <= wfs.comparator; i++)
                    otherSet.remove(i);
            }
            if (wfs.ruleCondition.equals("<")) {
                for (int i = wfs.comparator; i <= 4000; i++)
                    otherSet.remove(i);
            }

            // Take anything that wasn't in that set
            Set<Integer> passOnSet = new HashSet<>(newAllowed.get(wfs.variableName));
            passOnSet.removeAll(otherSet);

            newAllowed.put(wfs.variableName, passOnSet);

            result += numberAcceptedByRule(workflowMap, ruleName, newAllowed,step + 1);
        }

        System.out.printf("Result %d%n", result);

        return result;
    }
}