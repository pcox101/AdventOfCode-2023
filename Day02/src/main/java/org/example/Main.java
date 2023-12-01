package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        int outputValue = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day02/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {
                System.out.println("The data: " + line);


                line = br.readLine();
            }
            br.close();
            System.out.println("Output Value: " + outputValue);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}