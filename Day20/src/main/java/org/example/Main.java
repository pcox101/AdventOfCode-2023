package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day20/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            String line = br.readLine();
            while (line != null) {

                line = br.readLine();
            }

            System.out.printf("Output Part 1: %d%n", outputPart1);
            System.out.printf("Output Part 2: %d%n", outputPart2);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}