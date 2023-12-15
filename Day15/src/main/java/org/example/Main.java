package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    private static class Lens
    {
        public String Name;
        public Integer FocalLength;

        public Lens(String name, Integer focalLength)
        {
            Name = name;
            FocalLength = focalLength;
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day15/src/main/resources/input.txt"));

            String line = br.readLine();
            long outputPart1 = 0;
            long outputPart2 = 0;

            List<String> gameBoard = new ArrayList<>();
            while (line != null) {
                gameBoard.add(line);

                line = br.readLine();
            }

            // only one line separated by ","
            String[] inputData = gameBoard.get(0).split(",");

            // Part 1 is easy!
            for (int i = 0; i < inputData.length; i++) {
                long hash = calculateHASH(inputData[i]);
                //System.out.println(String.format("HASH of %s: %d", inputData[i], hash));
                outputPart1 += hash;
            }
            System.out.println(String.format("Output Part 1: %d", outputPart1));

            // Part 2...
            outputPart2 = doPart2(inputData);

            System.out.println(String.format("Output Part 2: %d", outputPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static long doPart2(String[] inputData)
    {
        Map<Integer, LinkedHashMap<String, Integer>> boxes = new HashMap<>();

        for (int i = 0; i < inputData.length; i++)
        {
            StringBuilder lensLabel = new StringBuilder();
            for (int j = 0; j < inputData[i].length(); j++)
            {
                char letter = inputData[i].charAt(j);
                if (letter == '-') {
                    int boxHash = calculateHASH(lensLabel.toString());

                    if (boxes.containsKey(boxHash))
                    {
                        if (boxes.get(boxHash).containsKey(lensLabel.toString()))
                        {
                            boxes.get(boxHash).remove(lensLabel.toString());
                        }
                    }
                }
                else if (letter == '=') {
                    // next character is the focal length, appears to be only 1 digit
                    int focalLength = inputData[i].charAt(j + 1) - '0';
                    int boxHash = calculateHASH(lensLabel.toString());
                    // add it to the map
                    if (boxes.containsKey(boxHash))
                    {
                        if (boxes.get(boxHash).containsKey(lensLabel.toString()))
                        {
                            boxes.get(boxHash).replace(lensLabel.toString(), focalLength);
                        }
                        else
                        {
                            boxes.get(boxHash).put(lensLabel.toString(), focalLength);
                        }
                    }
                    else
                    {
                        LinkedHashMap<String, Integer> boxContents = new LinkedHashMap<>();
                        boxContents.put(lensLabel.toString(), focalLength);
                        boxes.put(boxHash, boxContents);
                    }
                }
                else {
                    lensLabel.append(letter);
                }
            }

            //outputBoxes(boxes);
        }

        // Calculate the focussing power
        long counter = 0;
        for (Map.Entry<Integer, LinkedHashMap<String, Integer>> entry : boxes.entrySet()) {

            int j = 1;
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                long lensPower = (entry.getKey() + 1) * j * entry2.getValue();
                System.out.println(String.format("%s: %d", entry2.getKey(), lensPower));
                counter += lensPower;
                j++;
            }
        }

        return counter;
    }

    private static void outputBoxes(Map<Integer, LinkedHashMap<String, Integer>> boxes)
    {

        for (Map.Entry<Integer, LinkedHashMap<String, Integer>> entry : boxes.entrySet())
        {
            System.out.print(String.format("Box %d: ", entry.getKey()));
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet())
            {
                System.out.print(String.format("[%s %d] ", entry2.getKey(), entry2.getValue()));
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int calculateHASH(String stringToHash) {
        long hashValue = 0;
        for (int i = 0; i < stringToHash.length(); i++)
        {
            hashValue += (long)stringToHash.charAt(i);
            hashValue *= 17;
            hashValue = hashValue % 256;
        }
        return (int)hashValue;
    }
}