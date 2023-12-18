package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day18/src/main/resources/input.txt"));

            long outputPart1 = 0;
            long outputPart2 = 0;

            String line = br.readLine();
            while (line != null) {


                line = br.readLine();
            }

            System.out.println(String.format("Output Part 1: %d", outputPart1));
            System.out.println(String.format("Output Part 2: %d", outputPart2));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}