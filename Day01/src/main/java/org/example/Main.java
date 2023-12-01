package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        int calibration = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day01/src/main/resources/input.txt"));

            String line = br.readLine();

            while (line != null) {
                int firstChar = 0, lastChar = 0;

                for (int i = 0; i < line.length(); i++) {
                    char charAt = line.charAt(i);
                    if (Character.isDigit(charAt)) {
                        if (firstChar == 0) {
                            firstChar = charAt - '0';
                        }
                        lastChar = charAt - '0';
                    } else {
                        // Is it the start of a number
                        int startNumber = startNumber(line, i);
                        if (firstChar == 0) {
                            firstChar = startNumber;
                        }
                        if (startNumber != 0) {
                            lastChar = startNumber;
                        }
                    }
                }

                calibration = calibration + (firstChar * 10) + lastChar;

                line = br.readLine();
            }
            br.close();
            System.out.println("Calibration: " + calibration);

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static int startNumber(String line, int offset) {
        String startString = line.substring(offset);
        if (startString.startsWith("three")) {
            return 3;
        }
        if (startString.startsWith("seven")) {
            return 7;
        }
        if (startString.startsWith("eight")) {
            return 8;
        }
        if (startString.startsWith("four")) {
            return 4;
        }
        if (startString.startsWith("five")) {
            return 5;
        }
        if (startString.startsWith("nine")) {
            return 9;
        }
        if (startString.startsWith("two")) {
            return 2;
        }
        if (startString.startsWith("six")) {
            return 6;
        }
        if (startString.startsWith("one")) {
            return 1;
        }
        return 0;
    }
}
