package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day09/src/main/resources/input.txt"));

            String line = br.readLine();
            while (line != null) {


                line = br.readLine();
            }
            Integer output = 0;
            System.out.println(String.format("Output: ",output));

       } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}