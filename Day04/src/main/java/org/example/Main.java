package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day04/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {

                line = br.readLine();
            }
            br.close();
        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}