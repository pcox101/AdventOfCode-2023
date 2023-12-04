package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day04/src/main/resources/input.txt"));
            Pattern linePattern = Pattern.compile("^Card *(\\d*): ([\\d ]*) \\| ([\\d ]*)$");
            HashMap<Integer, Integer> cardPlays = new HashMap<Integer, Integer>();

            String line = br.readLine();

            Integer totalWinnings = 0;
            Integer totalScratchcards = 0;

            while (line != null) {
                Matcher lineMatcher = linePattern.matcher(line);

                if (lineMatcher.matches()) {
                    Integer game = Integer.parseInt(lineMatcher.group(1));
                    String winningNumberString = lineMatcher.group(2);
                    String myNumberString = lineMatcher.group(3);

                    Set<Integer> myNumbers = ParseNumberString(winningNumberString);
                    Set<Integer> winningNumbers = ParseNumberString(myNumberString);

                    Set<Integer> intersection = new HashSet<>(myNumbers);
                    intersection.retainAll(winningNumbers);

                    int score = 0;
                    if (intersection.size() > 0)
                        score = (int) Math.pow(2, intersection.size() - 1);
                    System.out.println(String.format("Game %d has a score %d (intersections = %d)", game, score, intersection.size()));

                    int numberOfScratchcards = 1;
                    if (cardPlays.containsKey(game)) {
                        numberOfScratchcards = cardPlays.get(game) + 1;
                    }
                    System.out.println(String.format("Game %d had a total of %d scratchcards", game, numberOfScratchcards));

                    for (int i = 0; i < intersection.size(); i++)
                    {
                        Integer gameToUpdate = i + game + 1;
                        System.out.println(String.format("Adding %d to game %d", numberOfScratchcards, gameToUpdate));
                        if (cardPlays.containsKey(gameToUpdate)) {
                            Integer oldCount = cardPlays.get(gameToUpdate);
                            System.out.println(String.format("Old value was %d", oldCount));
                            cardPlays.put(gameToUpdate, oldCount + numberOfScratchcards);
                        } else {
                            System.out.println("New game");
                            cardPlays.put(gameToUpdate, numberOfScratchcards);
                        }
                    }

                    totalWinnings += score;
                    totalScratchcards += numberOfScratchcards;
                }
                else
                {
                   throw new Exception("Line doesn't match.");
                }

                line = br.readLine();
            }
            br.close();

            System.out.println(String.format("Total Winnings: %d", totalWinnings));
            System.out.println(String.format("Total Scratchcards: %d", totalScratchcards));

        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }

    private static Set<Integer> ParseNumberString(String inputString)
    {
        HashSet<Integer> numbersToReturn = new HashSet<>();
        String[] numbers = inputString.split(" ");
        for (String number: numbers) {
            if (!number.isEmpty())
            {
                numbersToReturn.add(Integer.parseInt(number));
            }
        }
        return numbersToReturn;
    }
}