package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
            int output = 0;
            System.out.println(String.format("Output: %d", output));

        }
        catch (Exception ex)
        {
            System.out.println("There was an exception: " + ex);

        }
    }
}