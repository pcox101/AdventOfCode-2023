package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        try {
            int output = 0;
            BufferedReader br = new BufferedReader(new FileReader("Day03/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {

                line = br.readLine();
            }
            br.close();
            System.out.println(String.format("Output: %s", output));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }


}
