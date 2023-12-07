package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Day07/src/main/resources/input.txt"));

            String line = br.readLine();
            List<Hand> hands = new ArrayList<>();
            List<HandWithJoker> handsWithJokers = new ArrayList<>();

            while (line != null) {
                String[] fields = line.split(" ");
                hands.add(new Hand(fields[0], Integer.parseInt(fields[1])));
                handsWithJokers.add(new HandWithJoker(fields[0], Integer.parseInt(fields[1])));

                line = br.readLine();
            }

            Collections.sort(hands);
            Collections.sort(handsWithJokers);

            Integer handRank = 0;
            for (int rank = 0; rank < hands.size(); rank++)
            {
                handRank += (rank + 1) * hands.get(rank).Score;
            }
            System.out.println(String.format("Output without Jokers: %d", handRank));

            Integer handWithJokerRank = 0;
            for (int rank = 0; rank < handsWithJokers.size(); rank++)
            {
                handWithJokerRank += (rank + 1) * handsWithJokers.get(rank).Score;
            }
            System.out.println(String.format("Output with Jokers: %d", handWithJokerRank));


        } catch (Exception ex) {
            System.out.println("There was an exception: " + ex);
        }
    }
}


