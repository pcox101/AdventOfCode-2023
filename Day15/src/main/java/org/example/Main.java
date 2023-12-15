package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {
    private static class Box
    {
        public List<Lens> Lenses = new ArrayList<>();
        public Box()
        {
            Lenses = new ArrayList<>();
        }

        public void addLens(Lens lens)
        {
            int lensToRemove = -1;
            for (int i = 0; i < Lenses.size(); i++)
            {
                if (Lenses.get(i).Name.equals(lens.Name)) {
                    lensToRemove = i;
                    break;
                }
            }
            if (lensToRemove != -1) {
                Lenses.set(lensToRemove, lens);
            }
            else
            {
                Lenses.add(lens);
            }
        }

        public void removeLens(String lensName)
        {
            int lensToRemove = -1;
            for (int i = 0; i < Lenses.size(); i++)
            {
                if (Lenses.get(i).Name.equals(lensName)) {
                    lensToRemove = i;
                    break;
                }
            }
            if (lensToRemove != -1)
            {
                Lenses.remove(lensToRemove);
            }
        }
    }

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
        Box[] boxes = new Box[256];

        for (int i = 0; i < inputData.length; i++)
        {
            StringBuilder lensLabel = new StringBuilder();
            for (int j = 0; j < inputData[i].length(); j++)
            {
                char letter = inputData[i].charAt(j);
                if (letter == '-') {
                    int boxHash = calculateHASH(lensLabel.toString());

                    if (boxes[boxHash] == null)
                    {
                        boxes[boxHash] = new Box();
                    }
                    boxes[boxHash].removeLens(lensLabel.toString());
                }
                else if (letter == '=') {
                    // next character is the focal length, appears to be only 1 digit
                    int focalLength = inputData[i].charAt(j + 1) - '0';
                    int boxHash = calculateHASH(lensLabel.toString());
                    // add it to the map
                    if (boxes[boxHash] == null)
                    {
                        boxes[boxHash] = new Box();
                    }
                    boxes[boxHash].addLens(new Lens(lensLabel.toString(), focalLength));
                }
                else {
                    lensLabel.append(letter);
                }
            }

            //outputBoxes(boxes);
        }

        // Calculate the focussing power
        long counter = 0;
        for (int i = 0; i < boxes.length; i++)
        {
            if (boxes[i] != null) {
                for (int j = 0; j < boxes[i].Lenses.size(); j++) {
                    long lensPower = (i + 1) * (j + 1) * boxes[i].Lenses.get(j).FocalLength;
                    System.out.println(String.format("%s: %d", boxes[i].Lenses.get(j).Name, lensPower));
                    counter += lensPower;
                }
            }
        }

        return counter;
    }

    private static void outputBoxes(Box[] boxes)
    {
        for (int i = 0; i < boxes.length; i++)
        {
            if (boxes[i] != null) {
                System.out.print(String.format("Box %d: ", i));
                for (int j = 0; j < boxes[i].Lenses.size(); j++)
                {
                    Lens lens = boxes[i].Lenses.get(j);
                    System.out.print(String.format("[%s %d] ", lens.Name, lens.FocalLength));
                }
                System.out.println();
            }
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