package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day07/src/main/resources/input.txt"));

            String line = br.readLine();
            Integer output = 0;

            while (line != null) {


                line = br.readLine();
            }

            System.out.println(String.format("Output %d", output));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}