package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            Pattern cubePattern = Pattern.compile("^ ?(\\d*) (red|blue|green)$");
            Pattern gamePattern = Pattern.compile("^Game (\\d*): (.*)$");

            BufferedReader br = new BufferedReader(new FileReader("Day02/src/main/resources/input.txt"));
            Integer matchingGames = 0;
            Integer completePower = 0;
            String line = br.readLine();

            while (line != null) {
                Matcher gameMatcher = gamePattern.matcher(line);

                if (gameMatcher.matches()) {
                    Boolean possible = true;
                    Integer gamePower = 0;
                    Integer gameNumber = Integer.parseInt(gameMatcher.group(1));
                    Integer minimumRed = 0;
                    Integer minimumBlue = 0;
                    Integer minimumGreen = 0;
                    String game = gameMatcher.group(2);

                    // Split by semi colon
                    String[] plays = game.split(";");
                    for (String play : plays) {
                        // Split by comma
                        String[] cubes = play.split(",");
                        for (String cube : cubes) {
                            Matcher cubeMatcher = cubePattern.matcher(cube);

                            if (cubeMatcher.matches()) {
                                Integer cubeNumber = Integer.parseInt(cubeMatcher.group(1));
                                String colour = cubeMatcher.group(2);

                                switch (colour) {
                                    case "red":
                                        if (cubeNumber > 12) {
                                            possible = false;
                                        }
                                        if (cubeNumber > minimumRed)
                                            minimumRed = cubeNumber;
                                        break;
                                    case "blue":
                                        if (cubeNumber > 14) {
                                            possible = false;
                                        }
                                        if (cubeNumber > minimumBlue)
                                            minimumBlue = cubeNumber;
                                        break;
                                    case "green":
                                        if (cubeNumber > 13) {
                                            possible = false;
                                        }
                                        if (cubeNumber > minimumGreen)
                                            minimumGreen = cubeNumber;
                                        break;
                                }
                            }
                            else
                            {
                                System.out.println("No match : " + cube);
                            }
                        }
                    }

                    if (possible) {
                        System.out.println(String.format("Game %d is possible", gameNumber));
                        matchingGames += gameNumber;
                    } else {
                        System.out.println(String.format("Game %d is not possible", gameNumber));
                    }
                    gamePower = minimumRed * minimumBlue * minimumGreen;
                    completePower = completePower + gamePower;
                    System.out.println(String.format("Game %d has power %d", gameNumber, gamePower));

                }
                line = br.readLine();
            }
            br.close();
            System.out.println(String.format("Matching game score: %d",matchingGames));
            System.out.println(String.format("Complete Power: %d",completePower));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}